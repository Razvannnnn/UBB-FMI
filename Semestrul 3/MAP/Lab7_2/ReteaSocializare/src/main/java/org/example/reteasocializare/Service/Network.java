package org.example.reteasocializare.Service;

import org.example.reteasocializare.Domain.*;
import org.example.reteasocializare.Domain.Validators.ValidatorException;
import org.example.reteasocializare.Repository.DB.UtilizatorDBRepository;
import org.example.reteasocializare.Repository.DB.PrietenieDBRepository;
import org.example.reteasocializare.Repository.Paging.Page;
import org.example.reteasocializare.Repository.Paging.Pageable;
import org.example.reteasocializare.Utils.Events.ChangeEventType;
import org.example.reteasocializare.Utils.Events.UtilizatorEvent;
import org.example.reteasocializare.Utils.Observable;
import org.example.reteasocializare.Utils.Observer;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Network implements Observable<UtilizatorEvent> {
    /*
    private final InMemoryRepository<Long, Utilizator> repositoryUtilizator;
    private final InMemoryRepository<Long, Prietenie> repositoryPrietenie;

    */

    /*
    private final UtilizatorRepository repositoryUtilizator;
    private final PrieteniiRepository repositoryPrietenie;
     */

    private final UtilizatorDBRepository repositoryUtilizator;
    private final PrietenieDBRepository repositoryPrietenie;

    private final List<Observer<UtilizatorEvent>> observers;


    /**
     * Constructor pentru clasa Network
     * @param repositoryUtilizator
     * @param repositoryPrietenie
     */
    public Network(UtilizatorDBRepository repositoryUtilizator, PrietenieDBRepository repositoryPrietenie) {
        this.repositoryUtilizator = repositoryUtilizator;
        this.repositoryPrietenie = repositoryPrietenie;
        this.observers = new ArrayList<>();
    }

    public Iterable<Utilizator> getUtilizatori() {
        return repositoryUtilizator.findAll();
    }

    public Iterable<Prietenie> getPrietenii() {
        return repositoryPrietenie.findAll();
    }

    public Utilizator findUtilizator(Long id) {
        return repositoryUtilizator.findOne(id).orElse(null);
    }

    /**
     * Metoda care returneaza un id pentru un utilizator nou
     * @return id-ul utilizatorului
     */
    public Long getNewUtilizatorID() {
        Long id = 0L;
        if(repositoryUtilizator.findAll() != null) {
            for(Utilizator utilizator : repositoryUtilizator.findAll()) {
                if(utilizator.getId() > id) {
                    id = utilizator.getId();
                }
            }
        }
        id++;
        return id;
    }


    /**
     * Metoda care returneaza un id pentru o prietenie noua
     * @return id-ul prieteniei
     */
    public Long getNewPrietenieID() {
        Long id = 0L;
        if(repositoryPrietenie.findAll() != null) {
            for(Prietenie prietenie : repositoryPrietenie.findAll()) {
                if(prietenie.getId() != null && prietenie.getId() > id) {
                    id = prietenie.getId();
                }
            }
        }
        id++;
        return id;
    }

    /**
     * Metoda care adauga un utilizator in retea
     *
     * @param utilizator - utilizatorul de adaugat
     * @return
     */
    public Utilizator addUtilizator(Utilizator utilizator) {
        //utilizator.setId(getNewUtilizatorID());
        repositoryUtilizator.save(utilizator);
        notifyObservers(new UtilizatorEvent(ChangeEventType.ADD, utilizator));
        return utilizator;
    }


    /**
     * Metoda care sterge un utilizator din retea
     * @param id - id-ul utilizatorului de sters
     * @return utilizatorul sters
     */
    public Utilizator removeUtilizator(Long id) {
        Utilizator u = null;
        try {
            u = repositoryUtilizator.findOne(id).orElseThrow(() -> new ValidatorException("Utilizatorul nu exista!"));
            if(u == null) {
                throw new IllegalArgumentException("Utilizatorul nu exista!");
            }
            /*
            Vector<Long> toDelete = new Vector<>();
            getPrietenii().forEach(prietenie -> {
                if(prietenie.getIdUtilizator1().equals(id) || prietenie.getIdUtilizator2().equals(id)) {
                    toDelete.add(prietenie.getId());
                }
            });
            toDelete.forEach(repositoryPrietenie::delete);
             */

            repositoryUtilizator.delete(id).orElseThrow(() -> new ValidatorException("Utilizatorul nu exista!"));

            //u.getPrieteni().forEach(prieten -> prieten.removePrieten(u));
            //return utilizator;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        }
        if(u!=null) {
            notifyObservers(new UtilizatorEvent(ChangeEventType.DELETE, u));
        }
        return null;
    }


    /**
     * Metoda care adauga o prietenie in retea
     * @param prietenie - prietenia de adaugat
     */
    public void addPrietenie(Prietenie prietenie) {
        Utilizator utilizator1 = null;
        Utilizator utilizator2 = null;
        try {
            utilizator1 = repositoryUtilizator.findOne(prietenie.getIdUtilizator1()).orElseThrow(ValidatorException::new);
            utilizator2 = repositoryUtilizator.findOne(prietenie.getIdUtilizator2()).orElseThrow(ValidatorException::new);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        }
        if(getPrietenii() != null) {
            getPrietenii().forEach(f -> {
                if((f.getIdUtilizator1().equals(prietenie.getIdUtilizator1()) && f.getIdUtilizator2().equals(prietenie.getIdUtilizator2())) ||
                        (f.getIdUtilizator1().equals(prietenie.getIdUtilizator2()) && f.getIdUtilizator2().equals(prietenie.getIdUtilizator1()))) {
                    throw new IllegalArgumentException("Prietenia exista deja!");
                }
            });
            if(repositoryUtilizator.findOne(prietenie.getIdUtilizator1()).isEmpty() || repositoryUtilizator.findOne(prietenie.getIdUtilizator2()).isEmpty()) {
                throw new IllegalArgumentException("Unul dintre utilizatori nu exista!");
            }
            if(prietenie.getIdUtilizator1().equals(prietenie.getIdUtilizator2())) {
                throw new IllegalArgumentException("Utilizatorii sunt aceeasi persoana!");
            }
        }
        prietenie.setId(getNewPrietenieID());
        repositoryPrietenie.save(prietenie);

        assert utilizator1 != null;
        utilizator1.addPrieten(utilizator2);

        assert utilizator2 != null;
        utilizator2.addPrieten(utilizator1);
    }


    /**
     * Metoda care sterge o prietenie din retea
     * @param id1 - id-ul primului utilizator
     * @param id2 - id-ul celui de-al doilea utilizator
     */
    public void removePrietenie(Long id1, Long id2) {
        Utilizator utilizator1 = null;
        Utilizator utilizator2 = null;

        try {
            utilizator1 = repositoryUtilizator.findOne(id1).orElseThrow(ValidatorException::new);
            utilizator2 = repositoryUtilizator.findOne(id2).orElseThrow(ValidatorException::new);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        }

        final Long[] id = {0L};
        repositoryPrietenie.findAll().forEach(prietenie -> {
            if((prietenie.getIdUtilizator1().equals(id1) && prietenie.getIdUtilizator2().equals(id2)) ||
                    (prietenie.getIdUtilizator1().equals(id2) && prietenie.getIdUtilizator2().equals(id1))) {
                id[0] = prietenie.getId();
            }
        });
        if(id[0] == 0L) {
            throw new IllegalArgumentException("Prietenia nu exista!");
        }
        repositoryPrietenie.delete(id[0]);

        assert utilizator1 != null;
        utilizator1.removePrieten(utilizator2);
        assert utilizator2 != null;
        utilizator2.removePrieten(utilizator1);
    }

    public Utilizator findUtilizatorByUsername(String username) {
        Utilizator utilizator = null;
        for(Utilizator u : getUtilizatori()) {
            if(u.getNumeUtilizator().equals(username)) {
                utilizator = u;
            }
        }
        return utilizator;
    }

    public Iterable<Utilizator> getNewFriendsForUtilizator(Utilizator utilizator) {
        List<Utilizator> newFriends = new ArrayList<>();
        for(Utilizator u : getUtilizatori()) {
            boolean areFriends = false;
            for(Prietenie p : getPrietenii()) {
                if((p.getIdUtilizator1().equals(utilizator.getId()) && p.getIdUtilizator2().equals(u.getId()) && p.getStatus() == 3) ||
                        (p.getIdUtilizator1().equals(u.getId()) && p.getIdUtilizator2().equals(utilizator.getId()) && p.getStatus() == 3)) {
                    areFriends = true;
                    break;
                }
            }
            if(!areFriends && !u.equals(utilizator)) {
                newFriends.add(u);
            }
        }
        return newFriends;
    }

    public Map<Utilizator, LocalDateTime> getCurrentFriendsForUtilizator(Utilizator utilizator) {
        Map<Utilizator, LocalDateTime> currentFriends = new HashMap<>();
        for (Utilizator u : getUtilizatori()) {
            for (Prietenie p : getPrietenii()) {
                if ((p.getIdUtilizator1().equals(utilizator.getId()) && p.getIdUtilizator2().equals(u.getId()) && p.getStatus() == 3) ||
                        (p.getIdUtilizator1().equals(u.getId()) && p.getIdUtilizator2().equals(utilizator.getId()) && p.getStatus() == 3)) {
                    currentFriends.put(u, p.getDate());
                    break;
                }
            }
        }
        return currentFriends;
    }

    public void sendFriendRequest(Utilizator utilizator, Utilizator selected) {
        int ok = 0;
        for(Prietenie p : getPrietenii()) {
            if((p.getIdUtilizator1().equals(utilizator.getId()) && p.getIdUtilizator2().equals(selected.getId())) && p.getStatus() == 1) {
                throw new IllegalArgumentException("Cererea de prietenie exista deja!");
            }
            if((p.getIdUtilizator1().equals(selected.getId()) && p.getIdUtilizator2().equals(utilizator.getId())) && p.getStatus() == 1) {
                repositoryPrietenie.updateStatus(p, 3);
                ok = 1;
                break;
            }
        }
        if(ok == 0) {
            Prietenie prietenie = new Prietenie(utilizator.getId(), selected.getId(), 1);
            addPrietenie(prietenie);
        }
        notifyObservers(new UtilizatorEvent(ChangeEventType.UPDATE, utilizator));
    }

    public void declineFriendRequest(Utilizator utilizator, Utilizator selected) {
        for(Prietenie p : getPrietenii()) {
            if((p.getIdUtilizator1().equals(selected.getId()) && p.getIdUtilizator2().equals(utilizator.getId())) && p.getStatus() == 1) {
                repositoryPrietenie.delete(p.getId());
                break;
            }
        }
        notifyObservers(new UtilizatorEvent(ChangeEventType.UPDATE, utilizator));
    }

    public void acceptFriendRequest(Utilizator utilizator, Utilizator selected) {
        for(Prietenie p : getPrietenii()) {
            if((p.getIdUtilizator1().equals(selected.getId()) && p.getIdUtilizator2().equals(utilizator.getId())) && p.getStatus() == 1) {
                repositoryPrietenie.updateStatus(p, 3);
                break;
            }
        }
        notifyObservers(new UtilizatorEvent(ChangeEventType.UPDATE, utilizator));
    }

    public List<Utilizator> pendingFriendRequests(Utilizator utilizator) {
        List<Utilizator> currentFriends = new ArrayList<>();
        for (Utilizator u : getUtilizatori()) {
            for (Prietenie p : getPrietenii()) {
                if ((p.getIdUtilizator1().equals(utilizator.getId()) && p.getIdUtilizator2().equals(u.getId()) && p.getStatus() == 1)) {
                    currentFriends.add(u);
                    break;
                }
            }
        }
        return currentFriends;
    }

    public List<Utilizator> FriendRequests(Utilizator utilizator) {
        List<Utilizator> currentFriends = new ArrayList<>();
        for (Utilizator u : getUtilizatori()) {
            for (Prietenie p : getPrietenii()) {
                if ((p.getIdUtilizator1().equals(u.getId()) && p.getIdUtilizator2().equals(utilizator.getId()) && p.getStatus() == 1)) {
                    currentFriends.add(u);
                    break;
                }
            }
        }
        return currentFriends;
    }

    public int numberOfFriendRequests(Utilizator utilizator) {
        int count = 0;
        for (Utilizator u : getUtilizatori()) {
            for (Prietenie p : getPrietenii()) {
                if ((p.getIdUtilizator1().equals(u.getId()) && p.getIdUtilizator2().equals(utilizator.getId()) && p.getStatus() == 1)) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    public Page<Prietenie> findAllUserFriends(Pageable pageable, Utilizator user) {
        Page<Prietenie> pgF = repositoryPrietenie.findAllUserFriends(pageable, user.getId());
        for (Prietenie f: pgF.getContent()) {
            f.setNumeUser1(repositoryUtilizator.findOne(f.getIdUtilizator1()).get().getNume() + " " +
                    repositoryUtilizator.findOne(f.getIdUtilizator1()).get().getPrenume());
            f.setNumeUser2(repositoryUtilizator.findOne(f.getIdUtilizator2()).get().getNume() + " " +
                    repositoryUtilizator.findOne(f.getIdUtilizator2()).get().getPrenume());
        }
        return pgF;
    }

    public Page<Prietenie> findAllFriendships(Pageable pageable) {
        Page<Prietenie> pgF = repositoryPrietenie.findAll(pageable);
        for (Prietenie f: pgF.getContent()) {
            f.setNumeUser1(repositoryUtilizator.findOne(f.getIdUtilizator1()).get().getNume() + " " +
                    repositoryUtilizator.findOne(f.getIdUtilizator1()).get().getPrenume());
            f.setNumeUser2(repositoryUtilizator.findOne(f.getIdUtilizator2()).get().getNume() + " " +
                    repositoryUtilizator.findOne(f.getIdUtilizator2()).get().getPrenume());
        }
        return pgF;
    }

    @Override
    public void addObserver(Observer<UtilizatorEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<UtilizatorEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UtilizatorEvent t) {
        observers.forEach(observer -> observer.update(t));
    }
}
