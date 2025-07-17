using System;
using System.Reactive.Linq;
using Avalonia.Interactivity;
using Avalonia.ReactiveUI;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using pos.Extensions;
using ReactiveUI;

namespace Pos.Pages.Home;

public record SelectedTabIndexMessage(int Index);

public partial class HomePage : ReactiveUserControl<HomePageViewModel>
{
    public HomePage()
    {
        InitializeComponent();
        MessageBus.Current.Listen<SelectedTabIndexMessage>().Subscribe(message => homeTabStrip.SelectedIndex = message.Index);
    }

    public async void OnClickLogout(object? sender, RoutedEventArgs args)
    {
        var viewModel = this.DataContext as HomePageViewModel;
        if (viewModel == null)
        {
            Console.WriteLine("Internal error: ViewModel not set.");
            return;
        }
        try
        {
            await viewModel.LogoutCommand.Execute();
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Logout failed: {ex.Message}");
        }

    }
}