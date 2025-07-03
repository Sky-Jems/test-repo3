using System.IO;

namespace services_app_pos.Utils
{
    static class FileLogger
    {
        public static void SaveLog(string message, string fileName)
        {
            string folder = Path.Combine(
                Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData), 
                "ServerPOS"
            );

            Directory.CreateDirectory(folder);

            string logFilePath = Path.Combine(folder, fileName);
            File.AppendAllText(logFilePath, $"\n{message}");
           
        }
    }

    static class FileConstants
    {
        public static string ZookeeperLogs = "zookeeper.txt";
        public static string KafkaLogs = "kafka.txt";
    }
}
