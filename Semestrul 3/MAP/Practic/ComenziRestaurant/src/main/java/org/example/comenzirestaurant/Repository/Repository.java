package org.example.comenzirestaurant.Repository;

import java.util.List;

public interface Repository<E> {
    //E findOne(ID id);
    //E save(E entity);
    //E delete(ID id);
    // update(E entity);
    List<E> getAll();
}
