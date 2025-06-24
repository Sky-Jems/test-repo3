using System;
using Avalonia.Data.Converters;
using Avalonia.Media;
using System.Globalization;

namespace Pos.Converters;

public class BoolToBrushConverter : IValueConverter
{
    public IBrush TrueBrush { get; set; } = Brushes.RoyalBlue;
    public IBrush FalseBrush { get; set; } = Brushes.Transparent;

    public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
    {
        var isTrue = value is bool b && b;

        // Check if dynamic parameter was passed
        if (parameter is string brushParams)
        {
            var parts = brushParams.Split(',', StringSplitOptions.RemoveEmptyEntries | StringSplitOptions.TrimEntries);
            if (parts.Length == 2)
            {
                var dynamicTrueBrush = TryParseBrush(parts[0]) ?? TrueBrush;
                var dynamicFalseBrush = TryParseBrush(parts[1]) ?? FalseBrush;
                return isTrue ? dynamicTrueBrush : dynamicFalseBrush;
            }
        }

        return isTrue ? TrueBrush : FalseBrush;
    }

    public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture) =>
        Avalonia.Data.BindingOperations.DoNothing;
    
    private IBrush? TryParseBrush(string brushString)
    {
        try
        {
            var converter = Avalonia.Media.Brush.Parse(brushString);
            return converter;
        }
        catch
        {
            return null;
        }
    }
}
