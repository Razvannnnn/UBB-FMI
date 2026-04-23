using lab11.Domain;

namespace lab11.Repository.File;

public class JucatorActiveFile : FileRepo<JucatorActiv>
{
    public JucatorActiveFile(string filename) : base(filename) { }

    protected override JucatorActiv entityFromString(string data)
    {
        string[] parts = data.Split(";");
        return new JucatorActiv(Guid.Parse(parts[0]), Guid.Parse(parts[1]), Guid.Parse(parts[2]), int.Parse(parts[3]), Tip.Rezerva);
    }
}