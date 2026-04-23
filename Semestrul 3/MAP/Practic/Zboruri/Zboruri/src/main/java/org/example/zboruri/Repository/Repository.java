package org.example.zboruri.Repository;

public interface Repository<E> {
    //void add(E entity);
    //void remove(E entity);
    //void update(E entity);
    //E findById(int id);
    Iterable<E> getAll();
}
