using System.Diagnostics;
using CommunityToolkit.Mvvm.ComponentModel;

namespace services_app_pos.Models
{
    partial class ServiceModel : ObservableObject
    {
        [ObservableProperty]
        public string name = "";

        [ObservableProperty]
        public string status = "";

        [ObservableProperty]
        public string path = "";

        [ObservableProperty]
        public Process? command;
    }
}
