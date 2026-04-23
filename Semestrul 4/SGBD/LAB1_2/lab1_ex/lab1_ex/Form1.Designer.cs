namespace lab1_ex;

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
    /// Required method for Designer support - do not modify
    /// the contents of this method with the code editor.
    /// </summary>
    private void InitializeComponent()
    {
        label1 = new System.Windows.Forms.Label();
        dataGridView1 = new System.Windows.Forms.DataGridView();
        label2 = new System.Windows.Forms.Label();
        ((System.ComponentModel.ISupportInitialize)dataGridView1).BeginInit();
        SuspendLayout();
        // 
        // label1
        // 
        label1.Font = new System.Drawing.Font("Segoe UI", 12F, System.Drawing.FontStyle.Bold);
        label1.Location = new System.Drawing.Point(53, 31);
        label1.Name = "label1";
        label1.Size = new System.Drawing.Size(262, 45);
        label1.TabIndex = 0;
        label1.Text = "Tabelul Motociclete";
        label1.Click += label1_Click;
        // 
        // dataGridView1
        // 
        dataGridView1.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        dataGridView1.Location = new System.Drawing.Point(114, 142);
        dataGridView1.Name = "dataGridView1";
        dataGridView1.RowHeadersWidth = 62;
        dataGridView1.Size = new System.Drawing.Size(1120, 425);
        dataGridView1.TabIndex = 1;
        dataGridView1.Text = "dataGridView1";
        // 
        // label2
        // 
        label2.Location = new System.Drawing.Point(511, 31);
        label2.Name = "label2";
        label2.Size = new System.Drawing.Size(261, 57);
        label2.TabIndex = 2;
        label2.Text = "label2";
        label2.Click += label2_Click;
        // 
        // Form1
        // 
        AutoScaleDimensions = new System.Drawing.SizeF(10F, 25F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        BackColor = System.Drawing.SystemColors.Highlight;
        ClientSize = new System.Drawing.Size(1350, 665);
        Controls.Add(label2);
        Controls.Add(dataGridView1);
        Controls.Add(label1);
        Text = "Form1";
        Load += Form1_Load_1;
        ((System.ComponentModel.ISupportInitialize)dataGridView1).EndInit();
        ResumeLayout(false);
    }

    private System.Windows.Forms.Label label2;

    private System.Windows.Forms.DataGridView dataGridView1;

    private System.Windows.Forms.Label label1;

    #endregion
}