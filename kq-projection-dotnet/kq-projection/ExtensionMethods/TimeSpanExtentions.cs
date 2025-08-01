namespace KQ_Projection.ExtensionMethods;
public static class TimeSpanExtentions
{
    public static TimeSpan DurationBetweenTimes(this TimeSpan _, DateTime startTime, DateTime endTime) {
        return (endTime - startTime).Duration();
    }
}
