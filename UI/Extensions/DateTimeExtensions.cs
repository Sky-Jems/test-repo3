using System;

namespace pos.Extensions;

public static class DateTimeOffsetExtensions
{
    public static string ToISO8601(this DateTimeOffset dt)
    {
        return dt.ToString("yyyy-MM-ddTHH:mm:ssK");
    }

    public static DateTimeOffset DayStart(this DateTimeOffset dt)
    {
        return dt.Date;
    }

    public static DateTimeOffset DayEnd(this DateTimeOffset dt)
    {
        return dt.Date.AddDays(1).AddTicks(-1);
    }
}