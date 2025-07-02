using System;
using System.Globalization;
using Avalonia.Data;
using Avalonia.Data.Converters;

namespace Pos.Converters;

// https://docs.avaloniaui.net/docs/guides/data-binding/how-to-create-a-custom-data-binding-converter
public class TextCaseConverter : IValueConverter
{
    public static readonly TextCaseConverter Instance = new();

    public object? Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        if (value is string sourceText && parameter is string targetCase)
        {
            switch (targetCase)
            {
                case "upper":
                case "SQL":
                    return sourceText.ToUpper();
                case "lower":
                    return sourceText.ToLower();
                case "title": // Every First Letter Uppercase
                    var txtinfo = new CultureInfo("en-US", false).TextInfo;
                    return txtinfo.ToTitleCase(sourceText.ToLower());
                default:
                    // invalid option, return the exception below
                    break;
            }
        }
        // converter used for the wrong type
        return new BindingNotification(new InvalidCastException(), BindingErrorType.Error);
    }

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture) => throw new NotSupportedException();
}
