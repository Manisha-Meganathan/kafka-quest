using System.Runtime.Serialization;
using System.Text.Json.Serialization;

namespace KQ_Projection.Common.Enums;

[JsonConverter(typeof(JsonStringEnumConverter))]
public enum ResponseMessage
{
    [EnumMember(Value = "SUCCESS")]
    SUCCESS,

    [EnumMember(Value = "FAILURE")]
    FAILURE
}