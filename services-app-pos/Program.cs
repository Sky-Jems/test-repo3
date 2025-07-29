using Microsoft.Extensions.Configuration;
using services_app_pos.Models;
using System.Diagnostics;
namespace services_app_pos
{
    internal static class Program
    {
        public static IConfiguration Configuration { get; private set; }
        public static CustomSettings customSettings = new CustomSettings();
        /// <summary>
        ///  The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            if (Process.GetProcessesByName(Process.GetCurrentProcess().ProcessName).Length > 1)
            {
                MessageBox.Show("Application already running.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            IConfigurationBuilder builder = new ConfigurationBuilder()
                .SetBasePath(AppContext.BaseDirectory)
                .AddJsonFile("appsettings.json", optional: true, reloadOnChange: true);

            Configuration = builder.Build();
            Configuration.GetSection("CustomSettings").Bind(customSettings);

            // To customize application configuration such as set high DPI settings or default font,
            // see https://aka.ms/applicationconfiguration.
            ApplicationConfiguration.Initialize();
            Application.Run(new Form1());
        }
    }
}