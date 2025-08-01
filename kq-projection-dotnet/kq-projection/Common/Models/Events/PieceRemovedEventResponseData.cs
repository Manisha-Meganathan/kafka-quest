using System.Text.Json.Serialization;

namespace KQ_Projection.Common.Models.Events
{
    public class PieceRemovedEventResponseData : ResponseData
    {
        [JsonPropertyName("pieceId")]
        public Guid pieceId { get; set; }

        [JsonPropertyName("removedRowPosition")]
        public int removedRowPosition { get; set; }


        [JsonPropertyName("removedColumnPosition")]
        public int removedColumnPosition { get; set; }
    }
}
