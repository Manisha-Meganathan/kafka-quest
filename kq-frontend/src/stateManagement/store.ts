import { configureStore } from "@reduxjs/toolkit";
import gameStateSlice from "./gameStates";
import uiStateSlice from "./uiStates";
import webSocketStatesSlice from "./webSocketStates";

export const gameStore = configureStore({
    reducer: {
        game : gameStateSlice.reducer,
        ui : uiStateSlice.reducer,
        ws: webSocketStatesSlice.reducer
    }
})

export type RootState = ReturnType<typeof gameStore.getState>