using services_app_pos.Models;
using System.Diagnostics;
using services_app_pos.Services.Interfaces;
using System.ComponentModel;

namespace services_app_pos.Services
{
    internal class Services : IServices
    {
        public event EventHandler<int> MainServiceEvent;
        private readonly string JavaBinDirectory = $@"{Program.customSettings.MainDrive}\Java\jdk-24.0.1\bin";
        private readonly string KafkaDirectory = $@"{Program.customSettings.MainDrive}\Kafka\kafka_2.12-3.9.1\";
        private readonly static string InstalledDirectory = Program.customSettings.MainDrive + Program.customSettings.InstallationDirectory;

        private BindingList<ServiceModel> MicroserviceList = new BindingList<ServiceModel>
        {
            new ServiceModel { Name = "Auth", Path = $@"{InstalledDirectory}\auth\auth-service-0.0.1-SNAPSHOT.jar"},
            new ServiceModel { Name = "Billing", Path = $@"{InstalledDirectory}\billing\billing-service-0.0.1-SNAPSHOT.jar"},
            new ServiceModel { Name = "Discount", Path = $@"{InstalledDirectory}\discount\discount-service-0.0.1-SNAPSHOT.jar"},
            new ServiceModel { Name = "Gateway", Path = $@"{InstalledDirectory}\gateway\gateway-service-0.0.1-SNAPSHOT.jar"},
            new ServiceModel { Name = "Orders", Path = $@"{InstalledDirectory}\order\order-service-0.0.1-SNAPSHOT.jar"},
            new ServiceModel { Name = "Order Orchestrator", Path = $@"{InstalledDirectory}\orchestrator\order-orchestrator-service-0.0.1-SNAPSHOT.jar"},
            new ServiceModel { Name = "Payment", Path = $@"{InstalledDirectory}\payment\payment-service-0.0.1-SNAPSHOT.jar"},
            new ServiceModel { Name = "Product", Path = $@"{InstalledDirectory}\product\product-service-0.0.1-SNAPSHOT.jar"}
        };

        private PrerequisitesModel PrerequisitesStatus = new PrerequisitesModel
        {
            kafka = false,
            postgres = false,
            zookeeper = false
        };

        public Services()
        {
            PrerequisitesStatus.PropertyChanged += PrerequisitesPropertyChanged;
        }

        private void PrerequisitesPropertyChanged(object? sender, PropertyChangedEventArgs e)
        {
            if (PrerequisitesStatus.Zookeeper && PrerequisitesStatus.Kafka && PrerequisitesStatus.Postgres)
            {
                CreateDatabase();
                StartMicroServices();
            }
        }
        
        public BindingList<ServiceModel> GetServiceList()
        {
            return MicroserviceList;
        }

        private void CreateDatabase()
        {
            ProcessStartInfo startInfo = new ProcessStartInfo()
            {
                FileName = $@"{InstalledDirectory}\create-database.bat",
                UseShellExecute = false,
                CreateNoWindow = true
            };

            Process cmd = new Process();
            cmd.StartInfo = startInfo;
            cmd.Start();
            cmd.WaitForExit();
            cmd.Kill();
        }
        public void StartPostgreSQL()
        {
            string PostgresPath = Program.customSettings.MainDrive + Program.customSettings.PostgreSQLPath;
            string PostgresData = Program.customSettings.MainDrive + Program.customSettings.PostgreSQLData;

            bool isRunning = Process.GetProcessesByName("postgres").Any();
            if (isRunning) 
            {
                PrerequisitesStatus.Postgres = true;
                MainServiceEvent?.Invoke(this, (int)Utils.Constants.MainService.PostgreSQL);
                return;
            }
            ProcessStartInfo startInfo = new ProcessStartInfo
            {
                FileName = PostgresPath,
                Arguments = $@"start -D ""{PostgresData}""",
                UseShellExecute = false,
                RedirectStandardOutput = true,
                CreateNoWindow = true
            };

            Process command = new Process();
            command.StartInfo = startInfo;
            command.OutputDataReceived += (sender, e) =>
            {
                if (!string.IsNullOrWhiteSpace(e.Data) && e.Data.Contains("server started") && sender is Process item)
                {
                    PrerequisitesStatus.Postgres = true;
                    MainServiceEvent?.Invoke(this, (int)Utils.Constants.MainService.PostgreSQL);
                }
            };

            command.Start();
            command.BeginOutputReadLine();
        }

        public void StartZookeeper()
        {
            if (IsRunning(Program.customSettings.ZookeeperPort))
            {
                PrerequisitesStatus.Zookeeper = true;
                MainServiceEvent?.Invoke(this, (int)Utils.Constants.MainService.Zookeeper);
                return;
            }

            ProcessStartInfo startInfo = new ProcessStartInfo
            {
                FileName = "cmd.exe",
                Arguments = $@"/c set PATH=""{JavaBinDirectory}"";%PATH% && .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties",
                WorkingDirectory = KafkaDirectory,
                UseShellExecute = false,
                RedirectStandardOutput = true,
                CreateNoWindow = true,
            };

            Process command = new Process();
            command.StartInfo = startInfo;
            command.OutputDataReceived += (sender, e) =>
            {
                if (!string.IsNullOrWhiteSpace(e.Data) && e.Data.Contains("binding to port 0.0.0.0/0.0.0.0"))
                {
                    PrerequisitesStatus.Zookeeper = true;
                    MainServiceEvent?.Invoke(this, (int)Utils.Constants.MainService.Zookeeper);
                }
            };
            command.Start();
            command.BeginOutputReadLine();
        }

        public void StartKafka()
        {
            if (IsRunning(Program.customSettings.KafkaPort))
            {
                PrerequisitesStatus.Kafka = true;
                MainServiceEvent?.Invoke(this, (int)Utils.Constants.MainService.Kafka);
                return;
            }

            ProcessStartInfo startInfo = new ProcessStartInfo
            {
                FileName = "cmd.exe",
                Arguments = $@"/c set PATH=""{JavaBinDirectory}"";%PATH% && .\bin\windows\kafka-server-start.bat .\config\server.properties",
                WorkingDirectory = KafkaDirectory,
                UseShellExecute = false,
                RedirectStandardOutput = true,
                CreateNoWindow = true
            };

            Process command = new Process();
            command.StartInfo = startInfo;
            command.OutputDataReceived += (sender, e) =>
            {
                if (!string.IsNullOrWhiteSpace(e.Data) && e.Data.Contains("Awaiting socket connections on 0.0.0.0") && sender is Process item)
                {
                    PrerequisitesStatus.Kafka = true;
                    MainServiceEvent?.Invoke(this, (int)Utils.Constants.MainService.Kafka);
                }
            };
            command.Start();
            command.BeginOutputReadLine();
        }

        static bool IsRunning(int port)
        {
            var proc = new Process
            {
                StartInfo = new ProcessStartInfo
                {
                    FileName = "cmd.exe",
                    Arguments = $"/c netstat -aon | findstr :{port}",
                    RedirectStandardOutput = true,
                    UseShellExecute = false,
                    CreateNoWindow = true
                }
            };

            proc.Start();
            string output = proc.StandardOutput.ReadToEnd();
            proc.WaitForExit();
            proc.Kill();

            return !string.IsNullOrWhiteSpace(output);
        }

        private void StartMicroServices()
        {
            foreach (ServiceModel service in MicroserviceList)
            {
                try
                {
                    Process command = new Process();
                    command.StartInfo.FileName = $@"{JavaBinDirectory}\java.exe";
                    command.StartInfo.Arguments = $@"-jar ""{service.Path}""";
                    command.StartInfo.WorkingDirectory = $@"{Path.GetDirectoryName(service.Path)}";
                    command.StartInfo.UseShellExecute = false;
                    command.StartInfo.CreateNoWindow = true;
                    command.StartInfo.RedirectStandardOutput = true;
                    command.StartInfo.RedirectStandardError = true;

                    command.OutputDataReceived += OutputDataReceivedHandler;
                    command.ErrorDataReceived += ErrorDataReceivedHandler;

                    command.Start();
                    service.command = command;
                    command.BeginOutputReadLine();
                    command.BeginErrorReadLine();
                }
                catch (Exception ex)
                {
                    Debug.WriteLine($"{service.Name} service failed to start");
                }
            }
        }

        private void OutputDataReceivedHandler(object sender, DataReceivedEventArgs e)
        {
            if(!string.IsNullOrWhiteSpace(e.Data) && e.Data.Contains("Tomcat started on port") && sender is Process item)
            {
                foreach(ServiceModel service in MicroserviceList.Where(e => e.command.Id == item.Id))
                {
                    service.Status = "RUNNING";
                }
            }
        }

        private void ErrorDataReceivedHandler(object sender, DataReceivedEventArgs e)
        {
            if(!string.IsNullOrWhiteSpace(e.Data) && e.Data.Contains("Unable to access jarfile") && sender is Process item)
            {
                foreach(ServiceModel service in MicroserviceList.Where(e => e.command != null && e.command.Id == item.Id))
                {
                    service.status = "NOT FOUND";
                    service.command.CancelErrorRead();
                    service.command.Kill();
                }
            }
        }

        public void StopMicroServices()
        {
            Process command = new Process
            {
                StartInfo = new ProcessStartInfo
                {
                    FileName = "powershell.exe",
                    Arguments = "Get-CimInstance Win32_Process | Where-Object {$_.CommandLine -like '*-jar*' } | ForEach-Object { Stop-Process -Id $_.ProcessId -Force }",
                    UseShellExecute = false,
                    CreateNoWindow = true
                }
            };
            command.Start();
            command.WaitForExit();
            command.Kill();
        }
    }
}
