import { CommonTypes } from '../../types/common'
import FullWidthButton from './FullWidthButton'

export default function TogglePopUp({ title, message, buttonText, onClick }: CommonTypes.TogglePopUp) {

    return (
        <>
            <div className="fixed top-0 left-0 w-full h-full bg-black bg-opacity-50 flex justify-center items-center z-50">
                <div className="mx-auto w-popup-width p-popup-padding bg-primary-grey fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 text-white">
                    <p className='text-title-24 font-bold text-center mx-auto mb-2'>
                        {title}
                    </p>
                    <p className='text-title-16 font-semibold text-center mx-auto mb-2'>
                        {message}
                    </p>
                    <FullWidthButton
                        buttonText={buttonText}
                        onclick={onClick}
                    />
                </div>
            </div>
        </>
    )
}