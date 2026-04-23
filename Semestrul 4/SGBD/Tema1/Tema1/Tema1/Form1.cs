namespace Tema1;

using System.Data;
using Microsoft.Data.SqlClient;

public partial class Form1 : Form
{
    private static string connectionString = @"Server=ROG_RAZVAN\SQLEXPRESS;Database=BazaDateSGBD;
    Integrated Security=True;TrustServerCertificate=true;";

    private DataSet ds = new DataSet();
    private SqlDataAdapter adapter = new SqlDataAdapter();

    private SqlConnection connection = new SqlConnection(connectionString);

    public Form1()
    {
        InitializeComponent();
        LoadSporturi();
        populateComboBoxAntrenori();
    }

    public void LoadSporturi()
    {
        string query = "SELECT * FROM Sporturi";
        SqlDataAdapter adapter = new SqlDataAdapter(query, connection);
        DataSet dataSet = new DataSet();
        adapter.Fill(dataSet, "Sporturi");
        dataGridView1.DataSource = dataSet.Tables["Sporturi"];
    }

    public void LoadEchipe(int idSport)
    {
        string query = "SELECT * FROM Echipe WHERE id_sport = @id_sport";
        SqlDataAdapter adapter = new SqlDataAdapter(query, connection);
        adapter.SelectCommand.Parameters.AddWithValue("@id_sport", idSport);
        DataSet dataSet = new DataSet();
        adapter.Fill(dataSet, "Echipe");
        dataGridView2.DataSource = dataSet.Tables["Echipe"];
    }

    private void dataGridView1_CellClick(object sender, DataGridViewCellEventArgs e)
    {
        if (e.RowIndex >= 0)
        {
            int idSport = Convert.ToInt32(dataGridView1.Rows[e.RowIndex].Cells["id_sport"].Value);
            LoadEchipe(idSport);
        }
    }

    private void dataGridView2_CellClick(object sender, DataGridViewCellEventArgs e)
    {
        if (e.RowIndex >= 0)
        {
            textBoxNume.Text = dataGridView2.Rows[e.RowIndex].Cells["nume"].Value.ToString();
            textBoxTara.Text = dataGridView2.Rows[e.RowIndex].Cells["tara"].Value.ToString();

            int idAntrenor = Convert.ToInt32(dataGridView2.Rows[e.RowIndex].Cells["id_antrenor"].Value);
            comboBoxAntrenor.SelectedValue = idAntrenor;
        }
    }


    public void DeleteEchipa(int idEchipa)
    {
        string query = "DELETE FROM Echipe WHERE id_echipa = @id_echipa";
        SqlCommand command = new SqlCommand(query, connection);
        command.Parameters.AddWithValue("@id_echipa", idEchipa);
        connection.Open();
        command.ExecuteNonQuery();
        connection.Close();
    }

    public void UpdateEchipa(int idEchipa, string nume, string tara, int idAntrenor)
    {
        string query = "UPDATE Echipe SET nume = @nume, tara = @tara, id_antrenor = @id_antrenor WHERE id_echipa = @id_echipa";
        SqlCommand command = new SqlCommand(query, connection);
        command.Parameters.AddWithValue("@nume", nume);
        command.Parameters.AddWithValue("@tara", tara);
        command.Parameters.AddWithValue("@id_antrenor", idAntrenor);
        command.Parameters.AddWithValue("@id_echipa", idEchipa);
        connection.Open();
        command.ExecuteNonQuery();
        connection.Close();
    }

    public void AddEchipa(int idSport, int idAntrenor, string nume, string tara)
    {
        string query =
            "INSERT INTO Echipe (nume, tara, id_antrenor, id_sport) VALUES (@nume, @tara, @id_antrenor, @id_sport)";
        SqlCommand command = new SqlCommand(query, connection);
        command.Parameters.AddWithValue("@nume", nume);
        command.Parameters.AddWithValue("@tara", tara);
        command.Parameters.AddWithValue("@id_antrenor", idAntrenor);
        command.Parameters.AddWithValue("@id_sport", idSport);
        connection.Open();
        command.ExecuteNonQuery();
        connection.Close();
    }

    private void button1_Click(object sender, EventArgs e)
    {
        try
        {
            String nume = textBoxNume.Text;
            String tara = textBoxTara.Text;
            int idAntrenor = Convert.ToInt32(comboBoxAntrenor.SelectedValue);
            int idSport =
                Convert.ToInt32(dataGridView1.Rows[dataGridView1.CurrentCell.RowIndex].Cells["id_sport"].Value);

            if (dataGridView1.SelectedRows.Count == 0)
            {
                MessageBox.Show("Selectati un sport", "Eroare", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            if (nume == "" || tara == "" || idAntrenor == 0)
            {
                MessageBox.Show("Completati toate campurile", "Eroare", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            AddEchipa(idSport, idAntrenor, nume, tara);
            MessageBox.Show("Echipa a fost adaugata", "Succes", MessageBoxButtons.OK, MessageBoxIcon.Information);
            LoadEchipe(idSport);
        }
        catch (Exception ex)
        {
            MessageBox.Show("A aparut o eroare", "Eroare", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
        
        textBoxNume.Clear();
        textBoxTara.Clear();
        comboBoxAntrenor.SelectedIndex = 0;
    }

    private void button2_Click(object sender, EventArgs e)
    {
        try
        {
            if (dataGridView2.SelectedRows.Count == 0)
            {
                MessageBox.Show("Selectati o echipa", "Eroare", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            int id_echipa =
                Convert.ToInt32(dataGridView2.Rows[dataGridView2.CurrentCell.RowIndex].Cells["id_echipa"].Value);
            int id_sport =
                Convert.ToInt32(dataGridView1.Rows[dataGridView1.CurrentCell.RowIndex].Cells["id_sport"].Value);
            DeleteEchipa(id_echipa);
            MessageBox.Show("Echipa a fost stearsa", "Succes", MessageBoxButtons.OK, MessageBoxIcon.Information);
            LoadEchipe(id_sport);
        }
        catch (Exception ex)
        {
            MessageBox.Show("A aparut o eroare", "Eroare", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
        
        textBoxNume.Clear();
        textBoxTara.Clear();
        comboBoxAntrenor.SelectedIndex = 0;
    }

    private void button3_Click(object sender, EventArgs e)
    {
        try
        {
            String nume = textBoxNume.Text;
            String tara = textBoxTara.Text;
            int idAntrenor = Convert.ToInt32(comboBoxAntrenor.SelectedValue);
            int idSport =
                Convert.ToInt32(dataGridView1.Rows[dataGridView1.CurrentCell.RowIndex].Cells["id_sport"].Value);

            if (dataGridView2.SelectedRows.Count == 0)
            {
                MessageBox.Show("Selectati o echipa", "Eroare", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            if (nume == "" || tara == "" || idAntrenor == 0)
            {
                MessageBox.Show("Completati toate campurile", "Eroare", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            int idEchipa =
                Convert.ToInt32(dataGridView2.Rows[dataGridView2.CurrentCell.RowIndex].Cells["id_echipa"].Value);
            UpdateEchipa(idEchipa, nume, tara, idAntrenor);
            MessageBox.Show("Echipa a fost actualizata", "Succes", MessageBoxButtons.OK, MessageBoxIcon.Information);
            LoadEchipe(idSport);
        }
        catch (Exception ex)
        {
            MessageBox.Show("A aparut o eroare", "Eroare", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
        
        textBoxNume.Clear();
        textBoxTara.Clear();
        comboBoxAntrenor.SelectedIndex = 0;
    }

    public void populateComboBoxAntrenori()
    {
        string query = "SELECT * FROM Antrenori";
        SqlDataAdapter adapter = new SqlDataAdapter(query, connection);
        DataSet dataSet = new DataSet();
        adapter.Fill(dataSet, "Antrenori");
        comboBoxAntrenor.DataSource = dataSet.Tables["Antrenori"];
        comboBoxAntrenor.DisplayMember = "nume";
        comboBoxAntrenor.ValueMember = "id_antrenor";
    }

    private void label1_Click(object sender, EventArgs e)
    {
        throw new System.NotImplementedException();
    }

    private void label3_Click(object sender, EventArgs e)
    {
        throw new System.NotImplementedException();
    }
}
