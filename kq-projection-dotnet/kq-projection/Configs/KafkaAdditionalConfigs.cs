namespace KQ_Projection.Configs;

public static class KafkaAdditionalConfigs
{
    public static readonly string sourceTopicRegex = "^event-log";
    public static readonly string sinkTopic = "out-topic";
    public static readonly string bootstrapServer = "172.23.185.55:9093";
}