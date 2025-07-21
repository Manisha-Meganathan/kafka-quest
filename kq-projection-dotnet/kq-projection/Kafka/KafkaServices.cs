using Confluent.Kafka;
using KQ_Projection.Configs;

namespace KQ_Projection.Kafka;

public class KafkaServices
{
    public static void Start() {

        string sourceTopicRegex = ConfigHelper.GetSourceTopicRegex();
        string sinkTopic = ConfigHelper.GetSinkTopic();
        IAdminClient? adminClient = null;

        IEnumerable<KeyValuePair<string, string>> consumerConfigs = ConfigHelper.GetKafkaConsumerConfigs();
        IEnumerable<KeyValuePair<string, string>> producerConfigs = ConfigHelper.GetKafkaProducerConfigs();

        bool isConnected = false;
        do
        {
            try
            {
                adminClient = new AdminClientBuilder(new AdminClientConfig { BootstrapServers = ConfigHelper.GetBootstrapServer()}).Build();
                var res = adminClient.GetMetadata(TimeSpan.FromSeconds(5));
                if (!res.OriginatingBrokerName.Equals("")) {
                    Console.WriteLine("Admin client: connected, proceeding to next..");
                    isConnected = true;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Admin Client: Connection failed, retrying in 5 seconds..");
                Console.WriteLine(ex.Message);
                Thread.Sleep(5000);
                continue;
            }
        } while (!isConnected);

        try
        {
            var isBrokersReady = false;

            do
            {
                var metadata = adminClient?.GetMetadata(TimeSpan.FromSeconds(30));

                if (metadata != null && metadata.Topics.Any(metadata => metadata.Topic.StartsWith(sourceTopicRegex.Replace("^", ""))))
                {
                    Console.WriteLine($"Topics created and brokers are ready, starting producer and consumer..");
                    isBrokersReady = true;
                    Producer.InitProducer(sinkTopic, producerConfigs);
                    Consumer.RunConsumer(consumerConfigs, sourceTopicRegex);
                }
                else
                {
                    Console.WriteLine($"No topics found with the following pattern: {sourceTopicRegex}, retrying in 5 seceonds..");
                    Thread.Sleep(5000);
                }

            } while (!isBrokersReady);


        }
        catch (Exception ex)
        {
            Console.WriteLine("KafkaServices: Something went wrong: "+ ex.Message);
        }
    }
}
