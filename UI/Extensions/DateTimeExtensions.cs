using System;

namespace pos.Extensions;

public static class DateTimeOffsetExtensions
{
    public static string ToISO8601(this DateTimeOffset? dt)
    {
        if (dt.HasValue)
            return dt.Value.ToString("yyyy-MM-ddTHH:mm:ssK");
        return "";
    }

    public static string ToISO8601(this DateTimeOffset dt)
    {
        return dt.ToString("yyyy-MM-ddTHH:mm:ssK");
    }

    public static DateTimeOffset DayStart(this DateTimeOffset? dt)
    {
        return dt.Value.Date;
    }

    public static DateTimeOffset DayEnd(this DateTimeOffset? dt)
    {
        return dt.Value.Date.AddDays(1).AddTicks(-1);
    }
}