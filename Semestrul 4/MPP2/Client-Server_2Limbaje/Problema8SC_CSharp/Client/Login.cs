using System;
using System.Windows.Forms;
using Problema8SC_CSharp.Model;
using Problema8SC_CSharp.Services;

namespace Login
{
    public partial class Login : Form
    {
        private IServices _service;
        private User _currentUser;

        public Login(IServices service)
        {
            InitializeComponent();
            _service = service;
        }

        private void buttonLogin_Click(object sender, EventArgs e)
        {
            string username = textBoxUsername.Text;
            string password = textBoxPassword.Text;
            _currentUser = new User(username, password);

            try
            {
                var form = new Form1.Form1();
                _service.Login(_currentUser, form);
                if (_currentUser != null)
                {
                    form.Closed += (s, args) => this.Close();
                    form.SetService(_service, _currentUser);
                    form.Show();
                    this.Hide();
                    //MessageBox.Show("Login successful");
                }
                else
                {
                    ShowLoginFailed();
                }
            }
            catch (Exception ex)
            {
                ShowLoginFailed();
            }
        }

        private void ShowLoginFailed()
        {
            MessageBox.Show("Authentication failure", "Login Failed", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }

        private void label1_Click(object sender, EventArgs e)
        {
            // No need to implement if unused
        }
    }
}
