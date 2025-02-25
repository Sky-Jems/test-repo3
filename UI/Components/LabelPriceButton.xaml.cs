using System.Windows.Input;
namespace POS.Components;

public partial class LabelPriceButton : ContentView
{
	public LabelPriceButton()
	{
		InitializeComponent();
		var tapGesture = new TapGestureRecognizer();
        tapGesture.Tapped += async (s, e) =>
        {
            if (ClickCommand?.CanExecute(null) == true)
            {
                ClickCommand.Execute(null);
            }
            await AnimateButton();
        };
        GestureRecognizers.Add(tapGesture);
	}
    // Animation when button is clicked
    private async Task AnimateButton()
    {
        // Fade Out (50% opacity)
        await this.FadeTo(0.5, 50, Easing.Linear);
        // Fade In (100% opacity)
        await this.FadeTo(1, 50, Easing.Linear);
    }

	public static readonly BindableProperty LabelProperty =
            BindableProperty.Create(nameof(Label), typeof(string), typeof(LabelPriceButton));

    public string Label
    {
        get => (string)GetValue(LabelProperty);
        set => SetValue(LabelProperty, value);
    }

    	public static readonly BindableProperty PriceProperty =
            BindableProperty.Create(nameof(Price), typeof(string), typeof(LabelPriceButton));

    public string Price
    {
        get => (string)GetValue(PriceProperty);
        set => SetValue(PriceProperty, value);
    }

    // Button Color property
    public static readonly BindableProperty ButtonColorProperty =
        BindableProperty.Create(nameof(ButtonColor), typeof(Color), typeof(CustomImageButton), Colors.BlueViolet);

    public Color ButtonColor
    {
        get => (Color)GetValue(ButtonColorProperty);
        set => SetValue(ButtonColorProperty, value);
    }

    // Click Command property
    public static readonly BindableProperty ClickCommandProperty =
        BindableProperty.Create(nameof(ClickCommand), typeof(ICommand), typeof(CustomImageButton));

    public ICommand ClickCommand
    {
        get => (ICommand)GetValue(ClickCommandProperty);
        set => SetValue(ClickCommandProperty, value);
    }
    // Button Width property (Default: 150)
    public static readonly BindableProperty ButtonWidthProperty =
        BindableProperty.Create(nameof(ButtonWidth), typeof(double), typeof(CustomImageButton), 150.0);

    public double ButtonWidth
    {
        get => (double)GetValue(ButtonWidthProperty);
        set => SetValue(ButtonWidthProperty, value);
    }

    // Button Height property (Default: 50)
    public static readonly BindableProperty ButtonHeightProperty =
        BindableProperty.Create(nameof(ButtonHeight), typeof(double), typeof(CustomImageButton), 50.0);

    public double ButtonHeight
    {
        get => (double)GetValue(ButtonHeightProperty);
        set => SetValue(ButtonHeightProperty, value);
    }
}