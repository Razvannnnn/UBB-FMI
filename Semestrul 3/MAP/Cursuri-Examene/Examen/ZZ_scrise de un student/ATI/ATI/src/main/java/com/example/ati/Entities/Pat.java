package com.example.ati.Entities;

import java.util.Objects;

public class Pat{
    private Long id;
    private String tip;
    private String ventilatie;
    private String cnp;

    public Pat(Long id, String tip, String ventilatie, String cnp)
    {
        this.id = id;
        this.tip = tip;
        this.ventilatie = ventilatie;
        this.cnp = cnp;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTip()
    {
        return tip;
    }


    public String getPacient()
    {
        return cnp;
    }

    public void setPacient(String cnp)
    {
        this.cnp = cnp;
    }
}
