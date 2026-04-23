namespace lab11.Domain;

public class Jucator : Elev
{
    public Echipa echipa { get; set; }
    
    public Jucator() { }
    public Jucator(Guid id, string nume, string scoala, Echipa echipa) : base(id, nume, scoala)
    {
        this.echipa = echipa;
    }
}