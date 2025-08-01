namespace KQ_Projection.ExtensionMethods
{
    public static class DateTimeExtensions
    {
        public static long ToLong(this DateTime dateTime)
        {
            DateTimeOffset dateTimeOffset = DateTimeOffset.UtcNow;
            return dateTimeOffset.ToUnixTimeMilliseconds();
        }
        public static DateTime FromLong(this DateTime dateTime, long epochMilli)
        {
            DateTimeOffset dateTimeOffset = DateTimeOffset.FromUnixTimeMilliseconds(epochMilli).ToLocalTime();
            return dateTimeOffset.UtcDateTime;
        }
    }
}
