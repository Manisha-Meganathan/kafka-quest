import { createSlice } from "@reduxjs/toolkit";
import {
    setConn,
    setDisconnectionTrigger,
    setIsReconnecting,
    setIsStartedEventPublished,
    setTrackingId,
    resetReconnectAttemptsToDefault,
    increment
} from "./webSocketActions";
import { v4 as uuidv4 } from "uuid";

export interface webSocketState {
    isSocketConnected: boolean,
    trackingId: string,
    isGameStartedEventPublished: boolean,
    hasDisconnectActionTriggered: boolean,
    isReconnecting: boolean
    reconnectInterval: number,
    maxReconnectAttempts: number,
    reconnectAttempts: number
}

export const initialWebSocketState: webSocketState = {
    isSocketConnected: false,
    trackingId: uuidv4().toString(),
    isGameStartedEventPublished: false,
    hasDisconnectActionTriggered: false,
    isReconnecting: false,
    reconnectInterval: 3000,
    maxReconnectAttempts: 3,
    reconnectAttempts: 0
}

export const webSocketStatesSlice = createSlice({
    name: "WS",
    initialState: initialWebSocketState,
    reducers: {
        setIsSocketConnected: setConn,
        updateTrackingId: setTrackingId,
        setIsGameStartedEventPublished: setIsStartedEventPublished,
        triggerDisconnection: setDisconnectionTrigger,
        updateIsReconnecting: setIsReconnecting,
        resetReconnectAttempts: resetReconnectAttemptsToDefault,
        incrementReconnectAttempts: increment
    }
})

export const {
    setIsSocketConnected,
    updateTrackingId,
    setIsGameStartedEventPublished,
    triggerDisconnection,
    updateIsReconnecting,
    resetReconnectAttempts,
    incrementReconnectAttempts
} = webSocketStatesSlice.actions

export default webSocketStatesSlice