package org.example.zboruri.Service;

import org.example.zboruri.Domain.Client;
import org.example.zboruri.Domain.Flight;
import org.example.zboruri.Domain.Ticket;
import org.example.zboruri.Repository.RepoClient;
import org.example.zboruri.Repository.RepoFlight;
import org.example.zboruri.Repository.RepoTicket;
import org.example.zboruri.Utils.Observable;
import org.example.zboruri.Utils.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Service implements Observable {

    private final RepoClient repoClient;
    private final RepoFlight repoFlight;
    private final RepoTicket repoTicket;
    private final List<Observer> observers;

    public Service(RepoClient repoClient, RepoFlight repoFlight, RepoTicket repoTicket) {
        this.repoClient = repoClient;
        this.repoFlight = repoFlight;
        this.repoTicket = repoTicket;
        this.observers = new ArrayList<>();
    }

    public Iterable<Client> getAllClients() {
        return repoClient.getAll();
    }

    public Iterable<Flight> getAllFlights() {
        return repoFlight.getAll();
    }

    public Iterable<Ticket> getAllTickets() {
        return repoTicket.getAll();
    }




    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer:observers){
            observer.update();
        }
    }

    public Iterable<Flight> getAllFlightsFilter(String from, String to, LocalDateTime date) {
        return repoFlight.getAllFlightsFilter(from, to, date);
    }

    public boolean login(String username) {
        return repoClient.login(username);
    }

    public Client getClient(String username) {
        return repoClient.getClient(username);
    }

    public Iterable<String> getFromCities() {
        return repoFlight.getAllFromCities();
    }

    public Iterable<String> getToCities() {
        return repoFlight.getAllToCities();
    }

    public void buyTicket(Flight flight, Client client) {
        repoTicket.save(flight, client);
        notifyObservers();
    }

    public Integer numberOfTicketsForFlight(Long flightId) {
        return repoTicket.numberOfTicketsForFlight(flightId);
    }
}
