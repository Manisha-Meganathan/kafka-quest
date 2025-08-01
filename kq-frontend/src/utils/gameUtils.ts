import { UUID } from "crypto"
import { variableTexts } from "../consts/variableTexts"
import { EventTypes } from "../types/events"
import { replace, upperFirst } from "lodash"

export namespace GameUtils {

  export const getShuffledPieces = (availablePieces: EventTypes.JigsawPuzzlePiece[]): EventTypes.JigsawPuzzlePiece[] => {
    var shuffledList: EventTypes.JigsawPuzzlePiece[] = []

    var indexArr = getNumberOfRandomIndexes(variableTexts.numberOfPiecesToShow, availablePieces.length)
    indexArr.forEach(index => {
      console.log("Shuffled index: " + index)
      shuffledList.push(availablePieces[index])
    })
    return shuffledList
  }

  const getNumberOfRandomIndexes = (n: number, arrLength: number): number[] => {
    var indexArray: number[] = []

    do {
      const randomIndex = Math.floor(Math.random() * arrLength)
      if (!indexArray.includes(randomIndex)) {
        indexArray.push(randomIndex)
      }
      if (indexArray.length === arrLength) {
        console.log(`Shuffled ${indexArray.length} of items, no more items to shuffle, returning..`)
        break
      }
    } while (indexArray.length < n)

    return indexArray
  }

  export const removePieceFromArray = (pieceId: UUID, targetArray: EventTypes.JigsawPuzzlePiece[]): EventTypes.JigsawPuzzlePiece[] => {
    console.log("Array to remove from: " + targetArray)
    return targetArray.filter((el) => el.pieceId !== pieceId);
  }

  type idObject = {
    column: number | undefined,
    row: number | undefined
  }

  export const generateGridIds = (horizontalRows: number, verticalColumns: number): idObject[] => {
    let idsArray: idObject[] = []

    for (let i = 0; i < horizontalRows; i++) {
      for (let k = 0; k < verticalColumns; k++) {
        idsArray.push({ column: i, row: k })
      }
    }
    return idsArray
  }

  export const durationFromLongMillis = (longMillis: number): string => {
    return new Date(longMillis).toISOString().slice(11, 19)
  }

  export const localDateTimeFromUnixStamp = (unixStamp: number): string => {
    return new Date(unixStamp).toLocaleTimeString()
  }

  export const formatString = (text: string) : string => {
    return replace(upperFirst(text.toLowerCase()), "_", " ")
  }

  export const eventFormatString = (text: string): string => {
    return text.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase());
  };

  export function GenerateRandomUsername(length:number) {
    const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    let result = "";
    for (let i = 0; i < length; i++) {
      result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return result;
  };
  

}