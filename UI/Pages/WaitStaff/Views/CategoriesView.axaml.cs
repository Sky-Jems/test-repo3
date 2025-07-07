using Avalonia;
using Avalonia.ReactiveUI;
using Avalonia.Threading;

namespace Pos.Pages.WaitStaff;

public partial class CategoriesView : ReactiveUserControl<CategoriesViewModel>
{
    public CategoriesView()
    {
        InitializeComponent();
    }

    protected override void OnAttachedToVisualTree(VisualTreeAttachmentEventArgs e)
    {
        base.OnAttachedToVisualTree(e);
        Dispatcher.UIThread.Invoke(() =>
        {
            ViewModel?.LoadCategoriesCommand.Execute();
        });
    }
}