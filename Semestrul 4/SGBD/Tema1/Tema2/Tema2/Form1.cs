using System;
using System.Configuration;
using System.Data;
using System.Windows.Forms;
using Microsoft.Data.SqlClient;

namespace Tema2 
{
    public partial class Form1 : Form
    {
        private static string connectionString = ConfigurationManager.ConnectionStrings["connectionString"].ConnectionString;
        private SqlConnection connection = new SqlConnection(connectionString);
        
        static string parentName = ConfigurationManager.AppSettings["ParentTableName"];
        static string childName = ConfigurationManager.AppSettings["ChildTableName"];
        static int childNumberOfColumns = int.Parse(Configuratio        private SqlDataAdapter adapter = new SqlDataAdapter();
nManager.AppSettings["ChildNumberOfColumns"]);
        static string insertQuery = ConfigurationManager.AppSettings["ChildInsertQUERY"];
        static string deleteQuery = ConfigurationManager.AppSettings["ChildDeleteQUERY"];
        static string updateQuery = ConfigurationManager.AppSettings["ChildUpdateQUERY"];
        static string childArr = ConfigurationManager.AppSettings["ChildArr"];
        static string childColumnNames = ConfigurationManager.AppSettings["ChildColumnNames"];
        static string childColumnTypes = ConfigurationManager.AppSettings["ChildColumnTypes"];
        static string childToParentID = ConfigurationManager.AppSettings["ChildToParentID"];

        TextBox[] textBoxes = new TextBox[childNumberOfColumns];
        Label[] labels = new Label[childNumberOfColumns];

        DataSet dsP = new DataSet();
        DataSet dsC = new DataSet();
        
        public Form1()
        {
            InitializeComponent();
            string[] names = childColumnNames.Split(", ");

            for (int i = 0; i < childNumberOfColumns; i++)
            {
                labels[i] = new Label();
                textBoxes[i] = new TextBox();

                labels[i].Text = names[i];
                labels[i].Location = new Point(i * 200 + 50, 20);
                labels[i].Size = new Size(175, 20);
                textBoxes[i].Text = "";
                textBoxes[i].Location = new Point(i * 200 + 50, 50);
                textBoxes[i].Size = new Size(175, 20);
            }
        }
        
        private void Form1_Load(object sender, EventArgs e)
        {
            labelParent.Text = parentName;
            labelChild.Text = childName;
            
            for (int i = 0; i < childNumberOfColumns; i++)
            {
                panel1.Controls.Add(labels[i]);
                panel1.Controls.Add(textBoxes[i]);
            }
            
            adapter.SelectCommand = new SqlCommand("SELECT * FROM " + parentName, connection);
            dsP.Clear();
            adapter.Fill(dsP);
            dataGridViewParent.DataSource = dsP.Tables[0];
        }
        
        private void dataGridViewParent_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (dataGridViewParent.Rows[e.RowIndex].Cells[e.ColumnIndex].Value == null)
                return;

            string parentId = dataGridViewParent.Rows[e.RowIndex].Cells[0].Value.ToString();

            adapter.SelectCommand = new SqlCommand("SELECT * FROM " + childName +
                                              " WHERE " + childToParentID + " = " + parentId, connection);
            dsC.Clear();
            adapter.Fill(dsC);
            dataGridViewChild.DataSource = dsC.Tables[0];
        }
        
        private void buttonAdd_Click(object sender, EventArgs e)
        {
            adapter.InsertCommand = new SqlCommand(insertQuery, connection);

            adapter.InsertCommand.Parameters.Add("@id", SqlDbType.Int).Value =
                dsP.Tables[0].Rows[dataGridViewParent.CurrentCell.RowIndex][0];

            string[] args = childArr.Split(", ");
            string[] types = childColumnTypes.Split(", ");

            try
            {
                for (int i = 0; i < childNumberOfColumns; i++)
                { 
                    switch (types[i])
                    {
                        case "string":
                            adapter.InsertCommand.Parameters.Add(args[i], SqlDbType.VarChar).Value = textBoxes[i].Text;
                            break;
                        case "int":
                            adapter.InsertCommand.Parameters.Add(args[i], SqlDbType.Int).Value = int.Parse(textBoxes[i].Text);
                            break;
                        case "time":
                            adapter.InsertCommand.Parameters.Add(args[i], SqlDbType.Time).Value = TimeSpan.Parse(textBoxes[i].Text);
                            break;
                        default:
                            MessageBox.Show("Tip necunoscut!");
                            return;
                    }
                }

                connection.Open();
                adapter.InsertCommand.ExecuteNonQuery();
                connection.Close();
                dsC.Clear();
                adapter.Fill(dsC);
            }
            catch
            {
                connection.Close();
                MessageBox.Show("Input gresit");
            }
        }

        private void buttonDelete_Click(object sender, EventArgs e)
        {
            if (dataGridViewChild.SelectedCells.Count != 1)
            {
                MessageBox.Show("Selectati o singura linie in copil");
                return;
            }

            adapter.DeleteCommand = new SqlCommand(deleteQuery, connection);
            adapter.DeleteCommand.Parameters.Add("@id", SqlDbType.Int).Value =
                dsC.Tables[0].Rows[dataGridViewChild.CurrentCell.RowIndex][0];

            connection.Open();
            adapter.DeleteCommand.ExecuteNonQuery();
            connection.Close();
            dsC.Clear();
            adapter.Fill(dsC);
        }

        private void buttonUpdate_Click(object sender, EventArgs e)
        {
            if (dataGridViewChild.SelectedCells.Count != 1)
            {
                MessageBox.Show("Selectati o singura linie!");
                return;
            }

            adapter.UpdateCommand = new SqlCommand(updateQuery, connection);
            adapter.UpdateCommand.Parameters.Add("@id", SqlDbType.Int).Value =
                dsC.Tables[0].Rows[dataGridViewChild.CurrentCell.RowIndex][0];

            string[] args = childArr.Split(", ");
            string[] types = childColumnTypes.Split(", ");

            try
            {
                for (int i = 0; i < childNumberOfColumns; i++)
                {
                    Console.WriteLine(args[i] + " " + textBoxes[i].Text);

                    switch (types[i])
                    {
                        case "string":
                            adapter.UpdateCommand.Parameters.Add(args[i], SqlDbType.VarChar).Value = textBoxes[i].Text;
                            break;
                        case "int":
                            adapter.UpdateCommand.Parameters.Add(args[i], SqlDbType.Int).Value = int.Parse(textBoxes[i].Text);
                            break;
                        case "float":
                            adapter.UpdateCommand.Parameters.Add(args[i], SqlDbType.Float).Value = float.Parse(textBoxes[i].Text);
                            break;
                        case "time":
                            adapter.UpdateCommand.Parameters.Add(args[i], SqlDbType.Time).Value = TimeSpan.Parse(textBoxes[i].Text);
                            break;
                    }
                }

                connection.Open();
                int x = adapter.UpdateCommand.ExecuteNonQuery();
                connection.Close();
                dsC.Clear();
                adapter.Fill(dsC);

                if (x >= 1)
                    MessageBox.Show("Modificare actualizata!");
            }
            catch
            {
                connection.Close();
                MessageBox.Show("Input greșit!");
            }
        }
        
        private void label1_Click(object sender, EventArgs e)
        {
            // Do nothing
        }
        
        private void label3_Click(object sender, EventArgs e)
        {
            // Do nothing
        }

        private void dataGridView2_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            throw new System.NotImplementedException();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            throw new System.NotImplementedException();
        }

        private void button2Click(object sender, EventArgs e)
        {
            throw new System.NotImplementedException();
        }
    }
}
