import { useDispatch } from "react-redux";
import { GameEvent, addNewEvent, endTheGame, puzzlePices, setCurrentGameId, setScorecard, shufflePieces, startGame, updatePuzzlePieces, updateTimerState, setIsGameStarted } from "../stateManagement/gameStates";
import { EventTypes } from "../types/events";
import { setIsLoading, toggleGameEndPopup } from "../stateManagement/uiStates";
import { PieceUpdate, UpdatePieceAction } from "../stateManagement/gameActions";
import { UUID } from "crypto";
import { CommonTypes } from "../types/common";

export default function useResponseHandler() {
    const dispatchAction = useDispatch()
    const failure = "FAILURE"

    function handleProjectionResponse(response: EventTypes.ProjectionEventResponse) {
        console.log(`New response received*************************** \nEvent: ${response.eventType.toString()} \nInitiatedBy: ${response.initiatedBy.toString()}`)
        if (response.responseMessage.toString() === failure) {
            handleEventFailure(response)
            return // response is not further handled
        }

        const onGameStartedResponse = () => {
            console.log("Handling game started event response..")
            let responseData = response.responseData as EventTypes.GameStartedEventResponseData
            if (responseData.puzzlePieces) {

                let puzzlePieces: puzzlePices = {
                    draggablePieces: [],
                    addedPieces: [],
                    remainingPices: responseData.puzzlePieces
                }

                dispatchAction(setCurrentGameId(response.gameId))
                dispatchAction(startGame(puzzlePieces))
                dispatchAction(shufflePieces())
                dispatchAction(setIsLoading(false))
            }
        }

        const onPieceAddedEventResponse = () => {
            console.log("Handling piece added event response..")
            let responseData = response.responseData as EventTypes.PieceAddedEventResponseData
            console.log(responseData)

            if (response.initiatedBy.toString() !== "FRONT_END") {
                console.log("Revert action: adding back to added list")
                const action = createAction(responseData.pieceId, responseData.movedRowPosition, responseData.movedColumnPosition, PieceUpdate.ADD, true)
                dispatchAction(updatePuzzlePieces(action))
            }

            if (responseData.scoreCard) {
                let scoreCard = responseData.scoreCard as EventTypes.ScoreCard
                dispatchAction(setScorecard(scoreCard))
                dispatchAction(toggleGameEndPopup())
                dispatchAction(updateTimerState(CommonTypes.TimerState.RESET))
                return
            }
        }

        const onPieceRemovedEventResponse = () => {
            console.log("Handling piece removed event response..")

            if (response.initiatedBy.toString() !== "FRONT_END") {
                console.log("Revert action: removing from added list")
                const responseData = response.responseData as EventTypes.PieceRemovedEventResponseData
                const action = createAction(responseData.pieceId, responseData.removedRowPosition, responseData.removedColumnPosition, PieceUpdate.REMOVE, true)
                dispatchAction(updatePuzzlePieces(action))
            }
        }

        switch (response.eventType.toString()) {
            case "GAME_STARTED_EVENT":
                onGameStartedResponse()
                break
            case "PIECE_ADDED_EVENT":
                onPieceAddedEventResponse()
                break
            case "PIECE_REMOVED_EVENT":
                onPieceRemovedEventResponse()
                break
            default:
                console.log("Unhandled response: ", response.eventType.toString())
                break
        }

        addToEventList(response)
    }

    function addToEventList(projectionResponse: EventTypes.ProjectionEventResponse) {
        const eventType = projectionResponse.eventType.toString()
        const response = projectionResponse.responseMessage.toString()
        const timeStamp = projectionResponse.timestamp
        const eventStreamId = projectionResponse.eventStreamId
        const revertFromEventStreamId = projectionResponse.revertFromEventStreamId

        let event: GameEvent = {
            eventNumber: 0, //always passed as zero and updated to correct eventNumber by the reducer based on array size
            eventType,
            response,
            timeStamp,
            eventStreamId,
            revertFromEventStreamId
        }
        dispatchAction(addNewEvent(event))
    }

    function handleEventFailure(response: EventTypes.ProjectionEventResponse) {
        switch (response.eventType.toString()) {
            case "GAME_STARTED_EVENT":
                onGameStartedEventFail()
                return
            case "PIECE_ADDED_EVENT":
                onPieceAddedEventFail(response)
                return
            case "PIECE_REMOVED_EVENT":
                onPieceRemovedEventFail(response)
                return
            default:
                // not implemented yet
                console.log("Event failed, Event: ", response.eventType)
                return
        }
    }

    const onGameStartedEventFail = () => {
        window.alert("Failed to start the game")
        dispatchAction(endTheGame()) // this will reset the game to initial state
    }

    const onPieceAddedEventFail = (response: EventTypes.ProjectionEventResponse) => {
        window.alert("Move failed!") // replace this with existing common popup

        // remove the added piece from addedList
        const data = response.responseData as EventTypes.PieceAddedEventResponseData
        const action = createAction(data.pieceId, data.movedRowPosition, data.movedColumnPosition, PieceUpdate.REMOVE, false)
        dispatchAction(updatePuzzlePieces(action))
    }

    const onPieceRemovedEventFail = (response: EventTypes.ProjectionEventResponse) => {
        window.alert("Remove failed!")

        // add back to the added list with previous row, column position
        const data = response.responseData as EventTypes.PieceRemovedEventResponseData
        const action = createAction(data.pieceId, data.removedRowPosition, data.removedColumnPosition, PieceUpdate.ADD, false)
        dispatchAction(updatePuzzlePieces(action)) 
    }

    const createAction = (pieceId: UUID, rowPosition: number, columnPosition: number, update: PieceUpdate, isRevert: boolean): UpdatePieceAction => {
        return {
            update: update,
            piece: {
                pieceId: pieceId,
                pieceData: ""
            },
            rowPosition: rowPosition,
            columnPosition: columnPosition,
            isRevertUpdate: isRevert
        }
    }

    return { handleProjectionResponse }
}