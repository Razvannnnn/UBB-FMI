package iss.parkingapp.service;

import iss.parkingapp.domain.LocParcare;
import iss.parkingapp.domain.Masina;
import iss.parkingapp.domain.Rezervare;
import iss.parkingapp.domain.Utilizator;
import iss.parkingapp.repository.IRepoLocParcare;
import iss.parkingapp.repository.IRepoMasina;
import iss.parkingapp.repository.IRepoRezervare;
import iss.parkingapp.repository.IRepoUtilizator;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class Service {
    private IRepoUtilizator repoUtilizator;
    private IRepoLocParcare repoLocParcare;
    private IRepoRezervare repoRezervare;
    private IRepoMasina repoMasina;

    public Service(IRepoUtilizator repoUtilizator, IRepoLocParcare repoLocParcare, IRepoRezervare repoRezervare, IRepoMasina repoMasina) {
        this.repoUtilizator = repoUtilizator;
        this.repoLocParcare = repoLocParcare;
        this.repoRezervare = repoRezervare;
        this.repoMasina = repoMasina;
    }

    public Utilizator login(String email, String parola) {
        return repoUtilizator.login(email, parola);
    }

    public Utilizator findByEmail(String email) {
        return repoUtilizator.findByEmail(email);
    }

    public void addUtilizator(Utilizator utilizator) {
        if (utilizator == null) {
            throw new IllegalArgumentException("Utilizator cannot be null");
        }
        if(findByEmail(utilizator.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        repoUtilizator.add(utilizator);
    }

    public ObservableList<Rezervare> getAllRezervariForUser(Utilizator user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return repoRezervare.getAllRezervariForUser(user);
    }

    public ObservableList<Masina> getAllMasiniForUser(Utilizator user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return repoMasina.getAllForUser(user);
    }

    public void addMasina(Masina masina) {
        if (masina == null) {
            throw new IllegalArgumentException("Masina cannot be null");
        }
        repoMasina.add(masina);
    }

    public ObservableList<LocParcare> getAllLocuriParcare() {
        return repoLocParcare.findAll();
    }

    public void addRezervare(Rezervare rezervare) {
        if (rezervare == null) {
            throw new IllegalArgumentException("Rezervare cannot be null");
        }
        repoRezervare.add(rezervare);
    }

    public void addMasinaSiData(Masina masina, LocalDate value, String value1) {
        if (masina == null || value == null || value1 == null) {
            throw new IllegalArgumentException("Masina, date and time cannot be null");
        }
    }

    public void updateExpiredRezervari() {
        repoRezervare.updateExpiredRezervari();
    }

    private ObservableList<Rezervare> getAllRezervari() {
        return repoRezervare.findAll();
    }

    public ObservableList<Rezervare> getHistoryRezervariUser(Utilizator user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return repoRezervare.getHistoryRezervariUser(user);
    }

    public LocParcare getLocParcareById(Long locParcareId) {
        if (locParcareId == null) {
            throw new IllegalArgumentException("LocParcare ID cannot be null");
        }
        return repoLocParcare.findOne(locParcareId);
    }

    public Masina getMasinaById(Long masinaId) {
        if (masinaId == null) {
            throw new IllegalArgumentException("Masina ID cannot be null");
        }
        return repoMasina.findOne(masinaId);
    }

    public void stergeMasina(Masina masinaSelectata) {
        if (masinaSelectata == null) {
            throw new IllegalArgumentException("Masina cannot be null");
        }
        repoMasina.delete(masinaSelectata.getId_masina());
    }

    public boolean updateUser(Utilizator user, String nume, String prenume, String email, String parolaVeche, String parolaNoua) {
        if (user == null || nume == null || prenume == null || email == null || parolaVeche == null || parolaNoua == null) {
            throw new IllegalArgumentException("User and fields cannot be null");
        }
        if (!user.getPassword().equals(parolaVeche)) {
            return false;
        }
        user.setNume(nume);
        user.setPrenume(prenume);
        user.setEmail(email);
        user.setPassword(parolaNoua);
        repoUtilizator.update(user);
        return true;
    }
}
