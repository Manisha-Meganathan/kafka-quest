import { useCallback, useEffect, useMemo, useState } from "react";
import { CommonTypes } from "../../types/common";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../stateManagement/store";
import { updateTimerState } from "../../stateManagement/gameStates";

function Timer() {
  const [time, setTime] = useState<number>(0);
  const isGameStarted = useSelector((state: RootState) => state.game.isGameStarted);
  const timerState = useSelector((state: RootState) => state.game.timerState);
  const dispatchAction = useDispatch();

  const displayTime = useCallback(() => {
    const currTime = time;
    const minutes = Math.floor(currTime / 60);
    const seconds = Math.floor(currTime  % 60);

    let displayMinutes = minutes.toString();
    let displaySeconds = seconds.toString();

    if (minutes < 10) {
      displayMinutes = `0${displayMinutes}`;
    }

    if (seconds < 10) {
      displaySeconds = `0${displaySeconds}`
    }

      return (
        <>
          <p className="text-[54px] mr-16 lg:text-[64px] lg:ml-16 lg:mr-0">
            {displayMinutes}:{displaySeconds}
          </p>
        </>
      )
  }, [time]);

  useEffect(() => {
    if (isGameStarted) {
      dispatchAction(updateTimerState(CommonTypes.TimerState.RUNNING))
    } else {
      dispatchAction(updateTimerState(CommonTypes.TimerState.RESET))
    }
  }, [isGameStarted]);

  
  useEffect(() => {
    let timerId: any;

    handleTimerState(timerState);
    if (timerState == CommonTypes.TimerState.RUNNING) {
      timerId = setInterval(() => {
        setTime((prevTime) => prevTime + 1);
      }, 1000);
    }

    return () => {
      clearInterval(timerId);
    };

  }, [timerState]);

  const handleTimerState = (state: CommonTypes.TimerState) => {
    if (state == CommonTypes.TimerState.RESET) {
      setTime(0);
    }
    dispatchAction(updateTimerState(state));
  };


  return (
    <>
      {displayTime()}
    </>
  )
}

export default Timer;