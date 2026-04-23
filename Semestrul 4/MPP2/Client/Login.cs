using System.Data;
using System.Data.SqlClient;
using System.Data.SQLite;

using Problema8SC_CSharp.Model;

namespace Login;

public partial class Login : Form
{
    private Service.Service _service;
    
    public Login(Service.Service service)
    {
        InitializeComponent();
        _service = service;
    }

    private void label1_Click(object sender, EventArgs e)
    {
        throw new System.NotImplementedException();
    }

    private void button1_MouseClick(object sender, MouseEventArgs e)
    {
        try
        {
            string username = textBoxUsername.Text;
            string password = textBoxPassword.Text;
            User user = _service.Login(username, password);
            if (user != null)
            {
                this.Hide();
                var form = new Form1();
                form.Closed += (s, args) => this.Close();
                form.SetService(_service);
                form.Show();
                MessageBox.Show("Login successful");
            }
            else
            {
                MessageBox.Show("Login failed");
            }
        } catch (Exception ex)
        {
            MessageBox.Show("Login failed");
            Application.Exit();

        }
    }
}