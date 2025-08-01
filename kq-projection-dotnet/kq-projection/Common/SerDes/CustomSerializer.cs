using Confluent.Kafka;
using System.Text.Json;

namespace KQ_Projection.Common.SerDes
{
    internal class CustomSerializer<T> : ISerializer<T>
    {
        public byte[] Serialize(T data, SerializationContext context)
        {
            return JsonSerializer.SerializeToUtf8Bytes<T>(data);
        }
    }
}
