package ro.mpp2025.problema8_fx.Repository;

import ro.mpp2025.problema8_fx.Domain.Entity;

public interface IRepository<T extends Entity<ID>, ID> {
    T findOne(ID id);
    Iterable<T> findAll();
    void save(T entity);
    void delete(ID id);
    void update(T entity);
}
