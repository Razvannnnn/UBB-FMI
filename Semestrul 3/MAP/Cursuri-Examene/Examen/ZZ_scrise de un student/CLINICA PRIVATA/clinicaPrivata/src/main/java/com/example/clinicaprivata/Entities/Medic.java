package com.example.clinicaprivata.Entities;

public class Medic {
    Long id,idSectie,vechime;
    String nume,rezident;

    public Medic(Long id, Long idSectie, Long vechime, String nume, String rezident)
    {
        this.id = id;
        this.idSectie = idSectie;
        this.vechime = vechime;
        this.nume = nume;
        this.rezident = rezident;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getIdSectie()
    {
        return idSectie;
    }

    public void setIdSectie(Long idSectie)
    {
        this.idSectie = idSectie;
    }

    public Long getVechime()
    {
        return vechime;
    }

    public void setVechime(Long vechime)
    {
        this.vechime = vechime;
    }

    public String getNume()
    {
        return nume;
    }

    public void setNume(String nume)
    {
        this.nume = nume;
    }

    public String getRezident()
    {
        return rezident;
    }

    public void setRezident(String rezident)
    {
        this.rezident = rezident;
    }
}
