using CommunityToolkit.Mvvm.ComponentModel;

namespace services_app_pos.Models
{
    partial class PrerequisitesModel : ObservableObject
    {
        [ObservableProperty]
        public bool postgres;

        [ObservableProperty]
        public bool kafka;

        [ObservableProperty]
        public bool zookeeper;
    }
}
