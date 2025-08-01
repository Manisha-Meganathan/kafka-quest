namespace KQ_Projection.Common.Enums;

using System.Runtime.Serialization;
using System.Text.Json.Serialization;

[JsonConverter(typeof(JsonStringEnumConverter))]
public enum EventInitiatedBy
{
    [EnumMember(Value = "FRONT_END")]
    FRONT_END = 0,

    [EnumMember(Value = "API")]
    API = 1,

    [EnumMember(Value = "AGGREGATE")]
    AGGREGATE = 2,

    [EnumMember(Value = "PROJECTION")]
    PROJECTION = 3,
}
