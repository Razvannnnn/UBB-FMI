package com.example.anar.service;

import com.example.anar.domain.Localitate;
import com.example.anar.domain.Rau;
import com.example.anar.observer.ConcreteObservable;
import com.example.anar.repository.LocalitateRepository;
import com.example.anar.repository.RauRepository;

import java.util.List;

public class Service extends ConcreteObservable {
    private RauRepository rauRepository;
    private LocalitateRepository localitateRepository;

    public Service(RauRepository rauRepository, LocalitateRepository localitateRepository) {
        this.rauRepository = rauRepository;
        this.localitateRepository = localitateRepository;
    }
    public List<Rau> getRauri() {
        return rauRepository.getAll();
    }

    public int getCotaRauNameById(int id) {
        return rauRepository.findById(id).getCota();
    }

    public List<Localitate> getLocalitati() {
        return localitateRepository.getAll();
    }

    public Localitate getLocalitateByName(String name) {
        return localitateRepository.findByName(name);
    }

    public void modifyRau(Rau rau, Integer cota) {
        rau.setCota(cota);
        rauRepository.update(rau);
        notifyObservers();
    }

    public String getNumeRauById(int id) {
        return localitateRepository.findById(id).getNume();
    }
}
