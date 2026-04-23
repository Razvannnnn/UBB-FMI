using lab11.Domain;

namespace lab11.Repository.File;

public abstract class FileRepo<E> : InMemoryRepo<E> where E : Entity<Guid>, new()
{
    public FileRepo() { }
    public FileRepo(string filename)
    {
        readFromFile(filename);
    }
    
    protected void readFromFile(string filename)
    {
        StreamReader streamReaderreader = new StreamReader(filename);
        string data;
        while(true)
        {
            data = streamReaderreader.ReadLine();
            if (data == null)
                break;

            save(entityFromString(data));
        }
    }
    
    protected abstract E entityFromString(string data);
}