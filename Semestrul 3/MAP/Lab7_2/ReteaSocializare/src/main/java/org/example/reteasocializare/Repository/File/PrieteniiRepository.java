package org.example.reteasocializare.Repository.File;

import org.example.reteasocializare.Domain.Prietenie;
import org.example.reteasocializare.Domain.Validators.Validator;

public class PrieteniiRepository extends AbstractFileRepository<Long, Prietenie> {

    /**
     * Constructor pentru clasa PrieteniiRepository
     * @param validator - validatorul pentru entitatea de tip Prietenie
     * @param filename - numele fisierului
     */
    public PrieteniiRepository(Validator<Prietenie> validator, String filename) {
        super(validator, filename);
    }

    @Override
    public Prietenie createEntity(String line) {
        String[] splited = line.split(";");
        Prietenie p = new Prietenie(Long.parseLong(splited[1]), Long.parseLong(splited[2]), Integer.parseInt(splited[3]));
        p.setId(Long.parseLong(splited[0]));
        return p;
    }

    @Override
    public String saveEntity(Prietenie entity) {
        String s = entity.getId() + ";" + entity.getIdUtilizator1() + ";" + entity.getIdUtilizator2();
        return s;
    }
}
