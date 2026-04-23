package com.example.guiex1.services;



import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.repository.Repository;
import com.example.guiex1.utils.events.ChangeEventType;
import com.example.guiex1.utils.events.UtilizatorEntityChangeEvent;
import com.example.guiex1.utils.observer.Observable;
import com.example.guiex1.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtilizatorService implements Observable<UtilizatorEntityChangeEvent> {
    private Repository<Long, Utilizator> repo;
    private List<Observer<UtilizatorEntityChangeEvent>> observers=new ArrayList<>();

    public UtilizatorService(Repository<Long, Utilizator> repo) {
        this.repo = repo;
    }


    public Utilizator addUtilizator(Utilizator user) {
        if(repo.save(user).isEmpty()){
            UtilizatorEntityChangeEvent event = new UtilizatorEntityChangeEvent(ChangeEventType.ADD, user);
            notifyObservers(event);
            return null;
        }
        return user;
    }

    public Utilizator deleteUtilizator(Long id){
        Optional<Utilizator> user=repo.delete(id);
        if (user.isPresent()) {
            notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.DELETE, user.get()));
            return user.get();
        }
        return null;
    }

    public Iterable<Utilizator> getAll(){
        return repo.findAll();
    }



    @Override
    public void addObserver(Observer<UtilizatorEntityChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<UtilizatorEntityChangeEvent> e) {
        //observers.remove(e);
    }

    @Override
    public void notifyObservers(UtilizatorEntityChangeEvent t) {

        observers.stream().forEach(x->x.update(t));
    }

    public Utilizator updateUtilizator(Utilizator u) {
        Optional<Utilizator> oldUser=repo.findOne(u.getId());
        if(oldUser.isPresent()) {
            Optional<Utilizator> newUser=repo.update(u);
            if (newUser.isEmpty())
                notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.UPDATE, u, oldUser.get()));
            return newUser.orElse(null);
        }
        return oldUser.orElse(null);
    }


}
