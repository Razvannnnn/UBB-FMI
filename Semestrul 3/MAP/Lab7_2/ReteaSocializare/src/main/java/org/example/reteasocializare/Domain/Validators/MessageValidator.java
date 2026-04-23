package org.example.reteasocializare.Domain.Validators;

import org.example.reteasocializare.Domain.Message;

public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidatorException {
        String err = "";

        if (entity.getMessage().equals("")) {
            err += "The message cannot be void";
        }

        if (!err.equals("")) {
            throw new ValidatorException(err);
        }
    }
}
