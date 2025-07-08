using System;
using System.Globalization;
using Avalonia;
using Avalonia.Data.Converters;

namespace Pos.Converters;

public class StringEqualsConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        return string.Equals(value?.ToString(), parameter?.ToString(), StringComparison.OrdinalIgnoreCase);
    }

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        if (value is bool isChecked && isChecked && parameter is string str)
        {
            return str;
        }
        return AvaloniaProperty.UnsetValue;
    }
}
