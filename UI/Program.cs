using Avalonia;
using Avalonia.ReactiveUI;
using System;
using Microsoft.Extensions.Configuration;
using Pos.Models;
using pos.Models;

namespace Pos;

sealed class Program
{
    public static IConfiguration Configuration { get; private set; }
    public static CustomSettings customSettings = new CustomSettings();
    // Initialization code. Don't use any Avalonia, third-party APIs or any
    // SynchronizationContext-reliant code before AppMain is called: things aren't initialized
    // yet and stuff might break.
    [STAThread]
    public static void Main(string[] args) {
        IConfigurationBuilder builder = new ConfigurationBuilder()
            .SetBasePath(AppContext.BaseDirectory)
            .AddJsonFile("appsettings.json", optional: true, reloadOnChange: true);
        Configuration = builder.Build();
        Configuration.GetSection("CustomSettings").Bind(customSettings);
        BuildAvaloniaApp().StartWithClassicDesktopLifetime(args);
    }
        

    // Avalonia configuration, don't remove; also used by visual designer.
    public static AppBuilder BuildAvaloniaApp()
        => AppBuilder.Configure<App>()
            .UseReactiveUI()
            .UsePlatformDetect()
            .WithInterFont()
            .LogToTrace();
}
