using System;
using System.Globalization;
using Avalonia.Data.Converters;

namespace Pos.Converters;

public class EqualsConverter : IValueConverter
{
    public object? Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        if (value is int firstValue && parameter is string secondValue)
            return Equals(firstValue, Int32.Parse(secondValue));
        else
            return Equals(value, parameter);
    }

    public object? ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        throw new NotImplementedException();
    }
}
