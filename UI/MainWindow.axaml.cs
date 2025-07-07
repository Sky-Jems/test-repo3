using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.Controls.Notifications;
using System.Reactive.Linq;
using System;
using pos.Models.EventArgs;

namespace Pos;

public partial class MainWindow : Window
{
    public static WindowNotificationManager? NotificationManager { get; private set; }

    public MainWindow()
    {
        NotificationManager = new WindowNotificationManager(TopLevel.GetTopLevel(this)) { MaxItems = 3 };
        DataContext = new MainWindowViewModel();
        InitializeComponent();
        if (DataContext is MainWindowViewModel viewModel)
        {
            viewModel.UnknownUser += () =>
            {
                ErrorTextBlock.Text = $"Unknown user role for {viewModel.Username}";
                ErrorTextBlock.IsVisible = true;
            };

            viewModel.LoginFailed += (msg) =>
            {
                ErrorTextBlock.Text = $"Login failed";
                ErrorTextBlock.IsVisible = true;
            };
        }
        UsernameTextBox.Focus();
    }

    public async void onClickLogin(object? sender, RoutedEventArgs args)
    {
        var username = UsernameTextBox.Text;
        var password = PasswordTextBox.Text;

        if (string.IsNullOrEmpty(username) || string.IsNullOrEmpty(password))
        {
            ErrorTextBlock.Text = "Username and password cannot be empty.";
            ErrorTextBlock.IsVisible = true;
            return;
        }

        ErrorTextBlock.IsVisible = false;

        var viewModel = this.DataContext as MainWindowViewModel;
        if (viewModel == null)
        {
            ErrorTextBlock.Text = "Internal error: ViewModel not set.";
            ErrorTextBlock.IsVisible = true;
            return;
        }

        viewModel.Username = username;
        viewModel.Password = password;

        try
        {
            await viewModel.LoginCommand.Execute();
        }
        catch (Exception ex)
        {
            ErrorTextBlock.Text = $"Login failed";
            ErrorTextBlock.IsVisible = true;
        }
    }

    public static void NotificationMessage(object? sender, NotificationEventArgs args)
    {
        MainWindow.NotificationManager?.Show(
            new Notification("New Message", args.Message),
            (NotificationType)args.NotifType,
            TimeSpan.FromSeconds(5),
            classes: args.Classes
        );
    }
}