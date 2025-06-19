using System;

namespace pos.Extensions;

public static class StringExtensions
{
    public static string ToUrlEncoded(this String str)
    {
        return Uri.EscapeDataString(str.Replace("+", "%2B"));
    }
}