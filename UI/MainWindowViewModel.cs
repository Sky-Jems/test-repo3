using System;
using System.Reactive;
using System.Threading.Tasks;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using pos.Extensions;
using Pos.Pages.Home;
using ReactiveUI;
using Pos.Util;

namespace Pos;

public class MainWindowViewModel : ReactiveObject, IScreen
{
    private readonly IAuthService _authService;
    public string? UrlPathSegment => "Main";
    public RoutingState Router { get; } = new RoutingState();
    public IScreen HostScreen { get; }

    public string Username { get; set; }
    public string Password { get; set; }

    public ReactiveCommand<Unit, Unit> LoginCommand { get; }
    public ReactiveCommand<Unit, Unit> NavigateStaffLoginCommand { get; }

    public event Action? UnknownUser;
    public event Action<string>? LoginFailed;

    public MainWindowViewModel()
    {
        _authService = ServiceLocator.Services.GetRequiredService<IAuthService>();
        Username = string.Empty;
        Password = string.Empty;

        LoginCommand = ReactiveCommand.CreateFromTask(LoginAsync);
        LoginCommand.ThrownExceptions.Subscribe(ex =>
        {
            Console.WriteLine($"Login failed: {ex.Message}");
            LoginFailed?.Invoke("Login failed");
        });
        NavigateStaffLoginCommand = ReactiveCommand.Create(() =>
       {
           Router.Navigate.Execute(new StaffLoginViewModel(this, _authService));

       });
    }

    public async Task LoginAsync()
    {
        var token = await _authService.LoginAsync(Username, Password);

        if (string.IsNullOrEmpty(token))
            throw new Exception("Login failed: Token is empty.");

        Router.Navigate.Execute(new HomePageViewModel(this, Username, Constants.Role.Admin));
    }
}
