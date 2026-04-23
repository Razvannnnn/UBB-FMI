namespace Form1;

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
        tabControl1 = new System.Windows.Forms.TabControl();
        tabPage1 = new System.Windows.Forms.TabPage();
        comboBoxProbe = new System.Windows.Forms.ComboBox();
        comboBoxGrupeVarsta = new System.Windows.Forms.ComboBox();
        buttonLogout = new System.Windows.Forms.Button();
        dataGridView1 = new System.Windows.Forms.DataGridView();
        tabPage2 = new System.Windows.Forms.TabPage();
        label6 = new System.Windows.Forms.Label();
        label5 = new System.Windows.Forms.Label();
        label4 = new System.Windows.Forms.Label();
        label3 = new System.Windows.Forms.Label();
        label2 = new System.Windows.Forms.Label();
        buttonInscrie = new System.Windows.Forms.Button();
        comboBoxProba2 = new System.Windows.Forms.ComboBox();
        comboBoxProba1 = new System.Windows.Forms.ComboBox();
        comboBoxVarsta = new System.Windows.Forms.ComboBox();
        textBox2 = new System.Windows.Forms.TextBox();
        textBox1 = new System.Windows.Forms.TextBox();
        label1 = new System.Windows.Forms.Label();
        dataGridView2 = new System.Windows.Forms.DataGridView();
        tabControl1.SuspendLayout();
        tabPage1.SuspendLayout();
        ((System.ComponentModel.ISupportInitialize)dataGridView1).BeginInit();
        tabPage2.SuspendLayout();
        ((System.ComponentModel.ISupportInitialize)dataGridView2).BeginInit();
        SuspendLayout();
        // 
        // tabControl1
        // 
        tabControl1.Controls.Add(tabPage1);
        tabControl1.Controls.Add(tabPage2);
        tabControl1.Location = new System.Drawing.Point(1, 1);
        tabControl1.Name = "tabControl1";
        tabControl1.SelectedIndex = 0;
        tabControl1.Size = new System.Drawing.Size(1044, 654);
        tabControl1.TabIndex = 0;
        // 
        // tabPage1
        // 
        tabPage1.Controls.Add(dataGridView2);
        tabPage1.Controls.Add(comboBoxProbe);
        tabPage1.Controls.Add(comboBoxGrupeVarsta);
        tabPage1.Controls.Add(buttonLogout);
        tabPage1.Controls.Add(dataGridView1);
        tabPage1.Location = new System.Drawing.Point(4, 34);
        tabPage1.Name = "tabPage1";
        tabPage1.Padding = new System.Windows.Forms.Padding(3);
        tabPage1.Size = new System.Drawing.Size(1036, 616);
        tabPage1.TabIndex = 0;
        tabPage1.Text = "Menu";
        tabPage1.UseVisualStyleBackColor = true;
        // 
        // comboBoxProbe
        // 
        comboBoxProbe.FormattingEnabled = true;
        comboBoxProbe.Location = new System.Drawing.Point(374, 35);
        comboBoxProbe.Name = "comboBoxProbe";
        comboBoxProbe.Size = new System.Drawing.Size(282, 33);
        comboBoxProbe.TabIndex = 3;
        comboBoxProbe.Text = "Probe";
        // 
        // comboBoxGrupeVarsta
        // 
        comboBoxGrupeVarsta.FormattingEnabled = true;
        comboBoxGrupeVarsta.Location = new System.Drawing.Point(29, 35);
        comboBoxGrupeVarsta.Name = "comboBoxGrupeVarsta";
        comboBoxGrupeVarsta.Size = new System.Drawing.Size(274, 33);
        comboBoxGrupeVarsta.TabIndex = 2;
        comboBoxGrupeVarsta.Text = "Grupe Varsta";
        // 
        // buttonLogout
        // 
        buttonLogout.Location = new System.Drawing.Point(837, 35);
        buttonLogout.Name = "buttonLogout";
        buttonLogout.Size = new System.Drawing.Size(162, 33);
        buttonLogout.TabIndex = 1;
        buttonLogout.Text = "Log out";
        buttonLogout.UseVisualStyleBackColor = true;
        buttonLogout.Click += button1_Click;
        // 
        // dataGridView1
        // 
        dataGridView1.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        dataGridView1.Location = new System.Drawing.Point(24, 109);
        dataGridView1.Name = "dataGridView1";
        dataGridView1.RowHeadersWidth = 62;
        dataGridView1.Size = new System.Drawing.Size(384, 473);
        dataGridView1.TabIndex = 0;
        dataGridView1.Text = "dataGridView1";
        // 
        // tabPage2
        // 
        tabPage2.Controls.Add(label6);
        tabPage2.Controls.Add(label5);
        tabPage2.Controls.Add(label4);
        tabPage2.Controls.Add(label3);
        tabPage2.Controls.Add(label2);
        tabPage2.Controls.Add(buttonInscrie);
        tabPage2.Controls.Add(comboBoxProba2);
        tabPage2.Controls.Add(comboBoxProba1);
        tabPage2.Controls.Add(comboBoxVarsta);
        tabPage2.Controls.Add(textBox2);
        tabPage2.Controls.Add(textBox1);
        tabPage2.Controls.Add(label1);
        tabPage2.Location = new System.Drawing.Point(4, 34);
        tabPage2.Name = "tabPage2";
        tabPage2.Padding = new System.Windows.Forms.Padding(3);
        tabPage2.Size = new System.Drawing.Size(1036, 616);
        tabPage2.TabIndex = 1;
        tabPage2.Text = "Inscriere";
        tabPage2.UseVisualStyleBackColor = true;
        // 
        // label6
        // 
        label6.Location = new System.Drawing.Point(586, 416);
        label6.Name = "label6";
        label6.Size = new System.Drawing.Size(76, 32);
        label6.TabIndex = 11;
        label6.Text = "Proba 2";
        // 
        // label5
        // 
        label5.Location = new System.Drawing.Point(361, 413);
        label5.Name = "label5";
        label5.Size = new System.Drawing.Size(75, 35);
        label5.TabIndex = 10;
        label5.Text = "Proba 1";
        // 
        // label4
        // 
        label4.Location = new System.Drawing.Point(447, 322);
        label4.Name = "label4";
        label4.Size = new System.Drawing.Size(113, 35);
        label4.TabIndex = 9;
        label4.Text = "Grupa Varsta";
        // 
        // label3
        // 
        label3.Location = new System.Drawing.Point(310, 231);
        label3.Name = "label3";
        label3.Size = new System.Drawing.Size(73, 29);
        label3.TabIndex = 8;
        label3.Text = "CNP";
        // 
        // label2
        // 
        label2.Location = new System.Drawing.Point(307, 152);
        label2.Name = "label2";
        label2.Size = new System.Drawing.Size(76, 34);
        label2.TabIndex = 7;
        label2.Text = "Nume";
        // 
        // buttonInscrie
        // 
        buttonInscrie.Location = new System.Drawing.Point(447, 515);
        buttonInscrie.Name = "buttonInscrie";
        buttonInscrie.Size = new System.Drawing.Size(123, 38);
        buttonInscrie.TabIndex = 6;
        buttonInscrie.Text = "Inscrie";
        buttonInscrie.UseVisualStyleBackColor = true;
        buttonInscrie.Click += buttonInscriere_Click;
        // 
        // comboBoxProba2
        // 
        comboBoxProba2.FormattingEnabled = true;
        comboBoxProba2.Location = new System.Drawing.Point(531, 448);
        comboBoxProba2.Name = "comboBoxProba2";
        comboBoxProba2.Size = new System.Drawing.Size(179, 33);
        comboBoxProba2.TabIndex = 5;
        // 
        // comboBoxProba1
        // 
        comboBoxProba1.FormattingEnabled = true;
        comboBoxProba1.Location = new System.Drawing.Point(307, 448);
        comboBoxProba1.Name = "comboBoxProba1";
        comboBoxProba1.Size = new System.Drawing.Size(179, 33);
        comboBoxProba1.TabIndex = 4;
        // 
        // comboBoxVarsta
        // 
        comboBoxVarsta.FormattingEnabled = true;
        comboBoxVarsta.Location = new System.Drawing.Point(410, 360);
        comboBoxVarsta.Name = "comboBoxVarsta";
        comboBoxVarsta.Size = new System.Drawing.Size(195, 33);
        comboBoxVarsta.TabIndex = 3;
        // 
        // textBox2
        // 
        textBox2.Location = new System.Drawing.Point(309, 260);
        textBox2.Name = "textBox2";
        textBox2.Size = new System.Drawing.Size(387, 31);
        textBox2.TabIndex = 2;
        // 
        // textBox1
        // 
        textBox1.Location = new System.Drawing.Point(307, 189);
        textBox1.Name = "textBox1";
        textBox1.Size = new System.Drawing.Size(390, 31);
        textBox1.TabIndex = 1;
        // 
        // label1
        // 
        label1.Font = new System.Drawing.Font("Segoe UI", 20F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)238));
        label1.Location = new System.Drawing.Point(334, 64);
        label1.Name = "label1";
        label1.Size = new System.Drawing.Size(340, 79);
        label1.TabIndex = 0;
        label1.Text = "Inscrie participant";
        label1.Click += label1_Click;
        // 
        // dataGridView2
        // 
        dataGridView2.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        dataGridView2.Location = new System.Drawing.Point(443, 110);
        dataGridView2.Name = "dataGridView2";
        dataGridView2.RowHeadersWidth = 62;
        dataGridView2.Size = new System.Drawing.Size(564, 471);
        dataGridView2.TabIndex = 4;
        dataGridView2.Text = "dataGridView2";
        // 
        // Form1
        // 
        AutoScaleDimensions = new System.Drawing.SizeF(10F, 25F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        ClientSize = new System.Drawing.Size(1044, 652);
        Controls.Add(tabControl1);
        Text = "Form1";
        tabControl1.ResumeLayout(false);
        tabPage1.ResumeLayout(false);
        ((System.ComponentModel.ISupportInitialize)dataGridView1).EndInit();
        tabPage2.ResumeLayout(false);
        tabPage2.PerformLayout();
        ((System.ComponentModel.ISupportInitialize)dataGridView2).EndInit();
        ResumeLayout(false);
    }

    private System.Windows.Forms.DataGridView dataGridView2;

    private System.Windows.Forms.Label label2;
    private System.Windows.Forms.Label label3;
    private System.Windows.Forms.Label label4;
    private System.Windows.Forms.Label label5;
    private System.Windows.Forms.Label label6;

    private System.Windows.Forms.TextBox textBox1;
    private System.Windows.Forms.TextBox textBox2;
    private System.Windows.Forms.ComboBox comboBoxVarsta;
    private System.Windows.Forms.ComboBox comboBoxProba1;
    private System.Windows.Forms.ComboBox comboBoxProba2;
    private System.Windows.Forms.Button buttonInscrie;

    private System.Windows.Forms.Label label1;

    private System.Windows.Forms.ComboBox comboBoxGrupeVarsta;
    private System.Windows.Forms.ComboBox comboBoxProbe;

    private System.Windows.Forms.Button buttonLogout;

    private System.Windows.Forms.DataGridView dataGridView1;

    private System.Windows.Forms.TabControl tabControl1;
    private System.Windows.Forms.TabPage tabPage1;
    private System.Windows.Forms.TabPage tabPage2;

    #endregion
}