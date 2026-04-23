package com.example.ati.Serivice;

import com.example.ati.Entities.Pacient;
import com.example.ati.Entities.Pat;
import com.example.ati.Events.PatChangeEvent;
import com.example.ati.Observer.Observable;
import com.example.ati.Repository.PacientiRepository;
import com.example.ati.Repository.PaturiRepository;

import java.util.*;
import java.util.stream.Collectors;
import com.example.ati.Observer.Observer;

public class Service implements Observable<PatChangeEvent>
{
    private static PaturiRepository repo_paturi;
    private static PacientiRepository repo_pacienti;
    private List<Observer<PatChangeEvent>> obs=new ArrayList<>();

    public Service(PaturiRepository repo_paturi,PacientiRepository repo_pacienti)
    {
        this.repo_paturi=repo_paturi;
        this.repo_pacienti=repo_pacienti;
    }


    public List<Pat> getPaturi()
    {
        return repo_paturi.findAll();
    }


    public List<Pacient> getPacienti()
    {
        return repo_pacienti.findAll();
    }


    public List<Pat> getPaturiOcupate()
    {
        List<Pat> paturi=getPaturi();
        List<Pat> paturiOcupate=new ArrayList<>();
        for(Pat pat:paturi)
            if(pat.getPacient()!=null)
                paturiOcupate.add(pat);
        return paturiOcupate;
    }


    public List<Pacient> getIdPacientiInAsteptare()
    {
        List<Pat> paturi=getPaturi();
        Set<String> pacientiInternati=new HashSet<>();
        for(Pat pat:paturi)
            if(pat.getPacient()!=null)
                pacientiInternati.add(pat.getPacient());
        List<Pacient> pacienti=getPacienti();
        List<Pacient> pacientiInAsteptare=new ArrayList<>();
        for(Pacient p:pacienti)
            if(!pacientiInternati.contains(p.getCnp()))
                pacientiInAsteptare.add(p);
        List<Pacient> sorted=pacientiInAsteptare.stream().sorted((p1, p2) -> p2.getGravitate().compareTo(p1.getGravitate())).collect(Collectors.toList());
        return sorted;
    }



    public int getBusyTicNumber()
    {
        int busyBeds=0;
        List<Pat> paturi=getPaturi();
        for (Pat pat:paturi)
            if(pat.getTip().equals("1") && pat.getPacient()!=null)
                busyBeds++;
        return busyBeds;
    }


    public int getBusyTimNumber()
    {
        int busyBeds=0;
        List<Pat> paturi=getPaturi();
        for (Pat pat:paturi)
            if(pat.getTip().equals("2") && pat.getPacient()!=null)
                busyBeds++;
        return busyBeds;
    }


    public int getBusyTipNumber()
    {
        int busyBeds=0;
        List<Pat> paturi=getPaturi();
        for (Pat pat:paturi)
            if(pat.getTip().equals("3") && pat.getPacient()!=null)
                busyBeds++;
        return busyBeds;
    }


    public void atribuiePatPacient(String cnp,String tip)
    {
        List<Pat> paturi=getPaturi();
        for(Pat p:paturi)
            if(p.getPacient()==null && p.getTip().equals(tip))
            {
                repo_paturi.update(cnp,p.getId());
                break;
            }
    }


    public boolean checkBedAvailability(String tip)
    {
        List<Pat> paturi=getPaturi();
        for(Pat p:paturi)
            if(p.getTip().equals(tip) && p.getPacient()==null)
                return true;
        return false;
    }


    public boolean eliberarePat(String cnp)
    {
        List<Pat> paturi=getPaturi();
        for(Pat p:paturi)
            if(p.getPacient()!=null)
                if(p.getPacient().equals(cnp))
                {
                    repo_paturi.eliberarePat(p.getId());
                    repo_pacienti.eliberarePat(p.getPacient());
                    return true;
                }
        return false;
    }




    @Override
    public void addObserver(Observer<PatChangeEvent> e)
    {
        obs.add(e);
    }

    @Override
    public void removeObserver(Observer<PatChangeEvent> e)
    {
        obs.remove(e);
    }

    @Override
    public void notifyObservers(PatChangeEvent entity)
    {
        obs.stream().forEach(x->x.update(entity));
    }
}
