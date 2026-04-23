package iss.parkingapp.repository;

import iss.parkingapp.domain.Rezervare;
import iss.parkingapp.domain.Utilizator;
import javafx.collections.ObservableList;

public interface IRepoRezervare extends IRepo<Rezervare, Long> {
    ObservableList<Rezervare> getAllRezervariForUser(Utilizator user);

    void updateExpiredRezervari();

    ObservableList<Rezervare> getHistoryRezervariUser(Utilizator user);
}
