namespace Tema1;

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
        dataGridView1 = new System.Windows.Forms.DataGridView();
        dataGridView2 = new System.Windows.Forms.DataGridView();
        label1 = new System.Windows.Forms.Label();
        label2 = new System.Windows.Forms.Label();
        button1 = new System.Windows.Forms.Button();
        button2 = new System.Windows.Forms.Button();
        button3 = new System.Windows.Forms.Button();
        comboBoxAntrenor = new System.Windows.Forms.ComboBox();
        textBoxTara = new System.Windows.Forms.TextBox();
        textBoxNume = new System.Windows.Forms.TextBox();
        label3 = new System.Windows.Forms.Label();
        label4 = new System.Windows.Forms.Label();
        label5 = new System.Windows.Forms.Label();
        ((System.ComponentModel.ISupportInitialize)dataGridView1).BeginInit();
        ((System.ComponentModel.ISupportInitialize)dataGridView2).BeginInit();
        SuspendLayout();
        // 
        // dataGridView1
        // 
        dataGridView1.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        dataGridView1.Location = new System.Drawing.Point(49, 92);
        dataGridView1.Name = "dataGridView1";
        dataGridView1.RowHeadersWidth = 62;
        dataGridView1.Size = new System.Drawing.Size(516, 272);
        dataGridView1.TabIndex = 0;
        dataGridView1.Text = "dataGridView1";
        dataGridView1.CellClick += dataGridView1_CellClick;
        // 
        // dataGridView2
        // 
        dataGridView2.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        dataGridView2.Location = new System.Drawing.Point(49, 473);
        dataGridView2.Name = "dataGridView2";
        dataGridView2.RowHeadersWidth = 62;
        dataGridView2.Size = new System.Drawing.Size(816, 281);
        dataGridView2.TabIndex = 1;
        dataGridView2.Text = "dataGridView2";
        dataGridView2.CellClick += dataGridView2_CellClick;
        // 
        // label1
        // 
        label1.Font = new System.Drawing.Font("Segoe UI", 14F, System.Drawing.FontStyle.Bold);
        label1.Location = new System.Drawing.Point(240, 32);
        label1.Name = "label1";
        label1.Size = new System.Drawing.Size(171, 35);
        label1.TabIndex = 2;
        label1.Text = "Sporturi";
        label1.Click += label1_Click;
        // 
        // label2
        // 
        label2.Font = new System.Drawing.Font("Segoe UI", 14F, System.Drawing.FontStyle.Bold);
        label2.Location = new System.Drawing.Point(377, 412);
        label2.Name = "label2";
        label2.Size = new System.Drawing.Size(138, 43);
        label2.TabIndex = 3;
        label2.Text = "Echipe";
        // 
        // button1
        // 
        button1.Location = new System.Drawing.Point(678, 274);
        button1.Name = "button1";
        button1.Size = new System.Drawing.Size(131, 40);
        button1.TabIndex = 4;
        button1.Text = "Adauga";
        button1.UseVisualStyleBackColor = true;
        button1.Click += button1_Click;
        // 
        // button2
        // 
        button2.Location = new System.Drawing.Point(678, 320);
        button2.Name = "button2";
        button2.Size = new System.Drawing.Size(131, 40);
        button2.TabIndex = 5;
        button2.Text = "Sterge";
        button2.UseVisualStyleBackColor = true;
        button2.Click += button2_Click;
        // 
        // button3
        // 
        button3.Location = new System.Drawing.Point(678, 366);
        button3.Name = "button3";
        button3.Size = new System.Drawing.Size(131, 40);
        button3.TabIndex = 6;
        button3.Text = "Modifica";
        button3.UseVisualStyleBackColor = true;
        button3.Click += button3_Click;
        // 
        // comboBoxAntrenor
        // 
        comboBoxAntrenor.FormattingEnabled = true;
        comboBoxAntrenor.Location = new System.Drawing.Point(678, 185);
        comboBoxAntrenor.Name = "comboBoxAntrenor";
        comboBoxAntrenor.Size = new System.Drawing.Size(210, 33);
        comboBoxAntrenor.TabIndex = 7;
        // 
        // textBoxTara
        // 
        textBoxTara.AccessibleName = "";
        textBoxTara.Location = new System.Drawing.Point(678, 139);
        textBoxTara.Name = "textBoxTara";
        textBoxTara.Size = new System.Drawing.Size(210, 31);
        textBoxTara.TabIndex = 8;
        // 
        // textBoxNume
        // 
        textBoxNume.Location = new System.Drawing.Point(678, 92);
        textBoxNume.Name = "textBoxNume";
        textBoxNume.Size = new System.Drawing.Size(210, 31);
        textBoxNume.TabIndex = 9;
        // 
        // label3
        // 
        label3.Location = new System.Drawing.Point(585, 96);
        label3.Name = "label3";
        label3.Size = new System.Drawing.Size(87, 23);
        label3.TabIndex = 10;
        label3.Text = "Nume";
        label3.Click += label3_Click;
        // 
        // label4
        // 
        label4.Location = new System.Drawing.Point(585, 144);
        label4.Name = "label4";
        label4.Size = new System.Drawing.Size(87, 23);
        label4.TabIndex = 11;
        label4.Text = "Tara";
        // 
        // label5
        // 
        label5.Location = new System.Drawing.Point(588, 190);
        label5.Name = "label5";
        label5.Size = new System.Drawing.Size(84, 23);
        label5.TabIndex = 12;
        label5.Text = "Antrenor";
        // 
        // Form1
        // 
        AutoScaleDimensions = new System.Drawing.SizeF(10F, 25F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        ClientSize = new System.Drawing.Size(910, 792);
        Controls.Add(label5);
        Controls.Add(label4);
        Controls.Add(label3);
        Controls.Add(textBoxNume);
        Controls.Add(textBoxTara);
        Controls.Add(comboBoxAntrenor);
        Controls.Add(button3);
        Controls.Add(button2);
        Controls.Add(button1);
        Controls.Add(label2);
        Controls.Add(label1);
        Controls.Add(dataGridView2);
        Controls.Add(dataGridView1);
        Text = "Form1";
        ((System.ComponentModel.ISupportInitialize)dataGridView1).EndInit();
        ((System.ComponentModel.ISupportInitialize)dataGridView2).EndInit();
        ResumeLayout(false);
        PerformLayout();
    }

    private System.Windows.Forms.Label label3;
    private System.Windows.Forms.Label label4;
    private System.Windows.Forms.Label label5;

    private System.Windows.Forms.TextBox textBoxNume;

    private System.Windows.Forms.TextBox textBoxTara;

    private System.Windows.Forms.ComboBox comboBoxAntrenor;

    private System.Windows.Forms.Button button1;
    private System.Windows.Forms.Button button2;
    private System.Windows.Forms.Button button3;

    private System.Windows.Forms.Label label1;
    private System.Windows.Forms.Label label2;

    private System.Windows.Forms.DataGridView dataGridView2;

    private System.Windows.Forms.DataGridView dataGridView1;

    #endregion
}