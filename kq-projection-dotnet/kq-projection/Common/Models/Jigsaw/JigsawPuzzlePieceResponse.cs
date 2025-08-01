using System.Text.Json.Serialization;

namespace KQ_Projection.Common.Models.Jigsaw
{
    public class JigsawPuzzlePieceResponse
    {
        [JsonPropertyName("pieceId")]
        public Guid pieceId { get; set; }

        [JsonPropertyName("pieceData")]
        public string pieceData { get; set; }
    }
}
