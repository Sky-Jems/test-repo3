using System;
using System.Globalization;
using Avalonia.Data.Converters;

namespace Pos.Converters;

public class DateTimeConverter : IValueConverter
{
    public object? Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        string dateTimeFormat = (parameter as string) ?? "MMM d, yyyy";

        if (value is DateTimeOffset date)
            return date.ToString(dateTimeFormat);
        if (value is string stringDate)
            return DateTime.Parse(stringDate).ToString(dateTimeFormat);
        return value;
    }

    public object? ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        throw new NotImplementedException();
    }
}
