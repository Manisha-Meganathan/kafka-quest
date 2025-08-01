namespace KQ_Projection.Common.Enums;

using System.Runtime.Serialization;
using System.Text.Json.Serialization;

[JsonConverter(typeof(JsonStringEnumConverter))]
public enum Grade
{
    [EnumMember(Value = "BAD")]
    BAD,

    [EnumMember(Value = "GOOD")]
    GOOD,

    [EnumMember(Value = "EXCELLENT")]
    EXCELLENT
}
