import { useState } from 'react';
import axios, { AxiosError } from 'axios';
import { CommonTypes } from '../types/common';
import { getApiEndPoint } from "../consts/endpoints"
import { useDispatch } from 'react-redux';
import { setPlayerData as setPlayerDataToState } from '../stateManagement/gameStates';
import { toggleLoginPopup } from '../stateManagement/uiStates';

const useAuth = () => {
    const [loading, setLoading] = useState(false)
    const [message, setMessage] = useState("")
    const [isLoggedIn, setIsLoggedIn] = useState(false)
    const [playerData, setPlayerData] = useState<CommonTypes.Player>()
    const dispatchAction = useDispatch()

    const apiCall = async (callType: CommonTypes.ApiCallType, username: string) => {
        console.log("Making Api call: " + callType.toLocaleString())
        const url = getApiEndPoint(callType)

        setLoading(true)
        const user: CommonTypes.Player = {
            username: username
        }

        await axios
            .post(url, user)
            .then((response) => {
                const playerData = response.data as unknown as CommonTypes.Player

                if (playerData.playerId && playerData.username) {
                    setIsLoggedIn(true)
                    setPlayerData(playerData)

                    dispatchAction(setPlayerDataToState(playerData))

                    console.log(`player logged in with ID: ${playerData.playerId}, userName: ${playerData.username}`)
                } else {
                    console.log("error, player data null!")
                }

            })
            .catch((err: AxiosError | any) => {
                console.log(err)
                if(err.response?.status === 400) {
                    if(err.response?.data?.title && (err.response.data.title === "User Name Exists"  
                        || err.response.data.title==="Username Validation Failed" )) {
                        setMessage(err.response.data.detail)
                        dispatchAction(toggleLoginPopup())
                        return
                    }
                    setMessage(err.response?.data?.violations[0].message)
                    return
                } else if(err.response?.status === 401) {
                    setMessage(err.response?.data.detail)
                    return
                } else {
                    setMessage("Bad request: "+ err.message)
                }
            })
            .finally(() => {
                setLoading(false)
            });
    };

    function login(userName: string) {
        const callType = CommonTypes.ApiCallType.LOGIN
        apiCall(callType, userName)
    }

    function register(userName: string) {
        const callType = CommonTypes.ApiCallType.REGISTER
        apiCall(callType, userName)
    }

    return { loading, message, isLoggedIn, playerData, login, register};
}

export default useAuth