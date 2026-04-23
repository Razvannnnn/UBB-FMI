using lab11.Domain;

namespace lab11.Repository;

public interface Repository<ID, E> where E : Entity<ID>
{
    E findOne(ID id);
    IEnumerable<E> findAll();
    E save(E entity);
    bool delete(ID id);
}