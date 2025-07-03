using services_app_pos.Models;
using System.ComponentModel;

namespace services_app_pos.Services.Interfaces
{
    internal interface IServices
    {
        public BindingList<ServiceModel> GetServiceList();
        public void StopMicroServices();
        public void StartPostgreSQL();
        public void StartKafka();
        public void StartZookeeper();
        public event EventHandler<int> MainServiceEvent;
    }
}
