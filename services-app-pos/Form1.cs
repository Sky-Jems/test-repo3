using services_app_pos.Services.Interfaces;
using System.Diagnostics;

namespace services_app_pos
{
    public partial class Form1 : Form
    {
        private readonly IServices service = new Services.Services();

        public Form1()
        {
            InitializeComponent();
            service.MainServiceEvent += (sender, e) =>
            {
                if ((int)Utils.Constants.MainService.PostgreSQL == e)
                {
                    this.Invoke(new Action(() => { postgreSQLBtn.Enabled = false; }));

                }
                else if ((int)Utils.Constants.MainService.Zookeeper == e)
                {
                    this.Invoke(new Action(() =>
                    {
                        zookeeperBtn.Enabled = false;
                        kafkaBtn.Enabled = true;
                    }));
                }
                else if ((int)Utils.Constants.MainService.Kafka == e)
                {
                    this.Invoke(new Action(() => { kafkaBtn.Enabled = false; }));
                }
            };

            dataGridView1.DataSource = service.GetServiceList();
            dataGridView1.Columns["Path"].Visible = false;
            dataGridView1.Columns["Command"].Visible = false;
        }

        private void FormClosingEvent(object sender, FormClosingEventArgs e)
        {
            service.StopMicroServices();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            service.StartPostgreSQL();
        }

        private void button3_Click(object sender, EventArgs e)
        {
            service.StartKafka();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            service.StartZookeeper();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            service.StopMicroServices();
        }
    }
} 