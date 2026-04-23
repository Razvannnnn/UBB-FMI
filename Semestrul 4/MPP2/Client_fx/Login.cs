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
                User user = _service.Login(username, password);
                if (user != null)
                {
                    // Hide the login form
                    this.Hide();

                    // Open the main form
                    var mainForm = new Form1.Form1();
                    mainForm.SetService(_service, user);
                    mainForm.FormClosed += MainForm_FormClosed;

                    mainForm.Show();
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
            MessageBox.Show("Authentication failure: Wrong username or password", "Login Failed", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }

        private void MainForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            // Optional: Try logout on close
            try
            {
                if (_currentUser != null)
                {
                    _service.Logout(_currentUser);
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error logging out: " + ex.Message);
            }

            // After the main window closes, close the login form too
            this.Close();
        }

        private void label1_Click(object sender, EventArgs e)
        {
            // No need to implement if unused
        }
    }
}
