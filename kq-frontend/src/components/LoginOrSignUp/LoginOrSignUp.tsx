import { UiTexts } from "../../consts/uiTexts";
import PopUpCommon from "../common/PopUpCommon";
import { useEffect, useState } from "react";
import FullWidthButton from "../common/FullWidthButton";
import { toggleLoginPopup } from "../../stateManagement/uiStates";
import { useDispatch } from "react-redux";
import { setPlayerData } from '../../stateManagement/gameStates'
import useAuth from "../../hooks/UseAuth";
import { GameUtils } from "../../utils/gameUtils";

interface loginProps {
    onLogin: ()=> void
}
export default function LoginOrSignUp({onLogin}: loginProps) {
    const { message, loading, isLoggedIn, playerData, register} = useAuth();
    const dispatchAction = useDispatch()

    useEffect(()=>{
        if(isLoggedIn) {
            dispatchAction(setPlayerData(playerData!!))
            dispatchAction(toggleLoginPopup())
            onLogin()
        }
    },[isLoggedIn])

    useEffect(()=>{
        register(GameUtils.GenerateRandomUsername(10));
    },[])

    return (
        <>
            <PopUpCommon title={UiTexts.loginPopupTitle} onClose={()=> dispatchAction(toggleLoginPopup())}>
                {loading && <p>Setting up the game ... </p>}
                {message && <p className="text-red-700 text-subtitle-14">{message}</p>}
            </PopUpCommon>
        </>
    )
}


