import { PayloadAction } from "@reduxjs/toolkit"
import { UiStates } from "./uiStates"

export const setStartButton = (state: UiStates, action: PayloadAction<boolean>) => {
    state.shouldEnableStartButton = action.payload
}
export const setLoginPopup = (state: UiStates) => {
    state.shouldShowLoginPopup = !state.shouldShowLoginPopup
}
export const setGameEndPopup = (state: UiStates) => {
    state.shouldShowGameEndPopup = !state.shouldShowGameEndPopup
}
export const setLoading = (state: UiStates, action: PayloadAction<boolean>) => {
    state.shouldShowGameLoading = action.payload
}
export const setShouldShowHowToPlay = (state: UiStates) => {
    state.shouldShowHowToPlay = !state.shouldShowHowToPlay
}

export const setShouldShowGameClosedOfFailure = (state: UiStates, action: PayloadAction<boolean>) => {
    state.shouldShowGameClosedOfFailure = action.payload
}