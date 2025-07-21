import { UUID } from "crypto"
import { Interface } from "readline"

export namespace EventTypes {

  // event data

  export interface EventData {

  }

  export interface GameStartedEventData extends EventData {
    puzzleImageId: Number,
    horizontalSize: Number,
    verticalSize: Number
    puzzlePieces: JigsawPuzzlePiece[] | null
  }

  export interface PieceAddedEventData extends EventData {
    pieceId: UUID,
    movedRowPosition: number,
    movedColumnPosition: number,
    hasMovedToCorrectPosition: boolean,
    scoreCard: ScoreCard
  }

  export interface PieceRemovedEventData extends EventData {
    pieceId: UUID
    removedRowPosition: number
    removedColumnPosition: number
  }

  // puzzle 

  export interface JigsawPuzzlePiece {
    pieceId: UUID,
    pieceData: string
  }

  export enum EventType {
    GAME_STARTED_EVENT = 0,
    PIECE_ADDED_EVENT = 1,
    PIECE_REMOVED_EVENT = 2,
    REVERT_MOVES_EVENT = 3,
    SAVE_CURRENT_STATE_EVENT = 4,
    STARTED_FROM_SAVED_STATE_EVENT = 5,
    GAME_PAUSED_EVENT = 6,
    GAME_RESUMED_EVENT = 7,
    INIT_VALUE = 8
  }

  export enum EventInitiatedBy {
    FRONT_END,
    API,
    AGGREGATE,
    PROJECTION
  }

  export interface EventInfo {
    gameId: any,
    playerId?: number // made optional for quick fix
    eventType: EventType,
    initiatedBy: EventInitiatedBy
  }

  export interface UIEvent {
    eventInfo: EventInfo,
    eventData: EventData
  }

  export enum ResponseMessage {
    SUCCESS,
    FAILURE
  }

  export interface ProjectionEventResponse {
    playerId: number
    gameId: UUID
    eventType: EventType
    timestamp: number
    eventStreamId: number
    revertFromEventStreamId: number | null
    responseMessage: ResponseMessage
    responseData: GameStartedEventResponseData | PieceAddedEventResponseData | PieceRemovedEventResponseData
    initiatedBy: EventInitiatedBy
  }

  export interface GameStartedEventResponseData {
    puzzlePieces: Array<JigsawPuzzlePieceResponse>
  }

  interface JigsawPuzzlePieceResponse {
    pieceId: UUID
    pieceData: string
  }

  export interface ScoreCard {
    timeSpent: number;
    grade: Grade;
  }

  export enum Grade {
    BAD,
    GOOD,
    EXCELLENT
  }

  export interface RevertMovesEventData extends EventData {
    eventStreamId: number
  }

  export interface PieceAddedEventResponseData {
    pieceId: UUID,
    hasGameEnded: boolean,
    scoreCard: ScoreCard,
    movedColumnPosition: number,
    movedRowPosition: number }

  export interface PieceRemovedEventResponseData
  {
      pieceId: UUID,
      removedRowPosition: number,
      removedColumnPosition: number
  }

  export enum ServerNotifications {
    NO_MESSAGES_TO_DELIVER,
    UNDELIVERED_RESPONSES_DELIVERY_STARTED,
    UNDELIVERED_RESPONSES_DELIVERY_COMPLETED
  }
}
