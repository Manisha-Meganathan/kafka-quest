import { toggleGameEndPopup } from "../../stateManagement/uiStates"
import PopUpCommon from "./PopUpCommon"
import { useDispatch } from "react-redux"
import { EventTypes } from "../../types/events"
import { useEffect, useState } from "react"
import { endTheGame } from "../../stateManagement/gameStates"
import { GameUtils } from "../../utils/gameUtils"
import useWebSocket from "../../hooks/useWebSocket";

export default function GameEndPopup({grade, timeSpent} : EventTypes.ScoreCard) {
    const [elapsedTime, setElapsedTime] = useState("")
    const dispatchAction = useDispatch()
    const { disconnectWS } = useWebSocket()

    useEffect(()=>{
        setElapsedTime(GameUtils.durationFromLongMillis(timeSpent))
    },[timeSpent, grade])

    const endGame = ()=> {
        dispatchAction(toggleGameEndPopup())
        dispatchAction(endTheGame())
        disconnectWS()
    }
    

    return(
        <PopUpCommon title="Game Ended!" onClose={()=> endGame()}>
            <>
            <div className="w-full h-fit p-5 flex flex-col gap-2">
                <h3 > Grade: <b> {grade} </b> </h3>
                <h3> Elapsed Time: <b> {elapsedTime} </b> </h3>
            </div>
        </>
        </PopUpCommon>
    )
}