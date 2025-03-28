using System;
using Avalonia.Data.Converters;
using System.Globalization;

public class TabItemController : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        if (value is double width)
        {
            return Math.Floor(width / 5); // Divide the TabControl width into 5 equal parts
        }
        return 100; // Fallback width
    }

    public object? ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        throw new NotImplementedException();
    }
}
