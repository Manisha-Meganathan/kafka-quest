import { PayloadAction } from "@reduxjs/toolkit";
import {webSocketState} from "./webSocketStates";

export const setConn = (state: webSocketState, action: PayloadAction<boolean>) => {
    console.log("setting connection state: " + action.payload)
    state.isSocketConnected = action.payload
}

export const setIsReconnecting = (state: webSocketState, action: PayloadAction<boolean>) => {
    console.log("setting isReconnecting: ", action.payload);
    state.isReconnecting = action.payload
}

export const setTrackingId = (state: webSocketState, action: PayloadAction<string>) => {
    console.log("setting trackingId: ", action.payload);
    state.trackingId = action.payload
}

export const setIsStartedEventPublished = (state: webSocketState, action: PayloadAction<boolean>) => {
    console.log("setting isStartedEventPublished: ", action.payload);
    state.isGameStartedEventPublished = action.payload
}

export const setDisconnectionTrigger = (state: webSocketState, action: PayloadAction<boolean>) => {
    console.log("setting disconnection trigger: ", action.payload);
    state.hasDisconnectActionTriggered = action.payload
}

export const resetReconnectAttemptsToDefault = (state: webSocketState) => {
    console.log("setting reconnectAttempts to 0");
    state.reconnectAttempts = 0
}

export const increment = (state: webSocketState) => {
    console.log("Incrementing reconnectAttempts by 1");
    state.reconnectAttempts += 1
}