import { PayloadAction } from "@reduxjs/toolkit"
import { UUID } from "crypto"
import { CommonTypes } from "../types/common"
import { EventTypes } from "../types/events"
import { GameUtils } from "../utils/gameUtils"
import { AddedPiece, GameEvent, gameState, puzzlePices } from "./gameStates"

export const setPlayer = (state: gameState, action: PayloadAction<CommonTypes.Player>) => {
    console.log("setting player ", action.payload)
    state.player.playerId = action.payload.playerId
    state.player.username = action.payload.username
    state.isLoggedIn = true
}

export const start = (state: gameState, action: PayloadAction<puzzlePices>) => {
    console.log("initializing puzzle pieces ", action.payload)
    state.puzzlePieces.remainingPices = action.payload.remainingPices
    state.isGameStarted = true
    shuffle(state)
}

export const endGame = (state: gameState) => {
    state.gameId = null
    state.isGameStarted = false
    state.puzzlePieces.addedPieces = []
    state.player.playerId = 0
    state.player.username = ""
    state.isLoggedIn = false
    state.scoreCard = null
    state.isReloadedFromLocalStorage = false
    state.eventsList = []
    state.timerState = CommonTypes.TimerState.RESET
    
    console.log("Game ended!")
}

export const setSize = (state: gameState, action: PayloadAction<number>) => {
    console.log("setting puzzle size ", action.payload)
    state.gameSize = action.payload
}

export const setIsStarted = (state: gameState, action: PayloadAction<boolean>) => {
    console.log("setting isstarted ", action.payload);
    state.isGameStarted = action.payload
}

export const setGameId = (state: gameState, action: PayloadAction<UUID>) => {
    console.log("setting gameId ", action.payload);
    state.gameId = action.payload
}

export const setScore = (state: gameState, action: PayloadAction<EventTypes.ScoreCard>) => {
    console.log("setting score card:" + action.payload)
    state.scoreCard = action.payload
}

export enum PieceUpdate {
    REMOVE,
    ADD
}

export interface UpdatePieceAction {
    update: PieceUpdate
    piece: EventTypes.JigsawPuzzlePiece
    rowPosition: number | null
    columnPosition: number | null
    isRevertUpdate?: boolean
}

export const updatePieces = (state: gameState, action: PayloadAction<UpdatePieceAction>) => {
    console.log("updating puzzle pieces..");
    var puzzlePiece: EventTypes.JigsawPuzzlePiece | undefined

    const addedlist = [...state.puzzlePieces.addedPieces];
    const remaininglist = [...state.puzzlePieces.remainingPices];
    const dragablelist = [...state.puzzlePieces.draggablePieces];

    if(action.payload.isRevertUpdate) {
        state.numberOfEventsToRevert -= 1
        puzzlePiece = remaininglist.find(i=> i.pieceId === action.payload.piece.pieceId)
    } else {
        puzzlePiece = action.payload.piece
    }

    function onPieceAdded() {
        const newItem: AddedPiece = {
            piece: puzzlePiece!!,
            rowPosition: action.payload.rowPosition!!,
            columnPosition: action.payload.columnPosition!!
        };
        const updatedDragableList = dragablelist.filter(item => item.pieceId !== newItem.piece.pieceId)
        const updatedRemainingList = remaininglist.filter(item => item.pieceId !== newItem.piece.pieceId)

        state.puzzlePieces = {
            ...state.puzzlePieces,
            addedPieces: [...addedlist, newItem],
            draggablePieces: updatedDragableList,
            remainingPices: updatedRemainingList
        };
    }

    function onPieceRemoved() {
        const pieceToRemove = addedlist.find(item => item.piece.pieceId === action.payload.piece.pieceId);

        if (pieceToRemove) {
            const updatedAddedList = addedlist.filter(item => item.piece.pieceId !== action.payload.piece.pieceId);
            const updatedRemainingList = [...remaininglist, pieceToRemove.piece];

            state.puzzlePieces = {
                ...state.puzzlePieces,
                addedPieces: updatedAddedList,
                remainingPices: updatedRemainingList
            };
        }
    }

    switch (action.payload.update) {
        case PieceUpdate.ADD:
            onPieceAdded();
            break;
        case PieceUpdate.REMOVE:
            onPieceRemoved();
            break;
        default:
            break;
    }
};


export const shuffle = (state: gameState) => {
    if (state.isGameStarted && state.puzzlePieces.remainingPices.length > 0) {
        console.log("starting to shuffle..")
        let remainingPieces = state.puzzlePieces.remainingPices
        let draggablePieces = state.puzzlePieces.draggablePieces

        // include current dragable pieces to the shuffle as well
        if (draggablePieces) {
            draggablePieces.forEach(piece => {
                remainingPieces.push(piece)
            })
        }
        let shuffledPieces = GameUtils.getShuffledPieces(remainingPieces)

        // remove shuffeled items from remaining list
        remainingPieces = remainingPieces.filter((el) => !shuffledPieces.includes(el));

        state.puzzlePieces.remainingPices = remainingPieces
        state.puzzlePieces.draggablePieces = shuffledPieces
    } else {
        console.log("Game is not started or remaining pieces are empty!")
    }
}

export const revertCount = (state: gameState, action: PayloadAction<number>) => {
    const indexOfEvent_ToRevertFrom = state.eventsList.findIndex((event)=> event.eventNumber === action.payload)
    const eventList = [...state.eventsList]
    const eventsToBeReverted = eventList.splice(indexOfEvent_ToRevertFrom + 1)
    state.numberOfEventsToRevert = eventsToBeReverted.length
}

export const addEvent = (state: gameState, action: PayloadAction<GameEvent>) => {
    const event: GameEvent = {
        eventNumber: state.eventsList.length + 1,
        eventType: action.payload.eventType,
        response: action.payload.response,
        timeStamp: action.payload.timeStamp,
        eventStreamId: action.payload.eventStreamId,
        revertFromEventStreamId: action.payload.revertFromEventStreamId
    } 
    console.log("Adding new event to list: ",action.payload.eventType)
    state.eventsList.push(event)
}

export const reset = (state: gameState) => {
    state.gameId = null
    state.isGameStarted = false
    state.puzzlePieces.addedPieces = []
    state.puzzlePieces.draggablePieces = []
    state.puzzlePieces.remainingPices = []
    state.scoreCard = null
    state.isReloadedFromLocalStorage = false
    state.numberOfEventsToRevert = 0
    state.eventsList = []
    state.timerState = CommonTypes.TimerState.RESET

    console.log("Game reset!")
}

export const setTimerState = (state: gameState, action: PayloadAction<CommonTypes.TimerState>) => {
    state.timerState = action.payload
}

export const setHorizontalSize = (state: gameState, action: PayloadAction<number>) => {
    console.log("setting horizontal puzzle size ", action.payload)
    state.horizontalSize = action.payload
}

export const setVerticalSize = (state: gameState, action: PayloadAction<number>) => {
    console.log("setting vertical puzzle size ", action.payload)
    state.verticalSize = action.payload
}