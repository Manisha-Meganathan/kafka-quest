namespace KQ_Projection.Common.Enums;

using System.Runtime.Serialization;
using System.Text.Json.Serialization;

[JsonConverter(typeof(JsonStringEnumConverter))]
public enum EventStatus
{
    [EnumMember(Value = "GAME_ENDED")]
    GAME_ENDED = 0,

    [EnumMember(Value = "GAME_PAUSED")]
    GAME_PAUSED = 1,

    [EnumMember(Value = "GAME_RESUMED")]
    GAME_RESUMED = 2,

    [EnumMember(Value = "OTHER")]
    OTHER = 3
}
