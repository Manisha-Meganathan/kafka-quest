import play from '../assets/icons/svg/play.svg'
import shuffle from '../assets/icons/svg/shuffle.svg'
import info from '../assets/icons/svg/info.svg'
import reset from '../assets/icons/svg/reset.svg'
import kafka_icon from '../assets/icons/svg/icon_kafka.svg'

import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../stateManagement/store";
import { setIsLoading, toggleHowToPlay, toggleLoginPopup } from "../stateManagement/uiStates";
import useWebSocket from "../hooks/useWebSocket";
import { DndProvider } from "react-dnd";
import { HTML5Backend } from "react-dnd-html5-backend";
import LoginOrSignUp from "../components/LoginOrSignUp/LoginOrSignUp";
import { ButtonWithMx } from "../components/common/Button";
import GameEndPopup from "../components/common/GameEndPopup";
import EventSideDock from "../components/game/eventlist/EventSideDock";
import Grid from "../components/game/puzzle/Grid";
import PiecePanel from "../components/game/puzzle/PiecePanel";
import { resetGame, setHorizontalPuzzleSize, setPuzzleSize, setVerticalPuzzleSize, shufflePieces } from '../stateManagement/gameStates'
import { useEffect, useState } from 'react'
import Loader from '../components/common/Loader'
import Timer from '../components/common/Timer'
import CustomDropDown from '../components/common/CustomDropdown'
import { CommonTypes } from '../types/common'
import { UiTexts } from '../consts/uiTexts';
import ResetPopup from '../components/common/ResetPopup'
import { triggerDisconnection } from "../stateManagement/webSocketStates";
import GameClosedOfFailure from "../components/common/GameClosedOfFailure";

function Dashboard() {
  const uiState = useSelector((state: RootState) => state.ui)
  const gameState = useSelector((state: RootState) => state.game)
  const webSocketState = useSelector((state: RootState) => state.ws)
  const dispatchAction = useDispatch()
  const { connectWS, disconnectWS } = useWebSocket()
  const [gameSize, setGameSize] = useState(CommonTypes.GameSizes._5X5)
  const [showResetPopup, toggleResetPopup] = useState(false)
  const [isDropDownDisabled, setIsDropDownDisabled] = useState(false);
  const [isMobileView, setIsMobileView] = useState(false);

  const handleStartButtonClick = () => {
    if (gameState.isLoggedIn && !gameState.isGameStarted) {
      console.log("user logged in, starting game..")
      connectWS()
      return
    }
    else if (!gameState.isLoggedIn) {
      dispatchAction(toggleLoginPopup())
    } else {
      console.log("Already logged in, game already started!")
    }
  }

  const handleGameSizeChange = (selectedSize: CommonTypes.GameSizes) => {
    const sizeMappings: Record<CommonTypes.GameSizes, { horizontalSize: number; verticalSize: number }> = {
      [CommonTypes.GameSizes._4X4]: { horizontalSize: 4, verticalSize: 4 },
      [CommonTypes.GameSizes._5X5]: { horizontalSize: 5, verticalSize: 5 },
      [CommonTypes.GameSizes._6X6]: { horizontalSize: 6, verticalSize: 6 },
    };

    const { horizontalSize, verticalSize } = sizeMappings[selectedSize];

    dispatchAction(setHorizontalPuzzleSize(horizontalSize));
    dispatchAction(setVerticalPuzzleSize(verticalSize));
    dispatchAction(setPuzzleSize(horizontalSize * verticalSize))

    setGameSize(selectedSize);
    console.log("Selected game size:", selectedSize);
  };

  useEffect(() => {
    console.log("State change detected: ", gameState)
  }, [gameState])

  const onLoginCallBack = () => {
    dispatchAction(setIsLoading(true))
    connectWS()
  }

  const onGameResetRequest = () => {
    if (gameState.isGameStarted) {
      setIsDropDownDisabled(true);
      dispatchAction(resetGame())
      dispatchAction(setIsLoading(true))
      disconnectWS()
      connectWS()
      toggleResetPopup(!showResetPopup)
    }
  }

  const onGameResetRequestPopUp = () => {
    toggleResetPopup(!showResetPopup);
  }

  useEffect(() => {
    const handleResize = () => {
      setIsMobileView(window.innerWidth <= 768);
    };
    handleResize();
    window.addEventListener('resize', handleResize);
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);

  return (
    <>
      {uiState.shouldShowGameLoading && !webSocketState.isReconnecting && <Loader message={"Starting game.."} />}
      {gameState.numberOfEventsToRevert > 0 && <Loader message={"Reverting moves.."} />}
      {uiState.shouldShowLoginPopup && <LoginOrSignUp onLogin={onLoginCallBack} />}
      {webSocketState.isReconnecting && <Loader message={"Trying to reconnect..."} />}
      {uiState.shouldShowGameClosedOfFailure && <GameClosedOfFailure />}
      {gameState.scoreCard && uiState.shouldShowGameEndPopup && <GameEndPopup grade={gameState?.scoreCard?.grade!!} timeSpent={gameState?.scoreCard?.timeSpent!!} />}

      {!isMobileView ? (
        <div className="columns-2 flex flex-row w-screen h-screen bg-primary-black text-white align-top justify-between overflow-y">
          {/** left column */}
          <div className="col flex flex-col w-[78%] px-5 overflow-y custom-scrollbar">
            {/** header area */}
            <div className="columns-3 flex justify-between items-center mb-2 lg:mb-0">
              <Timer />
              <HeaderButtons isGameStarted={gameState.isGameStarted} />
            </div>

            {/** piece grid and panel */}
            <div className="columns-2 w-full flex flex-row justify-center">
              <DndProvider backend={HTML5Backend}>
                <div className='flex flex-row'>
                  <div className='mr-1.5 lg:mr-2'>
                    <Grid horizontalRows={gameState.horizontalSize} verticalColumns={gameState.verticalSize} addedPieces={gameState.puzzlePieces.addedPieces} />
                  </div>
                  <div className='ml-1.5 lg:ml-2'>
                    <PiecePanel gameSize={gameState.gameSize} draggablePieces={gameState.puzzlePieces.draggablePieces} />
                  </div>
                </div>
              </DndProvider>
            </div>

            {/** bottom buttons row */}
            <div className="columns-2 flex justify-between items-center mt-6">
              <BottomLeftButtonBar
                isGameStarted={gameState.isGameStarted}
                handleStartButtonClick={handleStartButtonClick}
                onGameResetRequest={onGameResetRequestPopUp}
                onShuffleButtonClick={() => dispatchAction(shufflePieces())} />

              <CustomDropDown
                title='Game size'
                options={Object.assign(CommonTypes.GameSizes)}
                defaultOption={gameSize}
                onChange={(selectedItem) => { handleGameSizeChange(selectedItem as unknown as CommonTypes.GameSizes) }}
                disabled={isDropDownDisabled || gameState.isGameStarted}
              />
            </div>
          </div>

          {/** right column (Event dock area) */}
          <div className="col w-[22%]">
            <EventSideDock />
          </div>
        </div>
      ) : (
        <div className="fixed top-0 left-0 w-full h-full bg-black bg-opacity-50 flex justify-center items-center z-50">
          <div className="w-[90%] p-6 bg-gray-800 rounded-md text-white">
            <p className="text-title-24 text-center font-semibold mb-4">{UiTexts.noteWarning}</p>
            <div className='text-center'>{UiTexts.resolutionWarningMessage}</div>
          </div>
        </div>
      )}
      {showResetPopup && <ResetPopup
        onClick={onGameResetRequest}
        onClose={() => toggleResetPopup(!showResetPopup)}
        title={UiTexts.resetWarningConfirmation} />}
    </>
  )
}

function HeaderButtons({ isGameStarted }: CommonTypes.GameProps) {
  const dispatchAction = useDispatch()
  return (
    <>
      <div className='col flex flex-col flex-nowrap justify-end my-4 lg:flex-row'>
        <div className="flex mb-2 items-right justify-end lg:mr-3 lg:ml-2 lg:mb-0">
          <ButtonWithMx icon={kafka_icon} bgColor='default' text='Kafka UI' marginX={0} onClick={() => { window.open(process.env.REACT_APP_KAFKA_UI_URL, "_blank") }} isGameStarted={isGameStarted} />
        </div>
        <div>
          <ButtonWithMx icon={info} bgColor="default" text="How to play" marginX={0} onClick={() => { dispatchAction(toggleHowToPlay()) }} isGameStarted={isGameStarted} />
        </div>
      </div>
    </>
  )
}

function BottomLeftButtonBar({ isGameStarted, handleStartButtonClick, onGameResetRequest, onShuffleButtonClick }: CommonTypes.BottomLeftButtonBarProps) {
  return (
    <>
      <div className='col flex flex-row flex-nowrap items-center mb-4'>
        <ButtonWithMx icon={play} bgColor={isGameStarted ? "primaryGray" : "primaryBlue"} text="Start" onClick={handleStartButtonClick} marginX={0} isGameStarted={isGameStarted} />
        <ButtonWithMx icon={reset} bgColor="default" text="Reset" onClick={onGameResetRequest} marginX={10} isGameStarted={isGameStarted} />
        <ButtonWithMx icon={shuffle} bgColor="default" text="Re-Shuffle" onClick={onShuffleButtonClick} marginX={0} isGameStarted={isGameStarted} />
      </div>
    </>
  )
}


export default Dashboard;