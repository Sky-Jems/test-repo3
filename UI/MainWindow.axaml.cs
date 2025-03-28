using Avalonia.ReactiveUI;
using Pos.ViewModels;

namespace Pos.Views;

public partial class MainWindow : ReactiveWindow<MainWindowViewModel>
{
    public MainWindow()
    {
        InitializeComponent();
    }
}