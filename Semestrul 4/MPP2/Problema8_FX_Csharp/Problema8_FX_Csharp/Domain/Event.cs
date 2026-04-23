namespace Problema8_FX_Csharp.Domain;

public class Event : Entity<long>
{
    public String Name { get; set; }
    public int Distance { get; set; }
    
    public Event(long id, String name, int distance)
    {
        SetId(id);
        Name = name;
        Distance = distance;
    }
    
    public override String ToString()
    {
        return $"Name: {Name}, Distance: {Distance}";
    }
    
    public override bool Equals(Object obj)
    {
        if (obj == null || GetType() != obj.GetType())
        {
            return false;
        }
        
        Event e = (Event)obj;
        return Name.Equals(e.Name) && Distance == e.Distance;
    }
}