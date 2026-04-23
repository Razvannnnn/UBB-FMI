using chat.model;

namespace chat.persistence;

public interface ICrudRepository<ID,T> where T:IIdentifiable<ID>
{
    void save(T entity);
    void delete(ID id);
    T findOne(ID id);
    void update(ID id, T e);
    IEnumerable<T> getAll();
}