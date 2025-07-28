using System;
using System.Reactive;
using System.Threading.Tasks;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using pos.Extensions;
using ReactiveUI;
using ReactiveUI.Fody.Helpers;

namespace Pos.Pages.Home;

public class HomePageViewModel : ReactiveObject, IRoutableViewModel
{
    public string? UrlPathSegment => throw new NotImplementedException();

    public IScreen HostScreen { get; }

    [Reactive]
    public string Name { get; set; }
    [Reactive]
    public string Role { get; set; }

    public ReactiveCommand<Unit, Unit> LogoutCommand { get; }

    public HomePageViewModel(IScreen screen, string name, string role)
    {
        HostScreen = screen;
        Name = name;
        Role = role;
        LogoutCommand = ReactiveCommand.CreateFromTask(LogoutAsync);
    }

    public async Task LogoutAsync()
    {
        try
        {
            var authService = ServiceLocator.Services.GetService<IAuthService>();
            var cartService = ServiceLocator.Services.GetService<ICartService>();
            if (authService != null)
            {
                cartService?.Reset();
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
