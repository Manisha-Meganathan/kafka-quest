import { createSlice } from "@reduxjs/toolkit";
import { UUID } from "crypto";
import { CommonTypes } from "../types/common";
import { EventTypes } from "../types/events";
import {
    setIsStarted,
    setPlayer,
    setGameId,
    updatePieces,
    setScore,
    start,
    shuffle,
    endGame,
    revertCount,
    addEvent,
    reset,
    setTimerState,
} from "./gameActions";


export interface AddedPiece {
    piece: EventTypes.JigsawPuzzlePiece
    rowPosition: number
    columnPosition: number
}

export interface puzzlePices {
    draggablePieces: EventTypes.JigsawPuzzlePiece[],
    remainingPices: EventTypes.JigsawPuzzlePiece[],
    addedPieces: AddedPiece[],
}

export interface GameEvent {
    eventNumber: number,
    eventType: string,
    response: string,
    timeStamp: number,
    eventStreamId: number
    revertFromEventStreamId: number | null
}

export interface gameState {
    gameSize: number,
    horizontalSize: number,
    verticalSize: number,
    player: CommonTypes.Player,
    isLoggedIn: boolean,
    puzzlePieces: puzzlePices,
    isGameStarted: boolean,
    gameId: UUID | null,
    isReloadedFromLocalStorage: boolean,
    scoreCard: EventTypes.ScoreCard | null,
    eventsList: GameEvent[],
    numberOfEventsToRevert: number,
    timerValue: number,
    timerState: CommonTypes.TimerState,
}

export const initialGameState: gameState = {
    player: {
        playerId: 0,
        username: ""
    },
    isLoggedIn: false,
    gameSize: 16,
    horizontalSize: 4,
    verticalSize: 4,
    puzzlePieces: {
        draggablePieces: [],
        remainingPices: [],
        addedPieces: []
    },
    isGameStarted: false,
    gameId: null,
    isReloadedFromLocalStorage: false,
    scoreCard: null,
    eventsList: [],
    numberOfEventsToRevert: 0,
    timerValue: 0,
    timerState: CommonTypes.TimerState.PAUSED,
}

const gameStateSlice = createSlice({
    name: "game",
    initialState: initialGameState,
    reducers: {
        setIsGameStarted: setIsStarted,
        setPlayerData: setPlayer,
        setCurrentGameId: setGameId,
        updatePuzzlePieces: updatePieces,
        setScorecard: setScore,
        shufflePieces: shuffle,
        startGame: start,
        endTheGame: endGame,
        setRevertCount: revertCount,
        addNewEvent: addEvent,
        resetGame: reset,
        updateTimerState: setTimerState,
    }
})

export const {
    setIsGameStarted,
    setCurrentGameId,
    setPlayerData,
    updatePuzzlePieces,
    setScorecard,
    shufflePieces,
    startGame,
    endTheGame,
    setRevertCount,
    addNewEvent,
    resetGame,
    updateTimerState,
} = gameStateSlice.actions

export default gameStateSlice