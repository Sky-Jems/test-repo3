using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.ReactiveUI;
using Pos.Pages;
using System.Reactive;
using System.Reactive.Linq;
using ReactiveUI;
using System;
using pos.Extensions;
using pos.Api;
using Microsoft.Extensions.DependencyInjection;

namespace Pos;

public partial class MainWindow : Window
{
    public MainWindow()
    {
        DataContext = new MainWindowViewModel();
        InitializeComponent();
        DataContext = new MainWindowViewModel();
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
}