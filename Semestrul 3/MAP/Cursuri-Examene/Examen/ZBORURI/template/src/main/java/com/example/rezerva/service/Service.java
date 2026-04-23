package com.example.rezerva.service;

import com.example.rezerva.domain.Client;
import com.example.rezerva.domain.Flight;
import com.example.rezerva.domain.Ticket;
import com.example.rezerva.repo.ClientRepo;
import com.example.rezerva.repo.FlightRepo;
import com.example.rezerva.repo.TicketRepo;
import com.example.rezerva.utils.events.EntityChangeEvent;
import com.example.rezerva.utils.observer.Observable;
import com.example.rezerva.utils.observer.ObservableImplementat;
import com.example.rezerva.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Service extends ObservableImplementat {
    private ClientRepo clientRepo;
    private FlightRepo flightRepo;
    private TicketRepo ticketRepo;
    //private List<Observer<EntityChangeEvent>> observers= new ArrayList<>();

    public Service(ClientRepo clientRepo,FlightRepo flightRepo,TicketRepo ticketRepo) {
        this.clientRepo = clientRepo;
        this.flightRepo=flightRepo;
        this.ticketRepo=ticketRepo;
    }

    public List<Client> getLocations() {
        return clientRepo.getAll();
    }
    public List<Flight> getFlights() {
        return flightRepo.getAll();
    }
    public List<Ticket> getTickets() {return ticketRepo.getAll();}

    public Client getClientByUsername(String username){
        for(Client c: clientRepo.getAll())
            if(c.getUsername().equals(username))
                return c;
        return null;
    }

    public void adaugaTicket(String username, Long idFlight){
        Ticket ticket=new Ticket(username,idFlight,LocalDateTime.now());
        ticketRepo.adauga(ticket);
    }
}
