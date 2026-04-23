namespace lab11.Domain;

public class Entity<ID>
{
    public ID id { get; set; }
    public Entity() { }
    public Entity(ID id)
    {
        this.id = id;
    } 
}