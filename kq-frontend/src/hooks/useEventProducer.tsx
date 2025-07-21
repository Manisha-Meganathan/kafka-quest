import { EventTypes } from "../types/events";
import { UUID } from "crypto";
import { variableTexts } from "../consts/variableTexts";

function useEventProducer() {

  function produceGameStartedEvent(playerId: number, verticalSize: number, horizontalSize: number): EventTypes.UIEvent {
    let eventData: EventTypes.GameStartedEventData = {
      puzzleImageId: 1,
      verticalSize: verticalSize,
      horizontalSize: horizontalSize,
      puzzlePieces: []
    }

    return {
      eventInfo: {
        gameId: null,
        playerId: playerId,
        eventType: EventTypes.EventType.GAME_STARTED_EVENT,
        initiatedBy: EventTypes.EventInitiatedBy.FRONT_END
      },
      eventData
    }
  }

  function producePieceAddedEvent(piece: EventTypes.JigsawPuzzlePiece, rowPosition: number, columnPosition: number,playerId: number, gameId: string): EventTypes.UIEvent {
    return {
      eventInfo: {
        gameId: gameId,
        playerId: playerId,
        eventType: EventTypes.EventType.PIECE_ADDED_EVENT,
        initiatedBy: EventTypes.EventInitiatedBy.FRONT_END
      },
      eventData: {
        pieceId: piece.pieceId,
        movedRowPosition: rowPosition,
        movedColumnPosition: columnPosition
      }
    }
  }

  function producePieceRemovedEvent(pieceId: UUID, columnPosition: number, rowPosition: number, gameId: string, playerId: number): EventTypes.UIEvent {
    return {
      eventInfo: {
        gameId: gameId,
        playerId: playerId,
        eventType: EventTypes.EventType.PIECE_REMOVED_EVENT,
        initiatedBy: EventTypes.EventInitiatedBy.FRONT_END
      },
      eventData: {
        pieceId: pieceId,
        removedRowPosition: rowPosition,
        removedColumnPosition: columnPosition
      }
    }
  }

  function produceRevertMoveEvent(eventStreamId: number, gameId: string, playerId: number): EventTypes.UIEvent {
    return {
      eventInfo: {
        gameId: gameId,
        playerId: playerId,
        eventType: EventTypes.EventType.REVERT_MOVES_EVENT,
        initiatedBy: EventTypes.EventInitiatedBy.FRONT_END
      },
      eventData: {
        eventStreamId: eventStreamId
      }
    }
  }

  return { 
    producePieceAddedEvent, 
    producePieceRemovedEvent, 
    produceGameStartedEvent,
    produceRevertMoveEvent
  }
}

export default useEventProducer