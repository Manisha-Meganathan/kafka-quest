import { useEffect, useState } from "react";
import DraggablePiece from "./DraggablePiece";

export default function PiecePanel({ draggablePieces, gameSize }: any) {
  const [panelAndGridAreaWidth, setPanelAndGridAreaWidth] = useState((74 / 100) * window.innerWidth);
  const sideLengthOfPiece = (panelAndGridAreaWidth/2) / Math.sqrt(gameSize);
  useEffect(() => {
    const handleResize = () => {
      setPanelAndGridAreaWidth((74 / 100) * window.innerWidth);
    };
    window.addEventListener('resize', handleResize);
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);

  return (
    <>
      <div className="border-[#44444F] border-[1px] bg-[#2B2B36]" style={{width: `${panelAndGridAreaWidth/2}px`, height: `${panelAndGridAreaWidth/2}px`}}>
      <div className="w-full h-full grid grid-cols-2 gap-4 place-items-center">
        {draggablePieces.map((piece: any, index: any) => (
            <DraggablePiece key={piece.pieceId} size={sideLengthOfPiece} pieceId={piece.pieceId.toString()} pieceData={piece.pieceData} />
        ))}
        </div>
      </div>
    </>
  )
}