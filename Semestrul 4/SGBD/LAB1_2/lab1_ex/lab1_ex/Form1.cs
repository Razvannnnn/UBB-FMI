using System.Data;

namespace lab1_ex;
using Microsoft.Data.SqlClient;

public partial class Form1 : Form
{
    private string connectionString = @"Server=ROG_RAZVAN\SQLEXPRESS;Database=Lab1224-2SGBD2025;
    Integrated Security=True;TrustServerCertificate=true;";

    private DataSet ds = new DataSet();
    private SqlDataAdapter adapter = new SqlDataAdapter();
        
    public Form1()
    {
        InitializeComponent();
    }

    private void label1_Click(object sender, EventArgs e)
    {
        throw new System.NotImplementedException();
    }
    
    private void Form1_Load_1(object sender, EventArgs e)
    {
        try
        {
            using (SqlConnection con = new SqlConnection(connectionString))
            {
                con.Open();
                label2.Text = con.State.ToString();
                adapter.SelectCommand = new SqlCommand("SELECT * FROM Motociclete", con);
                adapter.Fill(ds, "Motociclete");
                dataGridView1.DataSource = ds.Tables["Motociclete"];
                con.Close();
            }
        }
        catch (Exception ex)
        {   
            MessageBox.Show(ex.Message);
        }
    }

    private void label2_Click(object sender, EventArgs e)
    {
        throw new System.NotImplementedException();
    }
}