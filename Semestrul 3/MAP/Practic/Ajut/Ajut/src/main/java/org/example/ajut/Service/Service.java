package org.example.ajut.Service;

import org.example.ajut.Domain.Nevoie;
import org.example.ajut.Domain.Persoana;
import org.example.ajut.Repository.NevoieRepo;
import org.example.ajut.Repository.PersoanaRepo;
import org.example.ajut.Utils.Observable;
import org.example.ajut.Utils.Observer;

import java.util.ArrayList;
import java.util.List;

public class Service implements Observable {
    private NevoieRepo nevoieRepo;
    private PersoanaRepo persoanaRepo;
    private List<Observer> observerList;

    public Service(NevoieRepo nevoieRepo, PersoanaRepo persoanaRepo) {
        this.nevoieRepo = nevoieRepo;
        this.persoanaRepo = persoanaRepo;
        this.observerList = new ArrayList<>();
    }

    public Iterable<Persoana> getAllPersoane() {
        return persoanaRepo.findAll();
    }

    public void addPersoana(Persoana persoana) {
        persoanaRepo.save(persoana);
        notifyObservers();
    }

    @Override
    public void addObserver(Observer e) {
        observerList.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        observerList.remove(e);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer:observerList){
            observer.update();
        }
    }

    public Persoana getPersoanaByUsername(String username) {
        return persoanaRepo.findByUsername(username);
    }

    public Iterable<Nevoie> getNevoiDinOras(Persoana persoana) {
        return nevoieRepo.nevoiDinOras(persoana);
    }

    public Iterable<Nevoie> getAllNevoi() {
        return nevoieRepo.findAll();
    }

    public void addNevoie(Nevoie nevoie) {
        nevoieRepo.save(nevoie);
        notifyObservers();
    }

    public void updateNevoie(Nevoie nevoie, Persoana persoana) {
        nevoieRepo.update(nevoie, persoana);
        notifyObservers();
    }

    public Iterable<Nevoie> getNevoiPeCareLeRezolv(Persoana persoana) {
        return nevoieRepo.nevoiPeCareLeRezolv(persoana);
    }
}
