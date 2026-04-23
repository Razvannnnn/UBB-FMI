package org.example.reteasocializare.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class Utilizator extends Entity<Long> {

    private String numeUtilizator;
    private String nume;
    private String prenume;
    private String password;
    private List<Utilizator> prieteni;

    /**
     * Constructor pentru clasa Utilizator
     * @param nume numele utilizatorului
     * @param prenume prenumele utilizatorului
     */
    public Utilizator(String numeUtilizator, String nume, String prenume, String password) {
        this.numeUtilizator = numeUtilizator;
        this.nume = nume;
        this.prenume = prenume;
        this.password = password;
        prieteni = new Vector<Utilizator>();
    }

    /**
     * Getter pentru nume
     * @return
     */
    public String getNume() {
        return nume;
    }

    /**
     * Getter pentru prenume
     * @return
     */
    public String getPrenume() {
        return prenume;
    }

    /**
     * Getter pentru lista de prieteni
     * @return
     */
    public List<Utilizator> getPrieteni() {
        return prieteni;
    }

    /**
     * Getter pentru numeUtilizator
     * @return
     */
    public String getNumeUtilizator() { return numeUtilizator; }

    /**
     * Getter pentru password
     * @return
     */
    public String getPassword() { return password; }

    /**
     * Adauga un prieten in lista de prieteni
     * @param prieten
     */
    public void addPrieten(Utilizator prieten) {
        prieteni.add(prieten);
    }

    /**
     * Sterge un prieten din lista de prieteni
     * @param prieten
     */
    public void removePrieten(Utilizator prieten) {
        prieteni.remove(prieten);
    }

    /**
     * Setter pentru nume
     * @param nume
     */
    public void setNume(String nume) {
        this.nume = nume;
    }

    /**
     * Setter pentru prenume
     * @param prenume
     */
    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    /**
     * Setter pentru lista de prieteni
     * @param prieteni
     */
    public void setPrieteni(List<Utilizator> prieteni) {
        this.prieteni = prieteni;
    }

    /**
     * Setter pentru numeUtilizator
     * @param numeUtilizator
     */
    public void setNumeUtilizator(String numeUtilizator) { this.numeUtilizator = numeUtilizator; }

    /**
     * Setter pentru password
     * @param password
     */
    public void setPassword(String password) { this.password = password; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilizator that = (Utilizator) o;
        return Objects.equals(numeUtilizator, that.numeUtilizator) && Objects.equals(nume, that.nume) && Objects.equals(prenume, that.prenume) && Objects.equals(password, that.password) && Objects.equals(prieteni, that.prieteni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeUtilizator, nume, prenume, password, prieteni);
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "numeUtilizator='" + numeUtilizator + '\'' +
                ", nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", password='" + password + '\'' +
                ", prieteni=" + prieteni +
                '}';
    }
}
