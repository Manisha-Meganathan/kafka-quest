using KQ_Projection;
using KQ_Projection.Kafka;

Console.WriteLine("Starting the app..");

GlobalExceptionHandler.InitHandler();
KafkaServices.Start();