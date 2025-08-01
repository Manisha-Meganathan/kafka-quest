import { useDrag } from "react-dnd";
import { CommonTypes } from "../../../types/common";
import { useEffect, useState } from "react";
import { EventTypes } from "../../../types/events";
import { DropResult } from "./DropTarget";
import { PieceUpdate, UpdatePieceAction } from "../../../stateManagement/gameActions";
import { updatePuzzlePieces } from "../../../stateManagement/gameStates";
import { useDispatch, useSelector } from "react-redux";
import useEventProducer from "../../../hooks/useEventProducer";
import useWebSocket from "../../../hooks/useWebSocket";
import { RootState } from "../../../stateManagement/store";

function DraggablePiece({ size, pieceId, pieceData }: CommonTypes.DraggablePiece) {
  const gameState = useSelector((state:RootState)=>state.game)
  const [imagePiece, setImagePiece] = useState<EventTypes.JigsawPuzzlePiece>({ pieceId: pieceId, pieceData: pieceData })
  const [isDropped, setIsDropped] = useState(false)
  const dispatchAction = useDispatch()
  const {producePieceAddedEvent} = useEventProducer()
  const {publishEvent} = useWebSocket()

  useEffect(() => {
    setImagePiece({ pieceId: pieceId, pieceData: pieceData })
    setIsDropped(false)
  }, [pieceData, pieceId])

  const [{ isDragging }, drag, dragPreview] = useDrag(() => ({
    type: "imagePiece",
    item: { pieceId: pieceId, pieceData: pieceData },
    collect: (monitor) => ({
      isDragging: monitor.isDragging(),
    }),
    end: (item, moniter) => {
      if (moniter.didDrop()) {
        let res : DropResult | null = moniter.getDropResult()
        if(res){ onDragComplete(res) }
      }
    }
  }));

  const onDragComplete = (result: DropResult) => {
    setIsDropped(true)
    
    let action: UpdatePieceAction = {
      update: PieceUpdate.ADD,
      piece: {pieceId: pieceId, pieceData: pieceData},
      rowPosition: result.rowNumber,
      columnPosition: result.columnNumber
    }
    dispatchAction(updatePuzzlePieces(action))

    let msg = producePieceAddedEvent(imagePiece, result.rowNumber, result.columnNumber, gameState.player.playerId!!, gameState.gameId!!)
    publishEvent(msg)
  }


  return (
    <>
      {isDragging ?
        <div ref={dragPreview} style={{ height: `${size}px`, width: `${size}px`, border: "solid 2px white" }} >
        </div> :
        !isDropped && <img ref={drag} style={{ height: `${size}px`, width: `${size}px` }} src={`data:image/jpeg;base64,${imagePiece.pieceData}`} alt={"dragable"} />
      }
    </>
  )
}

export default DraggablePiece;