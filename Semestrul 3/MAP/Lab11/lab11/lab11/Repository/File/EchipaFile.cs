using lab11.Domain;

namespace lab11.Repository.File;

public class EchipaFile : FileRepo<Echipa>
{
    public EchipaFile(string filename) : base(filename) { }
    
    protected override Echipa entityFromString(string data)
    {
        string[] parts = data.Split(";");
        return new Echipa(Guid.Parse(parts[0]), parts[1]);
    }
}