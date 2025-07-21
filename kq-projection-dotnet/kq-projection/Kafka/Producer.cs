using Confluent.Kafka;
using KQ_Projection.Common.Models.Events;
using KQ_Projection.Common.Models.Keys;
using KQ_Projection.Common.SerDes;

namespace KQ_Projection.Kafka;

public class Producer
{
    private static bool isInitialized = false;
    private static IProducer<ProjectionKey, ProjectionEventResponse>? producer;
    private static string topic;
    public static void InitProducer(String sinkTopic, IEnumerable<KeyValuePair<string, string>> configuration)
    {
        producer = new ProducerBuilder<ProjectionKey, ProjectionEventResponse>(configuration)
            .SetKeySerializer(new CustomSerializer<ProjectionKey>())
            .SetValueSerializer(new CustomSerializer<ProjectionEventResponse>())
            .Build();
            Console.WriteLine("Producer initialized, ready to publish..");
        topic = sinkTopic;
        isInitialized = true;
    }

    public static void PublishEvent(Message<ProjectionKey, ProjectionEventResponse> message)
    {
        Console.WriteLine("Publishing event..");
        if (isInitialized)
        {
            producer!.Produce(topic, message, OnDeliveryReport);
            producer!.Flush(TimeSpan.FromSeconds(5));
            return;
        }
        Console.WriteLine("Producer is not initialized, please initialize first");
    }

    private static void OnDeliveryReport(DeliveryReport<ProjectionKey, ProjectionEventResponse> deliveryReport)
    {
        if (deliveryReport.Error.Code == ErrorCode.NoError)
        {
            Console.WriteLine($"Message publsihed: Topic -> {topic}, PlayerId: {deliveryReport.Message.Key.playerId}");
            Console.WriteLine($"Response: {deliveryReport.Message.Value.responseMessage}");
            return;
        }
        Console.WriteLine("Error publishing message!");
        Console.WriteLine(deliveryReport.Error.Reason);
    }
}