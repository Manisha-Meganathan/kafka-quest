import { useEffect, useRef, useState } from "react";
import { UUID } from "crypto";
import { useDrop } from "react-dnd";
import { useDispatch, useSelector } from "react-redux";
import { updatePuzzlePieces } from "../../../stateManagement/gameStates";
import useEventProducer from "../../../hooks/useEventProducer";
import useWebSocket from "../../../hooks/useWebSocket";
import FullWidthButton from "../../common/FullWidthButton";
import PopUpCommon from "../../common/PopUpCommon";
import { PieceUpdate, UpdatePieceAction } from "../../../stateManagement/gameActions";
import { RootState } from "../../../stateManagement/store";

interface DropTarget {
  rowPosition: number
  columnPosition: number
  image: {
    pieceId: UUID
    pieceData: string
  } | null
  size: number
}

export interface DropResult{
  rowNumber: number
  columnNumber: number
}

export default function DropTarget({ rowPosition, columnPosition, image, size }: DropTarget) {
  const gameState = useSelector((state:RootState)=>state.game)
  const canAcceptDrop = useRef(true)
  const dispatchAction = useDispatch()
  const {producePieceRemovedEvent} = useEventProducer()
  const {publishEvent} = useWebSocket()
  const [pieceRemovePopup, togglePopup] = useState(false)

  useEffect(()=>{
    if(image){
      canAcceptDrop.current = false
    } else {
      canAcceptDrop.current = true
    }
  },[image])

  const [, drop] = useDrop(() => ({
    accept: "imagePiece",
    canDrop: () => canAcceptDrop.current,
    drop: () => handleDrop()
  }))

  function handleDrop() : DropResult {
    return {
      rowNumber: rowPosition,
      columnNumber: columnPosition
    }
  }

  function handlePieceRemove() {
    let action: UpdatePieceAction = {
      update: PieceUpdate.REMOVE,
      piece: {
        pieceId: image?.pieceId!!,
        pieceData: image?.pieceData!!
      },
      rowPosition: rowPosition,
      columnPosition: columnPosition
    }
    dispatchAction(updatePuzzlePieces(action))
    let msg = producePieceRemovedEvent(image?.pieceId!!, columnPosition, rowPosition, gameState.gameId!!, gameState.player.playerId!!)
    publishEvent(msg)
    canAcceptDrop.current = true
    togglePopup(false)
  }

  return (
    <>
      <div className="border-[#44444F] border-[1px] bg-[#2B2B36]" ref={drop} style={{ width: `${size}px`, height: `${size}px`}} onClick={()=> image? togglePopup(true): {}}>
        {image && <img id={image.pieceId} className="w-full h-full" src={`data:image/jpeg;base64,${image.pieceData}`} alt="added piece" />}
      </div>

      {
      pieceRemovePopup &&
      <PopUpCommon title="Remove Piece" onClose={() => togglePopup(false)} >
            <div> Do you confirm to remove this piece ? </div>
            <FullWidthButton buttonText={"Remove Piece"} onclick={() => handlePieceRemove()} />
      </PopUpCommon>}
    </>
  )
}