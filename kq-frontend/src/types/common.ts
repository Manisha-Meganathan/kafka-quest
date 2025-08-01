import { UUID } from "crypto";
import { MutableRefObject, ReactNode } from "react";
import { AddedPiece } from "../stateManagement/gameStates";

export namespace CommonTypes {
  
  export interface CustomBtn {
    icon: string,
    text: string,
    bgColor: string,
    onClick?: () => void,
    isEnabled?: boolean
  }

  export interface FullWidthPrimaryButton {
    buttonText: string,
    onclick: () => void
  }

  export interface ButtonStyles {
    default: string;
    primaryBlue: string;
    primaryGray: string
  }

  export interface Grid {
    horizontalRows: number,
    verticalColumns: number,
    addedPieces: AddedPiece[]
  }

  export interface PiecePanel {
    horizontalRows: number,
    verticalColumns: number
  }

  export interface DragPieceHolder {
    size: number,
    rowNumber: number,
    columnNumber: number
  }

  export interface DraggablePiece {
    size: number
    pieceId: UUID,
    pieceData: string
  }

  export interface EventSideDock {
    isVisible: boolean,
    setIsVisible: React.Dispatch<React.SetStateAction<boolean>>
  }

  export interface PopUpCommon {
    title: string,
    onClose: Closable["close"],
    children: ReactNode
}

export interface Closable {
  close: () => void
}

export interface DemoRequest {
  firstName : string,
  lastName : string,
  email : string,
  companyName : string,
  companyRole : string,
  companySize : string,
  country : string,
  contactNumber : string,
  message : string
}

export interface Player {
  playerId? : number,
  username : string
}

export interface GameProps {
  isGameStarted: boolean
}

export interface BottomLeftButtonBarProps {
  isGameStarted: boolean
  handleStartButtonClick: () => void
  onGameResetRequest: () => void
  onShuffleButtonClick: () => void
}

export interface TogglePopUp {
  title: string,
  message: string,
  buttonText: string,
  onClick: () => void
}

export interface VideoProps {
  videoRef: MutableRefObject<HTMLVideoElement | null>;
  isPlaying: boolean;
  togglePlay: () => void;
}

export enum ApiCallType {
  LOGIN,
  REGISTER,
  INIT_VALUE
}

  export enum TimerState {
    RUNNING,
    PAUSED,
    RESET
  }
export enum GameSizes {
  _4X4 = "4 X 4",
  _5X5 = "5 X 5",
}

}