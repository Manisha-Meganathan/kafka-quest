import iconClose from '../../assets/icons/svg/icon_close.svg'
import { CommonTypes } from '../../types/common'

export default function PopUpCommon({ title, onClose, children }: CommonTypes.PopUpCommon) {

    return (
        <>
            <div className="fixed top-0 left-0 w-full h-full bg-black bg-opacity-50 flex justify-center items-center z-50">
                <div className="mx-auto w-popup-width p-popup-padding bg-primary-grey fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 text-white">
                <button className="absolute -top-3 -right-3" onClick={()=>onClose()}>
                    <img src={iconClose} />
                </button>
                    <p className='text-title-16 font-semibold'>
                        {title}
                    </p>

                    {children}
                </div>
            </div>
        </>
    )
}