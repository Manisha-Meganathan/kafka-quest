using System.Text.Json.Serialization;

namespace KQ_Projection.Common.Models.Events
{
    public class JigsawPieceCurrentPositionResponse
    {
        [JsonPropertyName("pieceId")]
        public Guid pieceId { get; set; }

        [JsonPropertyName("rowPosition")]
        public int rowPosition { get; set; }

        [JsonPropertyName("columnPosition")]
        public int columnPosition { get; set; }
    }
}
