namespace Problema8SC_CSharp.Model;

public class Event : Entity<long>
{
    public long Id
    {
        get => GetId();
        set => SetId(value);
    }
    public String Name { get; set; }
    public int Distance { get; set; }
    public long AgeGroupId { get; set; }
    
    public Event(long id, String name, int distance, long ageGroupId)
    {
        Id = id;
        Name = name;
        Distance = distance;
        AgeGroupId = ageGroupId;
    }
    
    public override String ToString()
    {
        return $"Name: {Name}, Distance: {Distance}, AgeGroupId: {AgeGroupId}";
    }
    
    public override bool Equals(object? obj)
    {
        if (obj == null || GetType() != obj.GetType())
        {
            return false;
        }
        
        Event e = (Event)obj;
        return Name.Equals(e.Name) && Distance == e.Distance && AgeGroupId == e.AgeGroupId;
    }

    protected bool Equals(Event other)
    {
        return Name == other.Name && Distance == other.Distance && AgeGroupId == other.AgeGroupId;
    }

    public override int GetHashCode()
    {
        return HashCode.Combine(Name, Distance, AgeGroupId);
    }
}