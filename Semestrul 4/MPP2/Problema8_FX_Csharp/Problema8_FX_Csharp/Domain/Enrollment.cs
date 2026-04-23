namespace Problema8_FX_Csharp.Domain;

public class Enrollment: Entity<long>
{
    public Child Child { get; set; }
    public Event Event { get; set; }
    
    public Enrollment(long id, Child child, Event eventt)
    {
        SetId(id);
        Child = child;
        Event = eventt;
    }
    
    public override String ToString()
    {
        return $"Child: {Child}, Event: {Event}";
    }
}