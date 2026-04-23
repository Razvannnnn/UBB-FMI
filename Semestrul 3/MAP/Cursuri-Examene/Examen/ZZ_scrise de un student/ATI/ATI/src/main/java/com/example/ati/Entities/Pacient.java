package com.example.ati.Entities;

import java.util.Objects;

public class Pacient {
    private String cnp;
    private Long varsta;
    private String prematur;
    private String diagnostic;
    private Long gravitate;

    public Pacient(String cnp, Long varsta, String prematur, String diagnostic, Long gravitate)
    {
        this.cnp = cnp;
        this.varsta = varsta;
        this.prematur = prematur;
        this.diagnostic = diagnostic;
        this.gravitate = gravitate;
    }

    public String getCnp()
    {
        return cnp;
    }


    public Long getGravitate()
    {
        return gravitate;
    }


    public String getDiagnostic()
    {
        return diagnostic;
    }

}
