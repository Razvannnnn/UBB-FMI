package org.example.reteasocializare.Service;

import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Repository.Repository;
import org.example.reteasocializare.Utils.Events.EventType;
import org.example.reteasocializare.Utils.Events.UtilizatorEventType;
import org.example.reteasocializare.Utils.Observer.Observable;
import org.example.reteasocializare.Utils.Observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtilizatorService implements Observable<UtilizatorEventType> {
    private Repository<Long, Utilizator> repository;
    private List<Observer<UtilizatorEventType>> observers = new ArrayList<>();

    public UtilizatorService(Repository<Long, Utilizator> repository) {
        this.repository = repository;
    }

    public Utilizator addUtilizator(Utilizator utilizator) {
        if(repository.save(utilizator).isEmpty()) {
            UtilizatorEventType event = new UtilizatorEventType(EventType.ADD, utilizator);
            notifyObservers(event);
            return null;
        }
        return utilizator;
    }

    public Utilizator deleteUtilizator(Long id) {
        Optional<Utilizator> utilizator = repository.delete(id);
        if(utilizator.isPresent()) {
            notifyObservers(new UtilizatorEventType(EventType.DELETE, utilizator.get()));
            return utilizator.get();
        }
        return null;
    }

    public Iterable<Utilizator> getAll() {
        return repository.findAll();
    }

    @Override
    public void addObserver(Observer<UtilizatorEventType> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<UtilizatorEventType> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(UtilizatorEventType event) {
        observers.forEach(x -> x.update(event));
    }
}
