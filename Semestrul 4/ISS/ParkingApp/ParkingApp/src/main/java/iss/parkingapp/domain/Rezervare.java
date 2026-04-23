package iss.parkingapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;


@Entity
public class Rezervare {
    private Long id_rezervare;
    private Long id_utilizator;
    private Long id_loc_parcare;
    private Long id_masina;
    private LocalDateTime data_rezervare;
    private LocalDateTime data_sfarsit_rezervare;
    private String status;

    public Rezervare(Long id_utilizator, Long id_loc_parcare, Long id_masina, LocalDateTime data_rezervare, LocalDateTime data_sfarsit_rezervare, String status) {
        this.id_utilizator = id_utilizator;
        this.id_masina = id_masina;
        this.id_loc_parcare = id_loc_parcare;
        this.data_rezervare = data_rezervare;
        this.data_sfarsit_rezervare = data_sfarsit_rezervare;
        this.status = status;
    }

    public Rezervare() {}

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    public Long getId_rezervare() {
        return id_rezervare;
    }
    public void setId_rezervare(Long id) {
        this.id_rezervare = id;
    }

    @NotNull
    public Long getId_utilizator() {
        return id_utilizator;
    }
    public void setId_utilizator(Long id_utilizator) {
        this.id_utilizator = id_utilizator;
    }

    @NotNull
    public Long getId_masina() {
        return id_masina;
    }
    public void setId_masina(Long id_masina) {
        this.id_masina = id_masina;
    }

    @NotNull
    public Long getId_loc_parcare() {
        return id_loc_parcare;
    }
    public void setId_loc_parcare(Long id_loc_parcare) {
        this.id_loc_parcare = id_loc_parcare;
    }

    @NotNull
    public LocalDateTime getData_rezervare() {
        return data_rezervare;
    }
    public void setData_rezervare(LocalDateTime data_rezervare) {
        this.data_rezervare = data_rezervare;
    }

    @NotNull
    public LocalDateTime getData_sfarsit_rezervare() {
        return data_sfarsit_rezervare;
    }
    public void setData_sfarsit_rezervare(LocalDateTime data_sfarsit_rezervare) {
        this.data_sfarsit_rezervare = data_sfarsit_rezervare;
    }

    @NotNull
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rezervare rezervare = (Rezervare) o;
        return Objects.equals(id_utilizator, rezervare.id_utilizator) && Objects.equals(id_masina, rezervare.id_masina) && Objects.equals(id_loc_parcare, rezervare.id_loc_parcare) && Objects.equals(data_rezervare, rezervare.data_rezervare) && Objects.equals(data_sfarsit_rezervare, rezervare.data_sfarsit_rezervare) && Objects.equals(status, rezervare.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_utilizator, id_masina, id_loc_parcare, data_rezervare, data_sfarsit_rezervare, status);
    }
}
