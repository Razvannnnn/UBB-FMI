namespace lab11.Domain;

public class Elev : Entity<Guid>
{
    public string Nume { get; set; }
    public string Scoala { get; set; }
    
    public Elev() { }
    public Elev(Guid id, string nume, string scoala) : base(id)
    {
        Nume = nume;
        Scoala = scoala;
    }

    public override string ToString()
    {
        return id.ToString() + ";" + Nume + ";" + Scoala;
    }
}