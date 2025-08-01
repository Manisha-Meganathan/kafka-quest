import { createSlice } from "@reduxjs/toolkit";
import {
    setStartButton,
    setLoginPopup,
    setGameEndPopup,
    setLoading,
    setShouldShowHowToPlay,
    setShouldShowGameClosedOfFailure
} from "./uiActions";

export interface UiStates {
    shouldShowLoginPopup: boolean,
    shouldEnableStartButton: boolean,
    shouldShowGameLoading: boolean,
    shouldShowGameEndPopup: boolean,
    shouldShowHowToPlay: boolean,
    shouldShowGameClosedOfFailure: boolean
}

let initialUiState : UiStates= {
    shouldEnableStartButton: false,
    shouldShowGameEndPopup: false,
    shouldShowGameLoading: false,
    shouldShowLoginPopup: false,
    shouldShowHowToPlay: false,
    shouldShowGameClosedOfFailure: false
}

export const uiStateSlice = createSlice({
    name: "UI",
    initialState : initialUiState,
    reducers: {
        setIsStartButtonEnabled: setStartButton,
        toggleLoginPopup: setLoginPopup,
        toggleGameEndPopup: setGameEndPopup,
        setIsLoading: setLoading,
        toggleHowToPlay: setShouldShowHowToPlay,
        toggleClosedOfFailure: setShouldShowGameClosedOfFailure
    }
})

export const {
    setIsStartButtonEnabled, 
    toggleGameEndPopup, 
    toggleLoginPopup, 
    setIsLoading,
    toggleHowToPlay,
    toggleClosedOfFailure
} = uiStateSlice.actions

export default uiStateSlice