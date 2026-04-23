package org.example.reteasocializare.Repository.File;

import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Domain.Validators.Validator;

public class UtilizatorRepository extends AbstractFileRepository<Long, Utilizator> {

    /**
     * Constructor pentru clasa UtilizatorRepository
     * @param validator - validatorul pentru entitatea de tip Utilizator
     * @param filename - numele fisierului
     */
    public UtilizatorRepository(Validator<Utilizator> validator, String filename) {
        super(validator, filename);
    }

    @Override
    public Utilizator createEntity(String line) {
        String[] splited = line.split(";");
        Utilizator u = new Utilizator(splited[1], splited[2], splited[3], splited[4]);
        u.setId(Long.parseLong(splited[0]));
        return u;
    }

    @Override
    public String saveEntity(Utilizator entity) {
        String s = entity.getId() + ";" + entity.getNume() + ";" + entity.getPrenume();
        return s;
    }
}
