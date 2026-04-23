package mpp.hibernate.example.chat;

public interface ICrudRepository<ID, E extends Entity<ID>> {
    void save(E e);
    void delete(ID id);
    E findOne(ID id);
    void update(ID id, E e);
    Iterable<E> getAll();
}
