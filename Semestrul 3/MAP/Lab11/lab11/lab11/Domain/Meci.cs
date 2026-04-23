namespace lab11.Domain;

public class Meci : Entity<Guid>
{
    public Echipa echipa1 { get; set; }
    public Echipa echipa2 { get; set; }
    public DateTime data { get; set; }
    
    public Meci() { }
    public Meci(Guid id, Echipa echipa1, Echipa echipa2, DateTime data) : base(id)
    {
        this.echipa1 = echipa1;
        this.echipa2 = echipa2;
        this.data = data;
    }
}