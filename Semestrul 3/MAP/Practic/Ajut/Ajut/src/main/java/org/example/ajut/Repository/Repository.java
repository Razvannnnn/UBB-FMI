package org.example.ajut.Repository;

public interface Repository<E> {
    //E findOne(Long id);
    Iterable<E> findAll();
    //E save(E entity);
    //E delete(Long id);
    //E update(E entity);
}
