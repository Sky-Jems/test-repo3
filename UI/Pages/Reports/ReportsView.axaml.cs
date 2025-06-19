using Avalonia.ReactiveUI;

namespace Pos.Pages.Reports;

public partial class ReportsView : ReactiveUserControl<ReportsViewModel>
{
    public ReportsView()
    {
        InitializeComponent();
        DataContext = new ReportsViewModel();
    }
}