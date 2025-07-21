import eventIcon from "../../../assets/icons/svg/events.svg";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../../stateManagement/store";
import icon_timer from '../../../assets/icons/svg/icon_timer.svg'
import PopUpCommon from "../../common/PopUpCommon";
import FullWidthButton from "../../common/FullWidthButton";
import { GameEvent, setRevertCount } from "../../../stateManagement/gameStates";
import { GameUtils } from "../../../utils/gameUtils";
import useWebSocket from "../../../hooks/useWebSocket";
import useEventProducer from "../../../hooks/useEventProducer";
import CustomDropDown from "../../common/CustomDropdown";
import HowToPlay from "../howToPlay/HowToPlay";
import icon_info from '../../../assets/icons/svg/info.svg'
import { toggleHowToPlay } from "../../../stateManagement/uiStates";
import icon_close from '../../../assets/icons/svg/icon_close.svg'

enum ShortOrder {
  OLDEST = "Oldest",
  NEWEST = "Newest"
}

function EventSideDock() {
  const showHowToPlay = useSelector((state: RootState) => state.ui.shouldShowHowToPlay)
  const eventList = useSelector((state: RootState) => state.game.eventsList)
  const [shortOrder, setShortOrder] = useState(ShortOrder.OLDEST)
  const [shortedEventList, setShortedEventList] = useState([...eventList])
  const dispatchAction = useDispatch()

  useEffect(() => {
    if (shortOrder === ShortOrder.NEWEST) {
      setShortedEventList([...eventList].reverse())
    } else {
      setShortedEventList([...eventList])
    }
  }, [shortOrder, eventList])

  return (
    <>
      <div className="fixed w-[22%] h-full bg-grey-light pt-4 px-4 overflow-y custom-scrollbar">
        {showHowToPlay ?
          <>
            <TopBar title="How to play" icon={icon_info} onClose={() => dispatchAction(toggleHowToPlay())} />
            <HowToPlay />
          </> :
          <>
            <TopBar title="Events" icon={eventIcon} />
            <CustomDropDown title={`Sort`} defaultOption={`${shortOrder}`} options={Object.assign(ShortOrder)} onChange={(selectedItem) => {
              setShortOrder(selectedItem as unknown as ShortOrder)
            }} />
            {eventList && shortedEventList.map(event =>
              <EventItem
                key={event.eventNumber}
                eventNumber={event.eventNumber}
                eventType={event.eventType}
                response={event.response}
                timeStamp={event.timeStamp}
                eventStreamId={event.eventStreamId}
                revertFromEventStreamId={event.revertFromEventStreamId}
              />
            )}
          </>}
      </div>
    </>
  )
}

function TopBar({ title, icon, onClose }: { title: string, icon: string, onClose?: () => void }) {
  return (
    <>
      <div className="flex justify-between items-center pb-2 border-b-2 border-primary-border mb-5 flex-col lg:flex-row">
        <div className="flex mb-2">
          <img src={icon} />
          <p className="ps-2 text-md">{title}</p>
        </div>

        {onClose && <img src={icon_close} onClick={onClose} className=" cursor-pointer" />}
      </div>
    </>
  )
}


function EventItem({ eventNumber, eventType, timeStamp, response, eventStreamId, revertFromEventStreamId }: GameEvent) {
  const [showPopup, togglePopup] = useState(false)
  const formattedEvent = GameUtils.eventFormatString(eventType)
  const formattedResponse = GameUtils.formatString(response)
  return (
    <>
      {revertFromEventStreamId != null ?
        <>
          <div className="bg-[#26262E] border-[1px] border-[#274DD4] rounded p-2 cursor-pointer mb-2 transition-all hover:bg-primary-blue hover:border-primary-blue"
            onClick={() => togglePopup(!showPopup)}>
            <div className="flex justify-between text-title-16 flex-col 2xl:flex-row static 2xl:relative">
              <div className="2xl:z-10">
                <p>{`${eventNumber}. ${formattedEvent}`}</p>
                <p className="text-sm text-white opacity-50">{formattedResponse}</p>
                <p className=" text-sm text-white opacity-50">{`Reverted Event: Originally From Stream ID: ${revertFromEventStreamId}`}</p>
              </div>
              <div className="flex flex-row justify-between items-center self-start top-0 2xl:z-20 2xl:-ml-28">
                <div className="xl:self-start">
                  <img className="inline  mb-1 mr-0.5 pt-1" src={icon_timer} alt="timer" />
                </div>
                <div>
                  <p className="inline">{GameUtils.localDateTimeFromUnixStamp(timeStamp)}</p>
                </div>
              </div>
            </div>
          </div>
        </> :
        <>
          <div className="bg-primary-grey border-[1px] border-light-gray rounded p-2 cursor-pointer mb-2 transition-all hover:bg-primary-blue hover:border-primary-blue"
            onClick={() => togglePopup(!showPopup)}>
            <div className="flex justify-between text-title-16 flex-col 2xl:flex-row">
              <div>
                <p>{`${eventNumber}. ${formattedEvent}`}</p>
                <p className=" text-sm text-white opacity-50">{formattedResponse}</p>
              </div>
              <div className="flex flex-row justify-between items-center self-start">
                <img className="inline mr-0.5 pt-1" src={icon_timer} alt="timer" />
                <p className="inline"> {GameUtils.localDateTimeFromUnixStamp(timeStamp)}</p>
              </div>
            </div>
          </div>
        </>
      }
      {showPopup && <RevertGamePopUp
        eventNumber={eventNumber}
        eventType={formattedEvent}
        eventStreamId={eventStreamId}
        onClose={() => togglePopup(!showPopup)}
      />}
    </>
  )
}

interface RevertGamePopUp {
  eventNumber: number
  eventType: string
  eventStreamId: number
  onClose: () => void
}
function RevertGamePopUp({ eventNumber, eventType, eventStreamId, onClose }: RevertGamePopUp) {
  const gameState = useSelector((state: RootState) => state.game)
  const dispatchAction = useDispatch()
  const { publishEvent } = useWebSocket()
  const { produceRevertMoveEvent } = useEventProducer()

  return (
    <PopUpCommon title="Event Details" onClose={onClose}>
      <div className="flex flex-row justify-between items-center my-2">
        <p className="text-title-16 font-normal text-white opacity-50">Event Number</p>
        <p>{eventNumber}</p>
      </div>
      <div className="flex flex-row justify-between items-center my-2">
        <p className="text-title-16 font-normal text-white opacity-50">Event Type</p>
        <p>{eventType}</p>
      </div>

      <FullWidthButton
        buttonText="Revert to this event"
        onclick={() => {
          dispatchAction(setRevertCount(eventNumber))
          publishEvent(produceRevertMoveEvent(eventStreamId, gameState.gameId!!, gameState.player.playerId!!))
          onClose()
        }}
      />
    </PopUpCommon>
  )
}

export default EventSideDock;