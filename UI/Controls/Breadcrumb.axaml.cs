using Avalonia;
using Avalonia.Controls.Primitives;
using Avalonia.Metadata;

namespace Pos.Controls;

public class Breadcrumb : TemplatedControl
{
    #region Content
    public static readonly StyledProperty<object> ContentProperty =
        AvaloniaProperty.Register<Breadcrumb, object>(nameof(Content));

    [Content]
    public object Content
    {
        get { return GetValue(ContentProperty); }
        set { SetValue(ContentProperty, value); }
    }
    #endregion
}