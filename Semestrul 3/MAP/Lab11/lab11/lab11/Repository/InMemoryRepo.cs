using lab11.Domain;

namespace lab11.Repository;

public class InMemoryRepo<E> : Repository<Guid, E> where E : Entity<Guid>, new()
{
    public Dictionary<Guid, E> entities;
    
    public InMemoryRepo() => entities = new Dictionary<Guid, E>();
    
    public IEnumerable<E> findAll() => entities.Values;
    public E findOne(Guid id)
    {
        return entities[id];
    }
    public E save(E entity)
    {   
        entities[entity.id] = entity;
        return entity;
    }
    public bool delete(Guid id)
    {
        return entities.Remove(id);
    }
}