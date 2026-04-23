package org.example.reteasocializare.Domain.Validators;

import org.example.reteasocializare.Domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {

    @Override
    public void validate(Utilizator entity) throws ValidatorException {
        if(entity.getNume().equals("") || entity.getPrenume().equals(""))
            throw new ValidatorException("Utilizatorul nu este valid");
    }
}
