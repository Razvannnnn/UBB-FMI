package org.example.reteasocializare.Domain.Validators;

public interface Validator<T> {
    /**
     * Valideaza o entitate
     * @param entity
     * @throws ValidatorException
     */
    void validate(T entity) throws ValidatorException;
}
