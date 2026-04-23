using lab11.Domain;

namespace lab11.Repository.File;

public class MeciFile : FileRepo<Meci>
{ 
    Repository<Guid, Echipa> echipe;
    
    public MeciFile(Repository<Guid, Echipa> echipe, string filename)
    {
        this.echipe = echipe;
        readFromFile(filename);
    }
    
    protected override Meci entityFromString(string data)
    {
        string[] parts = data.Split(";");
        Guid idEchipa1 = Guid.Parse(parts[1]);
        Guid idEchipa2 = Guid.Parse(parts[2]);
        return new Meci(Guid.Parse(parts[0]), echipe.findOne(idEchipa1), echipe.findOne(idEchipa2), DateTime.Parse(parts[3]));
    }   
}