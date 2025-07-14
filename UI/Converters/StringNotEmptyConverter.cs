using System;
using System.Globalization;
using Avalonia.Data.Converters;

namespace Pos.Converters;

public class StringNotEmptyConverter : IValueConverter
{
    public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
    {
        Console.WriteLine($"[StringNotEmptyConverter] Value: '{value}{!string.IsNullOrWhiteSpace(value?.ToString())}'");
        return !string.IsNullOrWhiteSpace(value?.ToString());
    }

    public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        => throw new NotImplementedException();
}
