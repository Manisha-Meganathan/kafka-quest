using System.Text.Json.Serialization;

namespace KQ_Projection.Common.Models.Events
{
    public class SaveStateEventData : EventData
    {
        [JsonPropertyName("toEventStreamId")]
        public int toEventStreamId { get; set; }
    }
}
