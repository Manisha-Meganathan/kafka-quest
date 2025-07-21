using Microsoft.Extensions.Configuration;
using System.Collections.ObjectModel;

namespace KQ_Projection.Configs

{
    public class ConfigHelper
    {
        private static IEnumerable<KeyValuePair<string, string>> GetCommonKafkaConfigs()
        {
            return new Collection<KeyValuePair<string, string>>
            {
                getEnvKeyValuePair("bootstrap.servers"),
                getEnvKeyValuePair("client.id"),
                getEnvKeyValuePair("allow.auto.create.topics")
            }.AsEnumerable();
        }

        private static IEnumerable<KeyValuePair<string, string>> GetConfigsFromJson()
        {
            var binDir = Directory.GetCurrentDirectory();
            var workDir = Directory.GetParent(binDir).Parent.Parent.ToString();

            var builder = new ConfigurationBuilder()
                .SetBasePath($"{workDir}/Configs/")
                .AddJsonFile("KafkaConfigs.json", optional: true, reloadOnChange: true);
            IConfigurationRoot config = builder.Build();

            return config.AsEnumerable();
        }

        private static KeyValuePair<string, string> getEnvKeyValuePair(string key)
        {
            string? returnedValue = Environment.GetEnvironmentVariable(key);
            return new KeyValuePair<string, string>(key, returnedValue.ToString());
        }

        public static IEnumerable<KeyValuePair<string, string>> GetKafkaConsumerConfigs()
        {
            if (IsEnvVarsAvailable())
            {
                return GetCommonKafkaConfigs()
                    .Append(getEnvKeyValuePair("max.partition.fetch.bytes"))
                    .Append(getEnvKeyValuePair("group.id"))
                    .Append(getEnvKeyValuePair("message.max.bytes"));
            }
            return GetConfigsFromJson();
        }

        public static IEnumerable<KeyValuePair<string, string>> GetKafkaProducerConfigs()
        {
            if (IsEnvVarsAvailable())
            {
                return GetCommonKafkaConfigs().
                    Append(getEnvKeyValuePair("message.max.bytes")); ;
            }

            return GetConfigsFromJson();
        }

        private static bool IsEnvVarsAvailable()
        {
            return Environment.GetEnvironmentVariable("bootstrap.servers") != null;
        }

        public static string GetSourceTopicRegex() {
            string sourceTopicRgx = Environment.GetEnvironmentVariable("source_topic_regex");
            if (sourceTopicRgx != null) {
                return "^" + sourceTopicRgx;
            }
            return KafkaAdditionalConfigs.sourceTopicRegex;
        }

        public static string GetSinkTopic() {
            string sinkTopic = Environment.GetEnvironmentVariable("");
            if (sinkTopic != null)
            {
                return sinkTopic;
            }
            return KafkaAdditionalConfigs.sinkTopic;
        }

        public static string GetBootstrapServer() {
            string server = Environment.GetEnvironmentVariable("bootstrap.servers");
            if (server != null)
            {
                return server;
            }
            return KafkaAdditionalConfigs.bootstrapServer;
        }
    }
}