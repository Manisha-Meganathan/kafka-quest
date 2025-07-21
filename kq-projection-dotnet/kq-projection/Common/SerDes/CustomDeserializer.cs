using Confluent.Kafka;
using System.Text.Json;

namespace KQ_Projection.Common.SerDes
{
    public class CustomDeserializer<T> : IDeserializer<T>
    {
        public T Deserialize(ReadOnlySpan<byte> data, bool isNull, SerializationContext context)
        {
            try
            {
                using MemoryStream ms = new MemoryStream(data.ToArray());
                return JsonSerializer.Deserialize<T>(ms);
            }
            catch (JsonException je) {
                Console.WriteLine(je.Message);
                return default(T);
            }
        }
    }
}
