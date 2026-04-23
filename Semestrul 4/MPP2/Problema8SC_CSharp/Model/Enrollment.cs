namespace Problema8SC_CSharp.Model;


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