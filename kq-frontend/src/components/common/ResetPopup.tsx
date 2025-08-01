import iconClose from '../../assets/icons/svg/icon_close.svg'
import { UiTexts } from '../../consts/uiTexts'
import { CommonTypes } from '../../types/common'
import FullWidthButton from './FullWidthButton'

interface ResetGamePopUp {
    title: string,
    onClose: CommonTypes.Closable["close"],
    onClick: () => void
}

export default function ResetPopup({ title, onClose, onClick }: ResetGamePopUp) {

    return (
        <>
            <div className="fixed top-0 left-0 w-full h-full bg-black bg-opacity-50 flex justify-center items-center z-50">
                <div className="mx-auto w-popup-width p-popup-padding bg-primary-grey fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 text-white">
                    <button className="absolute -top-3 -right-3" onClick={() => onClose()}>
                        <img src={iconClose} />
                    </button>
                    <p className='text-title-16 font-semibold text-center mx-auto'>
                        {title}
                    </p>
                    <p className='text-title-16 font-semibold text-center mx-auto'>
                        {UiTexts.resetWarningMesssage}
                    </p>
                    <FullWidthButton
                        buttonText="Reset the game"
                        onclick={onClick}
                    />
                </div>
            </div>
        </>
    )
}