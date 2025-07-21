import type { CommonTypes } from '../../types/common';


function Button({icon, text, bgColor, onClick, isEnabled}: CommonTypes.CustomBtn) {
  const btnStyle: CommonTypes.ButtonStyles = {
    default: "bg-primary-grey hover:bg-slate-700 text-white py-2 px-4 flex items-center justify-between mx-4 border-[1px] rounded-md border-primary-border",
    primaryBlue: "bg-primary-blue hover:bg-blue-600 text-white py-2 px-4 flex items-center justify-between mx-4 border-[1px] rounded-md border-primary-border",
    primaryGray: "bg-primary-gray text-white py-2 px-4 flex items-center justify-between mx-4 border-[1px] rounded-md border-primary-border"
  }
  
  return (
   <button 
      className={`${btnStyle[bgColor as keyof CommonTypes.ButtonStyles]} ...`}
      onClick={onClick}
    >
    <span className="pr-2"><img className="h-auto max-w-full" src={icon} /></span>
    <span className="text-sm b">{text}</span>
   </button>
  )
}

export function ButtonWithMx({icon, text, bgColor, onClick, marginX, isGameStarted}: {icon: string, text: string, bgColor: string, onClick: ()=>void, marginX: number, isGameStarted: boolean}) {
  const btnStyle: CommonTypes.ButtonStyles = {
    default: "bg-primary-grey hover:bg-slate-700 text-white py-2 px-4 flex items-center justify-between border-[1px] rounded-md border-primary-border",
    primaryBlue: "bg-primary-blue hover:bg-blue-600 text-white py-2 px-4 flex items-center justify-between border-[1px] rounded-md border-primary-border",
    primaryGray: "bg-primary-gray text-white py-2 px-4 flex items-center justify-between border-[1px] rounded-md border-primary-border"
  }
  
  return (
   <button 
      
      className={`${btnStyle[bgColor as keyof CommonTypes.ButtonStyles]} ${text === 'Start' && isGameStarted ? 'cursor-not-allowed' : 'cursor-allowed'} ...`}
      onClick={onClick}
      style={{marginLeft: `${marginX}px`, marginRight: `${marginX}px`}}
    >
    <span className="pr-2"><img className="h-auto max-w-full" src={icon} /></span>
    <span className="text-sm b">{text}</span>
   </button>
  )
}

export default Button;