using src.ViewModels;

namespace POS.Views;

public partial class ProductsPanel
{
	public event EventHandler? BackButtonClicked;
	public ProductsPanel()
	{
		InitializeComponent();
        BindingContext = new ProductsViewModel();
	}

	private async void OnBackButtonTapped(object sender, EventArgs e)
	{
		if (sender is Border border)
		{
			await border.FadeTo(0.5, 50, Easing.Linear);
            await border.FadeTo(1, 50, Easing.Linear);
			BackButtonClicked?.Invoke(this, EventArgs.Empty);
		}
	}
}