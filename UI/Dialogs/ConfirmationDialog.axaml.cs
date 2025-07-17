using Avalonia;
using Avalonia.Interactivity;
using Avalonia.Layout;
using AvaloniaDialogs.Views;

namespace Pos.Dialogs;

public partial class ConfirmationDialog : TwofoldDialog
{
    public static readonly StyledProperty<string> TitleProperty =
        AvaloniaProperty.Register<SingleActionDialog, string>(nameof(Title));

    public string Title
    {
        get { return GetValue(TitleProperty); }
        set { SetValue(TitleProperty, value); }
    }

    public ConfirmationDialog()
    {
        HorizontalButtonAlignment = HorizontalAlignment.Stretch;
        InitializeComponent();
    }

    private void CloseDialog(object sender, RoutedEventArgs args) => Close();
}