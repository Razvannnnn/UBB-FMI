using System.ComponentModel;

using Problema8SC_CSharp.Model;

namespace Form1;

public partial class Form1 : Form
{
    private Service.Service _service;
    private BindingList<Child> _children = new BindingList<Child>();
    
    public void SetService(Service.Service service)
    {
        _service = service;
        InitModel();
        SetupListeners();
    }
    
    public Form1()
    {
        InitializeComponent();
    }
    
    private void InitModel()
    {
        var ageGroups = _service.GetAgeGroups().ToList();
        AgeGroup emptyAgeGroup = new AgeGroup(-1, "Select an age group", 0, 0);
        ageGroups.Insert(0, emptyAgeGroup);
        
        InitAgeGroupComboBox(comboBoxGrupeVarsta, ageGroups);
        InitAgeGroupComboBox(comboBoxVarsta, ageGroups);
        
        dataGridView1.DataSource = _children;
        
        InitDataGridView2();
    }

    private async void InitDataGridView2()
    {
        dataGridView2.Columns.Clear();
    
        dataGridView2.Columns.Add("Name", "Name");
        dataGridView2.Columns.Add("Age", "Age");
        dataGridView2.Columns.Add("Number of events", "Number of events");
        dataGridView2.Columns[0].DataPropertyName = "Name";
        dataGridView2.Columns[1].DataPropertyName = "Age";
        dataGridView2.Columns[2].DataPropertyName = "NumberOfEvents";
        dataGridView2.AutoGenerateColumns = false;
        dataGridView2.Columns[0].Width = 200;
        dataGridView2.Columns[1].Width = 50;
        dataGridView2.Columns[2].Width = 200;

        try
        {
            var children = await Task.Run(() => _service.GetChildren().ToList());
            var childrenWithDetails = new List<object>();

            foreach (var child in children)
            {
                int age = AgeConverter.getAgeFromCNP(child.CNP);
                long eventCount = _service.GetNumberOfEvents(child.GetId());
                childrenWithDetails.Add(new
                {
                    child.Name,
                    Age = age,
                    NumberOfEvents = eventCount
                });
            }

            dataGridView2.DataSource = new BindingList<object>(childrenWithDetails);
        }
        catch (Exception ex)
        {
            MessageBox.Show($"Error while loading data: {ex.Message}");
        }
    }
    
    
    private void InitAgeGroupComboBox(ComboBox comboBox, List<AgeGroup> ageGroups)
    {
        comboBox.DataSource = ageGroups;
        comboBox.DisplayMember = "Name";
        comboBox.SelectedIndex = 0;
    }

    private void SetupListeners()
    {
        comboBoxVarsta.SelectedIndexChanged += (sender, e) =>
        {
            if (comboBoxVarsta.SelectedItem is AgeGroup ageGroup && ageGroup.GetId() != -1)
            {
                UpdateEventsByAgeGroup(ageGroup.GetId(), comboBoxProbe);
                _children.Clear();
            }
        };
        
        comboBoxGrupeVarsta.SelectedIndexChanged += (sender, e) =>
        {
            if (comboBoxGrupeVarsta.SelectedItem is AgeGroup ageGroup && ageGroup.GetId() != -1)
            {
                UpdateEventsByAgeGroup(ageGroup.GetId(), comboBoxProba1);
                UpdateEventsByAgeGroup(ageGroup.GetId(), comboBoxProba2);
            }
        };
        
        comboBoxProbe.SelectedIndexChanged += (sender, e) =>
        {
            if (comboBoxProbe.SelectedItem is Event eventItem)
            {
                UpdateChildrenByEvent(eventItem.GetId());
            }
        };
    }
    
    private void UpdateEventsByAgeGroup(long ageGroupId, ComboBox comboBox)
    {
        var events = _service.GetEventsByAgeGroup(ageGroupId).ToList();
        Event emptyEvent = new Event(-1, "Select an event", 0);
        events.Insert(0, emptyEvent);
        comboBox.DataSource = events;
        comboBox.DisplayMember = "Name";
        comboBox.SelectedIndex = 0;
    }
    
    private void UpdateChildrenByEvent(long eventId)
    {
        _children.Clear();
        foreach (var child in _service.GetChildrenByEvent(eventId))
        {
            _children.Add(child);
        }
    }
    
    private void button1_Click(object sender, EventArgs e)
    {
        MessageBox.Show("You have logged out!");
        this.Hide();
        Application.Exit();
    }
    
    private void buttonInscriere_Click(object sender, EventArgs e)
    {
        string nume = textBox1.Text;
        string cnp = textBox2.Text;
        
        if (comboBoxVarsta.SelectedItem is AgeGroup ageGroup1 && ageGroup1.GetId() != -1 && comboBoxProba1.SelectedItem is Event event1 && event1.GetId() != -1)
        {
            _service.SaveEnrollmentAndChild(nume, cnp, event1);
        }

        if (comboBoxVarsta.SelectedItem is AgeGroup ageGroup2 && ageGroup2.GetId() != -1 && comboBoxProba2.SelectedItem is Event event2 && event2.GetId() != -1)
        {
            _service.SaveEnrollmentAndChild(nume, cnp, event2);
        }
        
        MessageBox.Show("Inscriere successful!");
    }

    private void label1_Click(object sender, EventArgs e)
    {
        throw new System.NotImplementedException();
    }
}
