namespace KQ_Projection.Common.Enums;

using System.Runtime.Serialization;
using System.Text.Json.Serialization;

[JsonConverter(typeof(JsonStringEnumConverter))]
public enum EventType
{
    [EnumMember(Value = "GAME_STARTED_EVENT")]
    GAME_STARTED_EVENT = 0,

    [EnumMember(Value = "PIECE_ADDED_EVENT")]
    PIECE_ADDED_EVENT = 1,

    [EnumMember(Value = "PIECE_REMOVED_EVENT")]
    PIECE_REMOVED_EVENT = 2,

    [EnumMember(Value = "REVERT_MOVES_EVENT")]
    REVERT_MOVES_EVENT = 3,

    [EnumMember(Value = "SAVE_CURRENT_STATE_EVENT")]
    SAVE_CURRENT_STATE_EVENT = 4,

    [EnumMember(Value = "STARTED_FROM_SAVED_STATE_EVENT")]
    STARTED_FROM_SAVED_STATE_EVENT = 5,

    [EnumMember(Value = "GAME_PAUSED_EVENT")]
    GAME_PAUSED_EVENT = 6,

    [EnumMember(Value = "GAME_RESUMED_EVENT")]
    GAME_RESUMED_EVENT = 7,

    [EnumMember(Value = "EXCEPTIONAL_EVENT")]
    EXCEPTIONAL_EVENT = 8,

    [EnumMember(Value = "DISCARD_GAME_EVENT")]
    DISCARD_GAME_EVENT = 9
}