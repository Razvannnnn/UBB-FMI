package org.example.reteasocializare.Service;

import org.example.reteasocializare.Domain.Message;
import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Repository.DB.MessageDBRepository;

import java.time.LocalDateTime;
import java.util.List;

public class ServiceMessage {

    private final MessageDBRepository repository;

    public ServiceMessage(MessageDBRepository repository) {
        this.repository = repository;
    }

    public List<Message> getMessages(Utilizator from, Utilizator to) {
        return repository.getMessages(from, to);
    }

    public void addMessage(String msg, Long idFrom, Long idTo, LocalDateTime date) {
        Message message = new Message(msg, idFrom, idTo, date);
        repository.addMessage(message);
    }

}
