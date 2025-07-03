namespace services_app_pos
{
    partial class Form1
    {
        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            label1 = new Label();
            dataGridView1 = new DataGridView();
            postgreSQLBtn = new Button();
            zookeeperBtn = new Button();
            kafkaBtn = new Button();
            ((System.ComponentModel.ISupportInitialize)dataGridView1).BeginInit();
            SuspendLayout();
            // 
            // label1
            // 
            label1.AutoSize = true;
            label1.Font = new Font("Segoe UI", 16F);
            label1.Location = new Point(12, 9);
            label1.Name = "label1";
            label1.Size = new Size(92, 30);
            label1.TabIndex = 0;
            label1.Text = "Services";
            // 
            // dataGridView1
            // 
            dataGridView1.AllowUserToAddRows = false;
            dataGridView1.AllowUserToDeleteRows = false;
            dataGridView1.AllowUserToResizeColumns = false;
            dataGridView1.AllowUserToResizeRows = false;
            dataGridView1.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dataGridView1.BorderStyle = BorderStyle.None;
            dataGridView1.ColumnHeadersBorderStyle = DataGridViewHeaderBorderStyle.Single;
            dataGridView1.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            dataGridView1.Location = new Point(12, 109);
            dataGridView1.Name = "dataGridView1";
            dataGridView1.ReadOnly = true;
            dataGridView1.RowHeadersBorderStyle = DataGridViewHeaderBorderStyle.None;
            dataGridView1.RowHeadersVisible = false;
            dataGridView1.ScrollBars = ScrollBars.None;
            dataGridView1.ShowCellErrors = false;
            dataGridView1.ShowCellToolTips = false;
            dataGridView1.ShowEditingIcon = false;
            dataGridView1.ShowRowErrors = false;
            dataGridView1.Size = new Size(282, 222);
            dataGridView1.TabIndex = 1;
            // 
            // postgreSQLBtn
            // 
            postgreSQLBtn.Location = new Point(12, 56);
            postgreSQLBtn.Name = "postgreSQLBtn";
            postgreSQLBtn.Size = new Size(92, 47);
            postgreSQLBtn.TabIndex = 3;
            postgreSQLBtn.Text = "Start PostgreSQL";
            postgreSQLBtn.UseVisualStyleBackColor = true;
            postgreSQLBtn.Click += button2_Click;
            // 
            // zookeeperBtn
            // 
            zookeeperBtn.Location = new Point(107, 56);
            zookeeperBtn.Name = "zookeeperBtn";
            zookeeperBtn.Size = new Size(92, 47);
            zookeeperBtn.TabIndex = 4;
            zookeeperBtn.Text = "Start Zookeeper";
            zookeeperBtn.UseVisualStyleBackColor = true;
            zookeeperBtn.Click += button1_Click;
            // 
            // kafkaBtn
            // 
            kafkaBtn.Enabled = false;
            kafkaBtn.Location = new Point(202, 56);
            kafkaBtn.Name = "kafkaBtn";
            kafkaBtn.Size = new Size(92, 47);
            kafkaBtn.TabIndex = 5;
            kafkaBtn.Text = "Start Kafka";
            kafkaBtn.UseVisualStyleBackColor = true;
            kafkaBtn.Click += button3_Click;
            // 
            // Form1
            // 
            AutoScaleDimensions = new SizeF(7F, 15F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(306, 342);
            Controls.Add(kafkaBtn);
            Controls.Add(zookeeperBtn);
            Controls.Add(postgreSQLBtn);
            Controls.Add(dataGridView1);
            Controls.Add(label1);
            Name = "Form1";
            Text = "ServerPOS";
            FormClosing += FormClosingEvent;
            Load += Form1_Load;
            ((System.ComponentModel.ISupportInitialize)dataGridView1).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private Label label1;
        private DataGridView dataGridView1;
        public Button postgreSQLBtn;
        public Button zookeeperBtn;
        public Button kafkaBtn;
    }
}
