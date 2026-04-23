using System.ComponentModel;
using System.Windows;

using Problema8SC_CSharp.Model;
using Problema8SC_CSharp.Services;

namespace Form1;

public partial class Form1 : Form, IObserver
{
    private IServices _service;
    private User _currentUser;
    private BindingList<Child> _children = new BindingList<Child>();
    private BindingList<ChildDetails> _childrenDetails = new BindingList<ChildDetails>();
    
    public void SetService(IServices service, User user)
    {
        _service = service;
        _currentUser = user;
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
        InitDataGridView3();
    }

    private void InitDataGridView2()
    {
        dataGridView2.Columns.Clear();
        dataGridView2.Columns.Add("Name", "Name");
        dataGridView2.Columns.Add("Age", "Age");
        dataGridView2.Columns.Add("Number of events", "Number of events");
        dataGridView2.Columns[0].DataPropertyName = "Name";
        dataGridView2.Columns[1].DataPropertyName = "Age";
        dataGridView2.Columns[2].DataPropertyName = "NumberOfEvents";
        dataGridView2.AutoGenerateColumns = false;
        dataGridView2.Columns[0].Width = 150;
        dataGridView2.Columns[1].Width = 50;
        dataGridView2.Columns[2].Width = 200;

        UpdateChildDetails();
    }

    private void InitDataGridView3()
    {
        // Show all events and their age groups
        dataGridView3.Columns.Clear();
        dataGridView3.Columns.Add("Event Name", "Event Name");
        dataGridView3.Columns.Add("Age Group", "Age Group");
        dataGridView3.Columns.Add("Min Age", "Min Age");
        dataGridView3.Columns.Add("Max Age", "Max Age");
        dataGridView3.Columns[0].DataPropertyName = "Name";
        dataGridView3.Columns[1].DataPropertyName = "AgeGroup";
        dataGridView3.Columns[2].DataPropertyName = "MinAge";
        dataGridView3.Columns[3].DataPropertyName = "MaxAge";
        dataGridView3.AutoGenerateColumns = false;
        dataGridView3.Columns[0].Width = 100;
        dataGridView3.Columns[1].Width = 100;
        dataGridView3.Columns[2].Width = 75;
        dataGridView3.Columns[3].Width = 75;
        
        var events = _service.GetEvents().ToList();
        var ageGroups = _service.GetAgeGroups().ToList();
        var eventsList = events.Select(e => new
        {
            Name = e.Name,
            AgeGroup = ageGroups.FirstOrDefault(ag => ag.Id == e.AgeGroupId)?.Name,
            MinAge = ageGroups.FirstOrDefault(ag => ag.Id == e.AgeGroupId)?.MinAge,
            MaxAge = ageGroups.FirstOrDefault(ag => ag.Id == e.AgeGroupId)?.MaxAge
        }).ToList();
        dataGridView3.DataSource = eventsList;
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
            if (comboBoxVarsta.SelectedItem is AgeGroup ageGroup && ageGroup.Id != -1)
            {
                UpdateEventsByAgeGroup(ageGroup.Id, comboBoxProba1);
                UpdateEventsByAgeGroup(ageGroup.Id, comboBoxProba2);
            }
        };
        
        comboBoxGrupeVarsta.SelectedIndexChanged += (sender, e) =>
        {
            if (comboBoxGrupeVarsta.SelectedItem is AgeGroup ageGroup && ageGroup.Id != -1)
            {
                UpdateEventsByAgeGroup(ageGroup.Id, comboBoxProbe);
                _children.Clear();
            }
        };
        
        comboBoxProbe.SelectedIndexChanged += (sender, e) =>
        {
            if (comboBoxProbe.SelectedItem is Event eventItem && eventItem.Id != -1)
            {
                UpdateChildrenByEvent(eventItem.Id);
            }
        };
    }
    
    private void UpdateEventsByAgeGroup(long ageGroupId, ComboBox comboBox)
    {
        var events = _service.GetEventsByAgeGroup(ageGroupId) ?? new List<Event>();
        var eventsList = events.ToList();
    
        Event emptyEvent = new Event(-1, "Select an event", 0, ageGroupId);
        eventsList.Insert(0, emptyEvent);
    
        comboBox.DataSource = eventsList;
        comboBox.DisplayMember = "Name";
        comboBox.SelectedIndex = 0;
    }

    
    private void UpdateChildrenByEvent(long eventId)
    {
        _children.Clear();
        var childrens = _service.GetChildrenByEvent(eventId);
        if (childrens != null)
        {
            foreach (var child in childrens)
            {
                _children.Add(child);
            }
        }
        else
        {
            MessageBox.Show("No children found for this event.");
        }
        dataGridView1.DataSource = null; // Optional, forces DataGridView refresh
        dataGridView1.DataSource = _children;
        dataGridView1.Refresh();
    }

    private void UpdateChildDetails()
    {
        _childrenDetails.Clear();
        var childrenWithDetails = _service.GetDetailsForAllChildren();
        if (childrenWithDetails != null)
        {
            foreach (var child in childrenWithDetails)
            {
                _childrenDetails.Add(child);
            }
        }
        else
        {
            MessageBox.Show("No children found.");
        }
        dataGridView2.DataSource = null; // Optional, forces DataGridView refresh
        dataGridView2.DataSource = _childrenDetails;
        dataGridView2.Refresh();
    }
    
    private void button1_Click(object sender, EventArgs e)
    {
        _service.Logout(_currentUser, this);
        MessageBox.Show("You have logged out!");
        this.Hide();
        Application.Exit();
    }
    
    private void buttonInscriere_Click(object sender, EventArgs e)
    {
        string nume = textBox1.Text;
        string cnp = textBox2.Text;
        
        if (comboBoxVarsta.SelectedItem is AgeGroup ageGroup1 && ageGroup1.Id != -1 && comboBoxProba1.SelectedItem is Event event1 && event1.Id != -1)
        {
            _service.SaveChildAndEnrollment(nume, cnp, event1);
        }

        if (comboBoxVarsta.SelectedItem is AgeGroup ageGroup2 && ageGroup2.Id != -1 && comboBoxProba2.SelectedItem is Event event2 && event2.Id != -1)
        {
            _service.SaveChildAndEnrollment(nume, cnp, event2);
        }
        
        MessageBox.Show("Inscriere successful!");
    }

    private void label1_Click(object sender, EventArgs e)
    {
        throw new System.NotImplementedException();
    }

    public void UserLoggedIn(User user)
    {
        throw new NotImplementedException();
    }

    public void UserLoggedOut(User user)
    {
        throw new NotImplementedException();
    }

    public void GetChildrenByEvent(List<Child> children)
    {
        Console.WriteLine("Received children by event.");

        _children.Clear();
        foreach (var child in children)
        {
            _children.Add(child);
        }

        dataGridView1.DataSource = _children;
    }


    /// <inheritdoc />
    public void SaveChildAndEnrollment(Child child, Enrollment enrollment)
    {
        if (InvokeRequired)
        {
            Invoke(() =>
            {
                if (comboBoxProbe.SelectedItem is Event selectedEvent)
                {
                    UpdateChildrenByEvent(selectedEvent.Id);
                    UpdateChildDetails();
                }
            });
        }
        else
        {
            if (comboBoxProbe.SelectedItem is Event selectedEvent)
            {
                UpdateChildrenByEvent(selectedEvent.Id);
                UpdateChildDetails();
            }
        }
    }


}
