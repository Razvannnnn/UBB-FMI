package iss.parkingapp.repository;

import iss.parkingapp.domain.Entity;
import javafx.collections.ObservableList;

public interface IRepo<T, ID> {
    void add(T entity);
    void delete(ID id);
    void update(T entity);
    T findOne(ID id);
    ObservableList<T> findAll();
    int size();
}
