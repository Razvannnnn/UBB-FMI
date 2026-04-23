package iss.parkingapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
public class Utilizator {
    private Long id_utilizator;
    private String nume;
    private String prenume;
    private String email;
    private String password;
    private String rol;

    public Utilizator(String nume, String prenume, String email, String password) {
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.password = password;
    }

    public Utilizator() {}

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    public Long getId_utilizator() {
        return id_utilizator;
    }
    public void setId_utilizator(Long id) {
        this.id_utilizator = id;
    }

    @NotNull
    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }

    @NotNull
    public String getPrenume() {
        return prenume;
    }
    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    @NotNull
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull
    public String getPassword() {
        return password;
    }
    public void setPassword(String parola) {
        this.password = parola;
    }

    @NotNull
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilizator that = (Utilizator) o;
        return Objects.equals(nume, that.nume) && Objects.equals(prenume, that.prenume) && Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nume, prenume, email, password);
    }
}
