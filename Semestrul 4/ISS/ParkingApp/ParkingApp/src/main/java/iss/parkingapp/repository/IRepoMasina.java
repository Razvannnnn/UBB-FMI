package iss.parkingapp.repository;

import iss.parkingapp.domain.Masina;
import iss.parkingapp.domain.Utilizator;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public interface IRepoMasina extends IRepo<Masina, Long> {
    ObservableList<Masina> getAllForUser(Utilizator user);
}
