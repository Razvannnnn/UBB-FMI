using System.ComponentModel;

namespace Problema8_FX_Csharp;

partial class Login
{
    /// <summary>
    /// Required designer variable.
    /// </summary>
    private IContainer components = null;

    /// <summary>
    /// Clean up any resources being used.
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
        textBoxUsername = new System.Windows.Forms.TextBox();
        textBoxPassword = new System.Windows.Forms.TextBox();
        button1 = new System.Windows.Forms.Button();
        label2 = new System.Windows.Forms.Label();
        label3 = new System.Windows.Forms.Label();
        SuspendLayout();
        // 
        // label1
        // 
        label1.Font = new System.Drawing.Font("Segoe UI", 16F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)238));
        label1.Location = new System.Drawing.Point(71, 50);
        label1.Name = "label1";
        label1.Size = new System.Drawing.Size(367, 52);
        label1.TabIndex = 0;
        label1.Text = "Sistem Concurs Atletism";
        label1.Click += label1_Click;
        // 
        // textBoxUsername
        // 
        textBoxUsername.AccessibleName = "";
        textBoxUsername.Location = new System.Drawing.Point(167, 162);
        textBoxUsername.Name = "textBoxUsername";
        textBoxUsername.Size = new System.Drawing.Size(242, 31);
        textBoxUsername.TabIndex = 1;
        // 
        // textBoxPassword
        // 
        textBoxPassword.AccessibleName = "";
        textBoxPassword.Location = new System.Drawing.Point(167, 219);
        textBoxPassword.Name = "textBoxPassword";
        textBoxPassword.PasswordChar = '*';
        textBoxPassword.Size = new System.Drawing.Size(242, 31);
        textBoxPassword.TabIndex = 2;
        // 
        // button1
        // 
        button1.Location = new System.Drawing.Point(193, 334);
        button1.Name = "button1";
        button1.Size = new System.Drawing.Size(105, 45);
        button1.TabIndex = 3;
        button1.Text = "Login";
        button1.UseVisualStyleBackColor = true;
        button1.MouseClick += button1_MouseClick;
        // 
        // label2
        // 
        label2.Location = new System.Drawing.Point(76, 163);
        label2.Name = "label2";
        label2.Size = new System.Drawing.Size(91, 29);
        label2.TabIndex = 4;
        label2.Text = "Username";
        // 
        // label3
        // 
        label3.Location = new System.Drawing.Point(79, 221);
        label3.Name = "label3";
        label3.Size = new System.Drawing.Size(88, 29);
        label3.TabIndex = 5;
        label3.Text = "Password";
        // 
        // Login
        // 
        AutoScaleDimensions = new System.Drawing.SizeF(10F, 25F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        ClientSize = new System.Drawing.Size(502, 450);
        Controls.Add(label3);
        Controls.Add(label2);
        Controls.Add(button1);
        Controls.Add(textBoxPassword);
        Controls.Add(textBoxUsername);
        Controls.Add(label1);
        Text = "Login";
        ResumeLayout(false);
        PerformLayout();
    }

    private System.Windows.Forms.Label label2;
    private System.Windows.Forms.Label label3;

    private System.Windows.Forms.Button button1;

    private System.Windows.Forms.TextBox textBoxUsername;
    private System.Windows.Forms.TextBox textBoxPassword;

    private System.Windows.Forms.Label label1;

    #endregion
}