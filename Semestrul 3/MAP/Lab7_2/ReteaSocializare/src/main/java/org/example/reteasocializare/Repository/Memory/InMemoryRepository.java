package org.example.reteasocializare.Repository.Memory;

import org.example.reteasocializare.Domain.Entity;
import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Domain.Validators.Validator;
import org.example.reteasocializare.Domain.Validators.ValidatorException;
import org.example.reteasocializare.Repository.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private Validator<E> validator;
    protected Map<ID, E> entities;

    /**
     * Contructor pentru clasa InMemoryRepository
     * @param validator - validatorul pentru entitatea de tip E
     */
    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID, E>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        if(id == null) {
            throw new IllegalArgumentException("ID CANNOT BE NULL!");
        }
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) throws ValidatorException {
        if(entity == null) {
            throw new IllegalArgumentException("ENTITY CANNOT BE NULL!");
        }
        validator.validate(entity);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    @Override
    public Optional<E> delete(ID id) {
        if(id == null) {
            throw new IllegalArgumentException("ID CANNOT BE NULL!");
        }
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
        if(entity == null) {
            throw new IllegalArgumentException("ENTITY CANNOT BE NULL!");
        }
        validator.validate(entity);
        entities.put(entity.getId(), entity);
        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return Optional.empty();
        }
        return Optional.of(entity);
    }
}
