package problema8.persistence;

import problema8.model.Entity;

public interface IRepository<T extends Entity<ID>, ID> {
    T findOne(ID id);
    Iterable<T> findAll();
    void save(T entity);
    void delete(ID id);
    void update(T entity);
}
