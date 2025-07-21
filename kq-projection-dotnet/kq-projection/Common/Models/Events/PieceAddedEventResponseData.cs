using KQ_Projection.Common.Models.Jigsaw;
using System.Text.Json.Serialization;

namespace KQ_Projection.Common.Models.Events
{
    public class PieceAddedEventResponseData : ResponseData
    {
        [JsonPropertyName("pieceId")]
        public Guid pieceId { get; set; }

        [JsonPropertyName("movedRowPosition")]
        public int movedRowPosition { get; set; }

        [JsonPropertyName("movedColumnPosition")]
        public int movedColumnPosition { get; set; }    

        [JsonPropertyName("hasGameEnded")]
        public bool hasGameEnded { get; set; }

        [JsonPropertyName("scoreCard")]
        public TransportScoreCard scoreCard { get; set; }
    }
}
