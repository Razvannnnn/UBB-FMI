package iss.parkingapp.repository;

import iss.parkingapp.domain.Utilizator;

public interface IRepoUtilizator extends IRepo<Utilizator, Long> {
    Utilizator login(String email, String parola);
    Utilizator findByEmail(String email);
}
