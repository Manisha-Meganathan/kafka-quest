using System.Text.Json.Serialization;

namespace KQ_Projection.Common.Models.Events
{
    public class SaveStateResponseData : ResponseData
    {
        [JsonPropertyName("toEventStreamId")]
        public int toEventStreamId { get; set; }
    }
}
