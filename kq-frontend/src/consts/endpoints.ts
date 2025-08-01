import { CommonTypes } from "../types/common"

const subscriptionDestinationPrefix : string = "/game-session/"
const publishDestinationPrefix : string = "/game/"
const eventResponse : string = "/track"
const notificationResponse : string = "/notification"
const apiEndPoint = process.env.REACT_APP_API_ENDPOINT
const login : string = "/player/login"
const signUp : string = "/player/register"

export const eventDefaultEndPoint = publishDestinationPrefix + "event"

export const gameStartEventEndPoint = eventDefaultEndPoint + "/start-game"

export const sendEmailRequestEndPoint = apiEndPoint + "/email/send-demo-request"

export const triggerUndeliveredResponsesDeliveryEndPoint = publishDestinationPrefix + "trigger-undelivered-response-delivery"

export function getSubscriptionForUndeliveredResponsesProcessTracking(trackingId : string) : string {
    return subscriptionDestinationPrefix + trackingId + notificationResponse
}
export function getSubscriptionForEventResponses(trackingId : string) : string {
    return subscriptionDestinationPrefix + trackingId + eventResponse
}

export function getApiEndPoint(callType : CommonTypes.ApiCallType) : string {
    switch (callType) {
        case CommonTypes.ApiCallType.LOGIN:
            return apiEndPoint + login
        case CommonTypes.ApiCallType.REGISTER:
            return apiEndPoint + signUp
        default:
            return ""
    }
}