using System.Text.Json.Serialization;

namespace KQ_Projection.Common.Models.Jigsaw;
public class JigsawPuzzlePiece
{
    [JsonPropertyName("pieceId")]
    public Guid pieceId { get; set; }

    [JsonPropertyName("column")]
    private int column {  get; set; }

    [JsonPropertyName("row")]
    private int row { get; set; }

    [JsonPropertyName("pieceData")]
    public byte[] pieceData { get; set; }
}
