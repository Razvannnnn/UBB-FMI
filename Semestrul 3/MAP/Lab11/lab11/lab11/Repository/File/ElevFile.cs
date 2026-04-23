using lab11.Domain;

namespace lab11.Repository.File;

public class ElevFile : FileRepo<Elev>
{
    public ElevFile(string filename) : base(filename) { }

    protected override Elev entityFromString(string data)
    {
        string[] parts = data.Split(";");
        return new Elev(Guid.Parse(parts[0]), parts[1], parts[2]);
    }
}