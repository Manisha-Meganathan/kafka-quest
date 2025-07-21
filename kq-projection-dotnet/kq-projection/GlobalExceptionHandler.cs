namespace KQ_Projection;

public class GlobalExceptionHandler
{
    public static void InitHandler()
    {
        Console.WriteLine("Registered new global exception handler");

        AppDomain.CurrentDomain.UnhandledException += new UnhandledExceptionEventHandler((Object sender, UnhandledExceptionEventArgs e) =>
        {
            var exceptionMsg = $"Something went wrong with the following: , {e.ExceptionObject}, Sender: {sender.ToString}";
            Console.WriteLine(exceptionMsg);
        });
    }
}
