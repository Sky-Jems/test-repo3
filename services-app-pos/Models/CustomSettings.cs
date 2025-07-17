
namespace services_app_pos.Models
{
    partial class CustomSettings
    {
        public int KafkaPort { get; set; }
        public int ZookeeperPort { get; set; }
        public string MainDrive { get; set; } = string.Empty;
        public string PostgreSQLInstallationDirectory { get; set; } = string.Empty;
        public string PostgreSQLPath { get; set; } = string.Empty;
        public string PostgreSQLData { get; set; } = string.Empty;
        public string InstallationDirectory { get; set; } = string.Empty;
    }
}