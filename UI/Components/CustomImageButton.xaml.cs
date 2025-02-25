using Microsoft.Maui.Controls;
using System.Windows.Input;

namespace POS.Components
{
    public partial class CustomImageButton : ContentView
    {
        public event EventHandler? ButtonClicked;
        public CustomImageButton()
        {
            InitializeComponent();

            var tapGesture = new TapGestureRecognizer();
            tapGesture.Tapped += async (s, e) =>
            {
                ButtonClicked?.Invoke(this, EventArgs.Empty);
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

        // Click Command property
        public static readonly BindableProperty ClickCommandProperty =
            BindableProperty.Create(nameof(ClickCommand), typeof(ICommand), typeof(CustomImageButton));

        public ICommand ClickCommand
        {
            get => (ICommand)GetValue(ClickCommandProperty);
            set => SetValue(ClickCommandProperty, value);
        }

        // Text property
        public static readonly BindableProperty TextProperty =
            BindableProperty.Create(nameof(Text), typeof(string), typeof(CustomImageButton), "Click Me");

        public string Text
        {
            get => (string)GetValue(TextProperty);
            set => SetValue(TextProperty, value);
        }

        // Image property
        public static readonly BindableProperty ImageSourceProperty =
            BindableProperty.Create(nameof(ImageSource), typeof(ImageSource), typeof(CustomImageButton), default(ImageSource));

        public ImageSource ImageSource
        {
            get => (ImageSource)GetValue(ImageSourceProperty);
            set => SetValue(ImageSourceProperty, value);
        }

        // Image Width property 
        public static readonly BindableProperty ImageWidthProperty =
            BindableProperty.Create(nameof(ImageWidth), typeof(double), typeof(CustomImageButton), 50.0);

        public double ImageWidth
        {
            get => (double)GetValue(ImageWidthProperty);
            set => SetValue(ImageWidthProperty, value);
        }

        // Image Height property 
        public static readonly BindableProperty ImageHeightProperty =
            BindableProperty.Create(nameof(ImageHeight), typeof(double), typeof(CustomImageButton), 50.0);

        public double ImageHeight
        {
            get => (double)GetValue(ImageHeightProperty);
            set => SetValue(ImageHeightProperty, value);
        }

        // Button Color property
        public static readonly BindableProperty ButtonColorProperty =
            BindableProperty.Create(nameof(ButtonColor), typeof(Color), typeof(CustomImageButton), Colors.BlueViolet);

        public Color ButtonColor
        {
            get => (Color)GetValue(ButtonColorProperty);
            set => SetValue(ButtonColorProperty, value);
        }

        // Button Width property 
        public static readonly BindableProperty ButtonWidthProperty =
            BindableProperty.Create(nameof(ButtonWidth), typeof(double), typeof(CustomImageButton), 225.0);

        public double ButtonWidth
        {
            get => (double)GetValue(ButtonWidthProperty);
            set => SetValue(ButtonWidthProperty, value);
        }

        // Button Height property 
        public static readonly BindableProperty ButtonHeightProperty =
            BindableProperty.Create(nameof(ButtonHeight), typeof(double), typeof(CustomImageButton), 135.0);

        public double ButtonHeight
        {
            get => (double)GetValue(ButtonHeightProperty);
            set => SetValue(ButtonHeightProperty, value);
        }

        // Font Size property (Default: 30)
        public static readonly BindableProperty FontSizeProperty =
            BindableProperty.Create(nameof(FontSize), typeof(double), typeof(CustomImageButton), 30.0);

        public double FontSize
        {
            get => (double)GetValue(FontSizeProperty);
            set => SetValue(FontSizeProperty, value);
        }

        // Text Color property 
        public static readonly BindableProperty TextColorProperty =
            BindableProperty.Create(nameof(TextColor), typeof(Color), typeof(CustomImageButton), Colors.Black);

        public Color TextColor
        {
            get => (Color)GetValue(TextColorProperty);
            set => SetValue(TextColorProperty, value);
        }
    }
}
