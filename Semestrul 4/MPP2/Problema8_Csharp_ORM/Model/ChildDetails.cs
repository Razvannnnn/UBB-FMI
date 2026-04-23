namespace Problema8SC_CSharp.Model;

public class ChildDetails: Entity<long>
{
    public string Name { get; set; }
    public int Age { get; set; }
    public long NumberOfEvents { get; set; }
    
    public long Id
    {
        get => GetId();
        set => SetId(value);
    }
    
    public ChildDetails(long id, string name, int age, long numberOfEvents)
    {
        Id = id;
        Name = name;
        Age = age;
        NumberOfEvents = numberOfEvents;
    }
}