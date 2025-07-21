import {useDispatch} from "react-redux";
import {toggleClosedOfFailure} from "../../stateManagement/uiStates";
import {resetGame} from "../../stateManagement/gameStates";
import PopUpCommon from "./PopUpCommon";

export default function GameClosedOfFailure() {
    const dispatchAction = useDispatch()

    const onClose = () => {
        dispatchAction(dispatchAction(toggleClosedOfFailure(false)))
        dispatchAction(resetGame())
    }

    return (
        <PopUpCommon title="" onClose={onClose}>
            <p className='text-title-16 font-semibold text-center mx-auto'>
                Failed Reconnecting!
            </p>
        </PopUpCommon>
    )
}