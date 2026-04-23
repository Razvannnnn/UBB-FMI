package com.example.clinicaprivata.Entities;

import java.time.LocalDate;
import java.time.LocalTime;

public class Consultatie {
    Long id,idMedic;
    String cnpPacient,numePacient;
    LocalDate data;
    LocalTime ora;

    public Consultatie(Long id, Long idMedic, String cnpPacient, String numePacient, LocalDate data, LocalTime ora)
    {
        this.id = id;
        this.idMedic = idMedic;
        this.cnpPacient = cnpPacient;
        this.numePacient = numePacient;
        this.data = data;
        this.ora = ora;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getIdMedic()
    {
        return idMedic;
    }

    public void setIdMedic(Long idMedic)
    {
        this.idMedic = idMedic;
    }

    public String getCnpPacient()
    {
        return cnpPacient;
    }

    public void setCnpPacient(String cnpPacient)
    {
        this.cnpPacient = cnpPacient;
    }

    public String getNumePacient()
    {
        return numePacient;
    }

    public void setNumePacient(String numePacient)
    {
        this.numePacient = numePacient;
    }

    public LocalDate getData()
    {
        return data;
    }

    public void setData(LocalDate data)
    {
        this.data = data;
    }

    public LocalTime getOra()
    {
        return ora;
    }

    public void setOra(LocalTime ora)
    {
        this.ora = ora;
    }
}
