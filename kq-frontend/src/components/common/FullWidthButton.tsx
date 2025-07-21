import { CommonTypes } from "../../types/common"

export default function FullWidthButton({buttonText, onclick}: CommonTypes.FullWidthPrimaryButton) {
    const btnStyle: CommonTypes.ButtonStyles = {
      default: "bg-primary-grey hover:bg-slate-700 text-white py-2 px-4 flex items-center justify-between mx-4 border-[1px] rounded-md border-primary-border",
      primaryBlue: "bg-primary-blue hover:bg-blue-600 text-white py-2 px-4 flex items-center justify-between mx-4 border-[1px] rounded-md border-primary-border",
      primaryGray: "bg-primary-gray hover:bg-blue-600 text-white py-2 px-4 flex items-center justify-between mx-4 border-[1px] rounded-md border-primary-border"
    }
    
    return (
     <button 
        className="bg-primary-blue w-full rounded-default-border-radius p-button-padding-top 
        my-default-margin h-auto font-sans text-title-16 font-medium"
        onClick={onclick}
      >
      <span className="text-sm b">{buttonText}</span>
     </button>
    )
  }