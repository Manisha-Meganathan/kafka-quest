
using System.Text.Json.Serialization;

namespace KQ_Projection.Common.Models.Events
{
    [JsonDerivedType(typeof(GameStartedEventResponseData))]
    [JsonDerivedType(typeof(PieceAddedEventResponseData))]
    [JsonDerivedType(typeof(PieceRemovedEventResponseData))]
    [JsonDerivedType(typeof(ExceptionalEventResponseData))]
    [JsonDerivedType(typeof(GameDiscardEventResponseData))]
    public abstract class ResponseData
    {
    }
}
