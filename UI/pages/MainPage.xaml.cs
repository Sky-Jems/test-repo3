using POS.Views;

namespace pos;

public partial class MainPage : ContentPage
{

	public MainPage()
	{
		InitializeComponent();
		var WidgetsPanel = new WidgetsPanel();
		WidgetsPanel.ShowProducts += ShowProductsRequested;
		DynamicContentView.Content = WidgetsPanel;
	}

	private void ShowProductsRequested(object? sender, EventArgs e)
	{
		var ProductsPanel = new ProductsPanel();
		ProductsPanel.BackButtonClicked += OnBackButtonClicked;
		DynamicContentView.Content = ProductsPanel;
	}

	private void OnBackButtonClicked(object? sender, EventArgs e)
	{
		var WidgetsPanel = new WidgetsPanel();
		WidgetsPanel.ShowProducts += ShowProductsRequested;
		DynamicContentView.Content = WidgetsPanel;
	}
}

