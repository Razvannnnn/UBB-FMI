namespace lab11.Domain;

public class Echipa : Entity<Guid>
{
    public string Nume { get; set; }
    
    public Echipa() { }
    public Echipa(Guid id, string nume) : base(id)
    {
        Nume = nume;
    }
    
    public override string ToString()
    {
        return id.ToString() + ";" + Nume;
    }
}