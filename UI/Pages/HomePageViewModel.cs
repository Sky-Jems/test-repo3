using System;
using System.Reactive;
using System.Threading.Tasks;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using pos.Extensions;
using ReactiveUI;

namespace Pos.Pages;

public class HomePageViewModel : ReactiveObject, IRoutableViewModel
{
    public string? UrlPathSegment => throw new NotImplementedException();

    public IScreen HostScreen { get; }

    private string _Role;
    public string Role
    {
        get => _Role;
        set => this.RaiseAndSetIfChanged(ref _Role, value);
    }

    public ReactiveCommand<Unit, Unit> LogoutCommand { get; }

    public HomePageViewModel(IScreen screen, string role)
    {
        HostScreen = screen;
        Role = role;
        LogoutCommand = ReactiveCommand.CreateFromTask(LogoutAsync);
    }

    public async Task LogoutAsync()
    {
        try
        {
            var authService = ServiceLocator.Services.GetService<IAuthService>();
            if (authService != null)
            {
                await authService.LogoutAsync();
            }
            else
            {
                Console.WriteLine("AuthService not found.");
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Logout failed: {ex.Message}");
        }

        HostScreen.Router.NavigateBack.Execute();

    }
}
