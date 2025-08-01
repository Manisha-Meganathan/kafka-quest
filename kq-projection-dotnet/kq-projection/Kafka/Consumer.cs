using Confluent.Kafka;
using KQ_Projection.Common.Models.Keys;
using KQ_Projection.Common.SerDes;
using KQ_Projection.Services.EventHandling;

namespace KQ_Projection.Kafka;

public class Consumer
{
    public static void RunConsumer(IEnumerable<KeyValuePair<string, string>> configs, string topicRegex)
    {
        using var consumer = new ConsumerBuilder<AggregateKey, byte[]>(configs)
            .SetKeyDeserializer(new CustomDeserializer<AggregateKey>())
            .SetValueDeserializer(Deserializers.ByteArray)
            .SetErrorHandler((_, e) => Console.WriteLine($"Error: {e.Reason}"))
            .SetStatisticsHandler((_, json) => Console.WriteLine($"Statistics: {json}"))
            .SetPartitionsAssignedHandler((c, partitions) =>
            {
                Console.WriteLine(
                    "Partitions incrementally assigned: [" +
                    string.Join(',', partitions.Select(p => p.Partition.Value)) +
                    "], all: [" +
                    string.Join(',', c.Assignment.Concat(partitions).Select(p => p.Partition.Value)) +
                    "]");
            })
            .SetPartitionsRevokedHandler((c, partitions) =>
            {
                var remaining = c.Assignment.Where(atp => partitions.Where(rtp => rtp.TopicPartition == atp).Count() == 0);
                Console.WriteLine(
                    "Partitions incrementally revoked: [" +
                    string.Join(',', partitions.Select(p => p.Partition.Value)) +
                    "], remaining: [" +
                    string.Join(',', remaining.Select(p => p.Partition.Value)) +
                    "]");
            })
            .SetPartitionsLostHandler((c, partitions) =>
            {
                Console.WriteLine($"Partitions were lost: [{string.Join(", ", partitions)}]");
            })
            .Build();

        try
        {
            consumer.Subscribe(topicRegex);
            Console.WriteLine($"Consumer: Subscribed to topics: {topicRegex}, listening for events..");

            while (true)
            {
                try
                {
                    var cts = new CancellationTokenSource();
                    Console.CancelKeyPress += (_, args) =>
                    {
                        args.Cancel = true;
                        cts.Cancel();
                    };

                    var consumeResult = consumer.Consume(cts.Token);

                    if (consumeResult.IsPartitionEOF)
                    {
                        Console.WriteLine(
                            $"Reached end of topic {consumeResult.Topic}, partition {consumeResult.Partition}, offset {consumeResult.Offset}.");

                        continue;
                    }

                    Console.WriteLine($"Received message at {consumeResult.TopicPartitionOffset}: {consumeResult.Message.Key.eventType}");
                    var projectionResMessage = EventProcessor.process(consumeResult.Message.Value, consumeResult.Message.Key);
                    Producer.PublishEvent(projectionResMessage);
                }
                catch (ConsumeException e)
                {
                    Console.WriteLine($"Consume error: {e.Error.Reason}");
                }
            }
        }
        catch (OperationCanceledException)
        {
            Console.WriteLine("Closing consumer.");
            consumer.Close();
        }
    }
}