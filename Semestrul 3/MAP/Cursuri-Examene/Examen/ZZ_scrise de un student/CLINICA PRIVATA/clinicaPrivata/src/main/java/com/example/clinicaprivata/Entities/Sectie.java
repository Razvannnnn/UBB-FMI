package com.example.clinicaprivata.Entities;

public class Sectie {
    Long id,idSefDeSectie,pretPerConsultatie,durataMaximaConsultatie;
    String nume;

    public Sectie(Long id, Long idSefDeSectie, Long pretPerConsultatie, Long durataMaximaConsultatie, String nume)
    {
        this.id = id;
        this.idSefDeSectie = idSefDeSectie;
        this.pretPerConsultatie = pretPerConsultatie;
        this.durataMaximaConsultatie = durataMaximaConsultatie;
        this.nume = nume;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getIdSefDeSectie()
    {
        return idSefDeSectie;
    }

    public void setIdSefDeSectie(Long idSefDeSectie)
    {
        this.idSefDeSectie = idSefDeSectie;
    }

    public Long getPretPerConsultatie()
    {
        return pretPerConsultatie;
    }

    public void setPretPerConsultatie(Long pretPerConsultatie)
    {
        this.pretPerConsultatie = pretPerConsultatie;
    }

    public Long getDurataMaximaConsultatie()
    {
        return durataMaximaConsultatie;
    }

    public void setDurataMaximaConsultatie(Long durataMaximaConsultatie)
    {
        this.durataMaximaConsultatie = durataMaximaConsultatie;
    }

    public String getNume()
    {
        return nume;
    }

    public void setNume(String nume)
    {
        this.nume = nume;
    }
}
