using System;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Data;
using Avalonia.Threading;

namespace Pos.Behaviors;

public static class FocusBehavior
{
    public static readonly AttachedProperty<bool> IsFocusedProperty =
        AvaloniaProperty.RegisterAttached<Control, bool>(
            "IsFocused", typeof(FocusBehavior), false, defaultBindingMode: Avalonia.Data.BindingMode.TwoWay);

    public static void SetIsFocused(AvaloniaObject element, bool value)
        => element.SetValue(IsFocusedProperty, value);

    public static bool GetIsFocused(AvaloniaObject element)
        => element.GetValue(IsFocusedProperty);
    
    static FocusBehavior()
    {
        IsFocusedProperty.Changed.Subscribe(args =>
        {
            if (args.Sender is Control control &&
                args.NewValue is BindingValue<bool> bindingVal &&
                bindingVal.HasValue &&
                bindingVal.Value)
            {
                Dispatcher.UIThread.Post(() =>
                {
                    control.Focus();

                    // Reset IsFocused after focusing
                    control.SetValue(IsFocusedProperty, false);
                });
            }
        });
    }
}



