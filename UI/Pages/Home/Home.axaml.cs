using Avalonia.Controls;

namespace Pos.Pages.Home;

public partial class Home : UserControl
{
    public Home()
    {
        InitializeComponent();
        DataContext = new HomeViewModel();
    }
}