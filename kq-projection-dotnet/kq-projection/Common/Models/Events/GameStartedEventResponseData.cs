using KQ_Projection.Common.Models.Jigsaw;
using System.Text.Json.Serialization;

namespace KQ_Projection.Common.Models.Events
{
    public class GameStartedEventResponseData : ResponseData
    {
        [JsonPropertyName("puzzlePieces")]
        public List<JigsawPuzzlePieceResponse> puzzlePieces { get; set; }
    }
}
