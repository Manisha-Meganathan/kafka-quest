using System.Runtime.Serialization;
using System.Text.Json.Serialization;

namespace KQ_Projection.Common.Enums;

[JsonConverter(typeof(JsonStringEnumConverter))]
public enum DiscardReason
{
    [EnumMember(Value = "CLIENT_DISCONTINUED")]
    CLIENT_DISCONTINUED = 0
}
