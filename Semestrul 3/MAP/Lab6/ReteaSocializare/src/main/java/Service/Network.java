package Service;

import Domain.*;
import Domain.Validators.ValidatorException;
import Repository.DB.UtilizatorDBRepository;
import Repository.DB.PrietenieDBRepository;
import Repository.File.PrieteniiRepository;
import Repository.File.UtilizatorRepository;
import Repository.Memory.InMemoryRepository;


import java.util.Vector;

public class Network {
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


    /**
     * Constructor pentru clasa Network
     * @param repositoryUtilizator
     * @param repositoryPrietenie
     */
    public Network(UtilizatorDBRepository repositoryUtilizator, PrietenieDBRepository repositoryPrietenie) {
        this.repositoryUtilizator = repositoryUtilizator;
        this.repositoryPrietenie = repositoryPrietenie;
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
     * @param utilizator - utilizatorul de adaugat
     */
    public void addUtilizator(Utilizator utilizator) {
        utilizator.setId(getNewUtilizatorID());
        repositoryUtilizator.save(utilizator);
    }


    /**
     * Metoda care sterge un utilizator din retea
     * @param id - id-ul utilizatorului de sters
     * @return utilizatorul sters
     */
    public Utilizator removeUtilizator(Long id) {
        try {
            Utilizator u = repositoryUtilizator.findOne(id).orElseThrow(() -> new ValidatorException("Utilizatorul nu exista!"));
            Vector<Long> toDelete = new Vector<>();
            getPrietenii().forEach(prietenie -> {
                if(prietenie.getIdUtilizator1().equals(id) || prietenie.getIdUtilizator2().equals(id)) {
                    toDelete.add(prietenie.getId());
                }
            });
            toDelete.forEach(repositoryPrietenie::delete);
            Utilizator utilizator = repositoryUtilizator.delete(id).orElseThrow(() -> new ValidatorException("Utilizatorul nu exista!"));
            u.getPrieteni().forEach(prieten -> prieten.removePrieten(u));
            return utilizator;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
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

}
