package ro.mpp2025.repository;

import ro.mpp2025.domain.Entity;

public interface IRepository<ID, T extends Entity<ID>> {
    T findOne(ID id);
    Iterable<T> findAll();
    T save(T entity);
    T delete(ID id);
    T update(T entity);
}