namespace lab11.Domain;

public class JucatorActiv : Entity<Guid>
{
    public Guid idJucator { get; set; }
    public Guid idMeci { get; set; }
    public int nrPuncteInscrise { get; set; }
    public Tip Tip { get; set; }
    
    public JucatorActiv() { }
    public JucatorActiv(Guid id, Guid idJucator, Guid idMeci, int nrPuncteInscrise, Tip tip) : base(id)
    {
        this.idJucator = idJucator;
        this.idMeci = idMeci;
        this.nrPuncteInscrise = nrPuncteInscrise;
        Tip = tip;
    }
}