package org.example.reteasocializare.Domain.Validators;

import org.example.reteasocializare.Domain.Prietenie;
import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Repository.DB.PrietenieDBRepository;
import org.example.reteasocializare.Repository.DB.UtilizatorDBRepository;
import org.example.reteasocializare.Repository.Memory.InMemoryRepository;

import java.util.Optional;

public class PrietenieValidator implements Validator<Prietenie> {

    private UtilizatorDBRepository repo;

    public PrietenieValidator(UtilizatorDBRepository repo) {
        this.repo = repo;
    }

    @Override
    public void validate(Prietenie entity) throws ValidatorException {
        Optional<Utilizator> u1 = repo.findOne(entity.getIdUtilizator1());
        Optional<Utilizator> u2 = repo.findOne(entity.getIdUtilizator2());

        if(entity.getIdUtilizator1() == null || entity.getIdUtilizator2() == null) {
            throw new ValidatorException("Id-ul utilizatorului nu poate fi null");
        }
        if(u1 == null || u2 == null) {
            throw new ValidatorException("Utilizatorii nu exista");
        }
    }
}
