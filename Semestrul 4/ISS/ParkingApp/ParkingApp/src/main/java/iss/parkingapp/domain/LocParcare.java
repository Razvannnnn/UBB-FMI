package iss.parkingapp.domain;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class LocParcare {
    private Long id_loc_parcare;
    private StatusLoc status;
    private String pozitie;
    private Integer taxa;

    public LocParcare(StatusLoc status, String pozitie, Integer taxa) {
        this.status = status;
        this.pozitie = pozitie;
        this.taxa = taxa;
    }

    public LocParcare() {}

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    public Long getId_loc_parcare() {
        return id_loc_parcare;
    }

    public void setId_loc_parcare(Long id) {
        this.id_loc_parcare = id;
    }

    @NotNull
    public String getPozitie() {
        return pozitie;
    }
    public void setPozitie(String pozitie) {
        this.pozitie = pozitie;
    }

    @NotNull
    public StatusLoc getStatus() {
        return status;
    }
    public void setStatus(StatusLoc status) {
        this.status = status;
    }

    @NotNull
    public Integer getTaxa() {
        return taxa;
    }
    public void setTaxa(Integer taxa) {
        this.taxa = taxa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocParcare that = (LocParcare) o;
        return status == that.status && Objects.equals(pozitie, that.pozitie) && Objects.equals(taxa, that.taxa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, pozitie, taxa);
    }

    @Override
    public String toString() {
        return "LocParcare{" +
                "status=" + status +
                ", pozitie='" + pozitie + '\'' +
                ", taxa=" + taxa +
                '}';
    }
}
