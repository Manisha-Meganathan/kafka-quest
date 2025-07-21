namespace KQ_Projection.Common.SerDes.JsonConverters;

using KQ_Projection.Common.Models.Events;
using System.Text.Json;
using System.Text.Json.Serialization;

public class CustomEventDataConverter : JsonConverter<EventData>
{
    private readonly Type targetEventType;

    public CustomEventDataConverter(Type targetEventType)
    {
        this.targetEventType = targetEventType ?? throw new ArgumentNullException(nameof(targetEventType));
    }

    public override EventData Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
    {
        return (EventData)JsonSerializer.Deserialize(ref reader, targetEventType, options);
    }

    public override void Write(Utf8JsonWriter writer, EventData value, JsonSerializerOptions options)
    {
        JsonSerializer.Serialize(writer, value, value.GetType(), options);
    }
}
