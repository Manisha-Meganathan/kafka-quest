import { useEffect, useState } from "react";
import { CommonTypes } from "../../../types/common"
import { GameUtils } from "../../../utils/gameUtils"
import DropTarget from "./DropTarget"

function Grid({ horizontalRows, verticalColumns, addedPieces }: CommonTypes.Grid) {

  const [panelAndGridAreaWidth, setPanelAndGridAreaWidth] = useState((74 / 100) * window.innerWidth);
  const idsArray = GameUtils.generateGridIds(horizontalRows, verticalColumns)
  const gridItemSize =  (panelAndGridAreaWidth/2) / Math.sqrt(verticalColumns * horizontalRows)
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
      <div className="bg-primary-grey border-[#44444F] gap-0" style={{columns: `${verticalColumns}`, width: `${panelAndGridAreaWidth/2}px`, height: `${panelAndGridAreaWidth/2}px`}}>
        {
          idsArray.map((idObject) => {
            let existingImage = addedPieces.find((piece) => piece.columnPosition === idObject.column && piece.rowPosition === idObject.row)

            return <DropTarget
              key={`${idObject.row}-${idObject.column}`}
              rowPosition={idObject.row!!}
              columnPosition={idObject.column!!}
              image={existingImage?.piece ? existingImage.piece : null}
              size={gridItemSize}
            />
          })
        }
      </div>
    </>
  )
}

export default Grid;