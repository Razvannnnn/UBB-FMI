package com.example.ati.Repository;
import java.util.List;

public interface GenericRepository <T>{
    void getData();
    List<T> findAll();
}