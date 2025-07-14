using System;
using System.Globalization;
using Avalonia.Data.Converters;
using System.Collections.Generic;
using Avalonia;

namespace Pos.Converters;

public class QuantityDisplayConverter : IMultiValueConverter
{
    public object Convert(IList<object?> values, Type targetType, object? parameter, CultureInfo culture)
    {
        if (values.Count != 2)
            return "";

        if (values[0] is int quantity && values[1] is bool canModify)
        {
            return canModify ? quantity.ToString() : $"x{quantity}";
        }

        return "";
    }

    public object? ConvertBack(IList<object?> values, Type targetType, object? parameter, CultureInfo culture)
        => throw new NotImplementedException();
}


