package chat.persistence;

import chat.model.Identifiable;


public interface ICrudRepository<ID, E extends Identifiable<ID>> {
    void save(E e);
    void delete(ID id);
    E findOne(ID id);
    void update(ID id, E e);
    Iterable<E> getAll();
}
