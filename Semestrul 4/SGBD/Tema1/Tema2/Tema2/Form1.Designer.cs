namespace Tema2;

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
        dataGridViewParent = new System.Windows.Forms.DataGridView();
        dataGridViewChild = new System.Windows.Forms.DataGridView();
        labelParent = new System.Windows.Forms.Label();
        labelChild = new System.Windows.Forms.Label();
        button1 = new System.Windows.Forms.Button();
        button2 = new System.Windows.Forms.Button();
        button3 = new System.Windows.Forms.Button();
        panel1 = new System.Windows.Forms.Panel();
        ((System.ComponentModel.ISupportInitialize)dataGridViewParent).BeginInit();
        ((System.ComponentModel.ISupportInitialize)dataGridViewChild).BeginInit();
        SuspendLayout();
        // 
        // dataGridViewParent
        // 
        dataGridViewParent.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        dataGridViewParent.Location = new System.Drawing.Point(49, 68);
        dataGridViewParent.Name = "dataGridViewParent";
        dataGridViewParent.RowHeadersWidth = 62;
        dataGridViewParent.Size = new System.Drawing.Size(816, 272);
        dataGridViewParent.TabIndex = 0;
        dataGridViewParent.Text = "dataGridView1";
        dataGridViewParent.CellClick += dataGridViewParent_CellClick;
        // 
        // dataGridViewChild
        // 
        dataGridViewChild.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        dataGridViewChild.Location = new System.Drawing.Point(49, 400);
        dataGridViewChild.Name = "dataGridViewChild";
        dataGridViewChild.RowHeadersWidth = 62;
        dataGridViewChild.Size = new System.Drawing.Size(816, 281);
        dataGridViewChild.TabIndex = 1;
        dataGridViewChild.Text = "dataGridView2";
        // 
        // labelParent
        // 
        labelParent.Font = new System.Drawing.Font("Segoe UI", 14F, System.Drawing.FontStyle.Bold);
        labelParent.Location = new System.Drawing.Point(367, 16);
        labelParent.Name = "labelParent";
        labelParent.Size = new System.Drawing.Size(171, 35);
        labelParent.TabIndex = 2;
        labelParent.Click += label1_Click;
        // 
        // labelChild
        // 
        labelChild.Font = new System.Drawing.Font("Segoe UI", 14F, System.Drawing.FontStyle.Bold);
        labelChild.Location = new System.Drawing.Point(367, 354);
        labelChild.Name = "labelChild";
        labelChild.Size = new System.Drawing.Size(200, 43);
        labelChild.TabIndex = 3;
        // 
        // button1
        // 
        button1.Location = new System.Drawing.Point(909, 247);
        button1.Name = "button1";
        button1.Size = new System.Drawing.Size(131, 40);
        button1.TabIndex = 4;
        button1.Text = "Adauga";
        button1.UseVisualStyleBackColor = true;
        button1.Click += buttonAdd_Click;
        // 
        // button2
        // 
        button2.Location = new System.Drawing.Point(909, 346);
        button2.Name = "button2";
        button2.Size = new System.Drawing.Size(131, 40);
        button2.TabIndex = 5;
        button2.Text = "Sterge";
        button2.UseVisualStyleBackColor = true;
        button2.Click += buttonDelete_Click;
        // 
        // button3
        // 
        button3.Location = new System.Drawing.Point(909, 442);
        button3.Name = "button3";
        button3.Size = new System.Drawing.Size(131, 40);
        button3.TabIndex = 6;
        button3.Text = "Modifica";
        button3.UseVisualStyleBackColor = true;
        button3.Click += buttonUpdate_Click;
        // 
        // panel1
        // 
        panel1.Location = new System.Drawing.Point(49, 709);
        panel1.Name = "panel1";
        panel1.Size = new System.Drawing.Size(991, 168);
        panel1.TabIndex = 13;
        // 
        // Form1
        // 
        AutoScaleDimensions = new System.Drawing.SizeF(10F, 25F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        ClientSize = new System.Drawing.Size(1072, 896);
        Controls.Add(panel1);
        Controls.Add(button3);
        Controls.Add(button2);
        Controls.Add(button1);
        Controls.Add(labelChild);
        Controls.Add(labelParent);
        Controls.Add(dataGridViewChild);
        Controls.Add(dataGridViewParent);
        Text = "Form1";
        Load += Form1_Load;
        ((System.ComponentModel.ISupportInitialize)dataGridViewParent).EndInit();
        ((System.ComponentModel.ISupportInitialize)dataGridViewChild).EndInit();
        ResumeLayout(false);
    }

    private System.Windows.Forms.Panel panel1;

    private System.Windows.Forms.Button button1;
    private System.Windows.Forms.Button button2;
    private System.Windows.Forms.Button button3;

    private System.Windows.Forms.Label labelParent;
    private System.Windows.Forms.Label labelChild;

    private System.Windows.Forms.DataGridView dataGridViewChild;

    private System.Windows.Forms.DataGridView dataGridViewParent;

    #endregion
}