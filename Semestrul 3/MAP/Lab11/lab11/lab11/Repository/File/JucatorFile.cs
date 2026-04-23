using lab11.Domain;

namespace lab11.Repository.File;

public class JucatorFile : FileRepo<Jucator>
{
    Repository<Guid, Echipa> echipaRepo;

    public JucatorFile(Repository<Guid, Echipa> echipaRepo, string filename)
    {
        this.echipaRepo = echipaRepo;
        readFromFile(filename);
    }

    protected override Jucator entityFromString(string data)
    {
        string[] parts = data.Split(";");
        Guid idEchipa = Guid.Parse(parts[3]);
        return new Jucator(Guid.Parse(parts[0]), parts[1], parts[2], echipaRepo.findOne(idEchipa));
    }
}