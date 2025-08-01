namespace KQ_Projection.Common.SerDes;

using KQ_Projection.Common.SerDes.JsonConverters;
using System.Text.Json;

public static class CustomDeserializerWithConcreteType<T>
{
    public static T Deserialize(byte[] eventAsBytes, Type eventType)
    {
        try
        {
            JsonSerializerOptions options = new JsonSerializerOptions
            {
                Converters = { new CustomEventDataConverter(eventType) },
                PropertyNamingPolicy = JsonNamingPolicy.CamelCase,
            };
            
            return JsonSerializer.Deserialize<T>(eventAsBytes, options);
        }
        catch (NotSupportedException ex)
        {
            throw new ApplicationException("Failed to deserialize value", ex);
        }
    }
}