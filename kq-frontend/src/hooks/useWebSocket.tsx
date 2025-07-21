import {Client, IFrame} from "@stomp/stompjs";
import {
    eventDefaultEndPoint,
    gameStartEventEndPoint,
    getSubscriptionForEventResponses,
    getSubscriptionForUndeliveredResponsesProcessTracking,
    triggerUndeliveredResponsesDeliveryEndPoint
} from "../consts/endpoints";
import {EventTypes} from "../types/events";
import useResponseHandler from "./useResponseHandler";
import useEventProducer from "./useEventProducer";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../stateManagement/store";
import {
    incrementReconnectAttempts,
    resetReconnectAttempts,
    setIsGameStartedEventPublished,
    setIsSocketConnected,
    triggerDisconnection,
    updateIsReconnecting,
    updateTrackingId
} from "../stateManagement/webSocketStates";
import {v4 as uuidv4} from "uuid";
import {toggleClosedOfFailure} from "../stateManagement/uiStates";
import ServerNotifications = EventTypes.ServerNotifications;

const client = new Client({
    brokerURL: process.env.REACT_APP_WEBSOCKET_URL,
})

export default function useWebSocket() {
    const { handleProjectionResponse } = useResponseHandler();
    const { produceGameStartedEvent } = useEventProducer()
    const dispatchAction = useDispatch()
    const gameState = useSelector((state: RootState) => state.game)
    const webSocketState = useSelector((state: RootState) => state.ws)


    const onResponse = (message: any) => {
        const response = JSON.parse(JSON.parse(JSON.stringify(message.body))) as EventTypes.ProjectionEventResponse;
        handleProjectionResponse(response)
    }

    async function disconnectWS() {
        if(client.active) {
            console.log("Deactivating socket connection..")
            dispatchAction(triggerDisconnection(true))
            await client.deactivate() // to reset game with new session
            dispatchAction(triggerDisconnection(false))
            dispatchAction(updateTrackingId(uuidv4().toString()))
            dispatchAction(setIsGameStartedEventPublished(false))
            console.log("Socket connection deactivated!")
        }
    }

    function connectWS() {
        if (!client.active) {
            console.log("Activating socket connection..")
            client.activate()
            console.log("Socket connection Activated!")
        }
    }

    client.onConnect = (frame) => {
        dispatchAction(setIsSocketConnected(true))

        let playerId = gameState.player.playerId!!
        let verticalSize = gameState.verticalSize
        let horizontalSize = gameState.horizontalSize
        let trackingId = webSocketState.trackingId;

        let headers = { 'player-id': playerId.toString(), 'tracking-id': trackingId }

        client.subscribe(getSubscriptionForEventResponses(trackingId), onResponse)

        if(!webSocketState.isGameStartedEventPublished) {
            let msg = produceGameStartedEvent(playerId, verticalSize, horizontalSize)

            console.log("Publishing game started event")
            client.publish(
                {
                    destination: gameStartEventEndPoint,
                    body: JSON.stringify(msg),
                    headers: headers
                }
            )
            console.log("Published game started event")

            dispatchAction(setIsGameStartedEventPublished(true))
        } else if (webSocketState.isReconnecting) {

            let deliveryNotificationSub = client.subscribe(
                getSubscriptionForUndeliveredResponsesProcessTracking(trackingId),
                function (message) {

                    let response = JSON.parse(message.body) as ServerNotifications

                    console.log(response)

                    if (response === ServerNotifications.NO_MESSAGES_TO_DELIVER) {
                        deliveryNotificationSub.unsubscribe()
                        dispatchAction(updateIsReconnecting(false))
                        console.log("No undelivered messages")
                    }

                    if (response === ServerNotifications.UNDELIVERED_RESPONSES_DELIVERY_STARTED) {
                        console.log("Undelivered messages delivery Started")
                    }

                    if (response === ServerNotifications.UNDELIVERED_RESPONSES_DELIVERY_COMPLETED) {
                        deliveryNotificationSub.unsubscribe()
                        dispatchAction(updateIsReconnecting(false))
                        console.log("Undelivered messages delivery Completed")
                    }
                }
            )

            client.publish(
                {
                    destination: triggerUndeliveredResponsesDeliveryEndPoint,
                    headers: headers
                }
            )

            dispatchAction(updateIsReconnecting(false))
        }

    }

    client.configure = (conf) => {
        conf.reconnectDelay = 0 // disabled provided auto-reconnect mechanism
    }

    client.onStompError = (frame: IFrame): void => {
        console.log("stomp error..")
    }

    client.onWebSocketClose = async (): Promise<void> => {
        dispatchAction(setIsSocketConnected(false))
        if (!webSocketState.hasDisconnectActionTriggered) {
            dispatchAction(updateIsReconnecting(true))

            let reconnectAttempts = webSocketState.reconnectAttempts
            let maxReconnectAttempts = webSocketState.maxReconnectAttempts
            let reconnectInterval = webSocketState.reconnectInterval

            if (reconnectAttempts < maxReconnectAttempts) {
                setTimeout(() => {
                    console.log("Attempting to reconnect...");
                    connectWS();
                    dispatchAction(incrementReconnectAttempts());
                }, reconnectInterval);
            } else {
                console.error("Max reconnect attempts reached.");
                dispatchAction(resetReconnectAttempts())
                dispatchAction(updateIsReconnecting(false))
                dispatchAction(toggleClosedOfFailure(true))
                await disconnectWS()
            }
        }
        console.log("Socket disconnected!")
    }

    function publishEvent(message: EventTypes.UIEvent) {
        if (client.active) {
            console.log("Publishing event, event: " + message.eventInfo.eventType)
            client.publish({ destination: eventDefaultEndPoint, body: JSON.stringify(message) })
        } else {
            console.log("unable to publish, connection null");
        }
    }

    return { publishEvent, connectWS, disconnectWS }
}