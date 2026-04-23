package com.example.clinicaprivata.Service;

import com.example.clinicaprivata.Entities.Consultatie;
import com.example.clinicaprivata.Entities.Medic;
import com.example.clinicaprivata.Entities.Sectie;
import com.example.clinicaprivata.Events.ChangeEventType;
import com.example.clinicaprivata.Events.ConsultatieChangeEvent;
import com.example.clinicaprivata.Observer.Observable;
import com.example.clinicaprivata.Observer.Observer;
import com.example.clinicaprivata.Repository.RepositoryConsultatii;
import com.example.clinicaprivata.Repository.RepositoryMedici;
import com.example.clinicaprivata.Repository.RepositorySectii;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Service implements Observable<ConsultatieChangeEvent> {
    private static RepositorySectii repo_sectii;
    private static RepositoryMedici repo_medici;
    private static RepositoryConsultatii repo_consultatii;
    //private List<Observer<PatChangeEvent>> obs=new ArrayList<>();
    private List<Observer<ConsultatieChangeEvent>> obs=new ArrayList<>();


    public Service(RepositorySectii repo_sectii,RepositoryMedici repo_medici,RepositoryConsultatii repo_consultatii)
    {
        this.repo_sectii=repo_sectii;
        this.repo_medici=repo_medici;
        this.repo_consultatii=repo_consultatii;
    }


    public List<Sectie> getSectii()
    {
        return repo_sectii.findAll();
    }


    public List<Medic> getMedici()
    {
        return repo_medici.findAll();
    }

    public List<Consultatie> getConsultatii()
    {
        return repo_consultatii.findAll();
    }


    public List<Consultatie> sortareConsultatii(Medic medic)
    {
        List<Consultatie> consultatii=getConsultatii();
        List<Consultatie> consultatiiRamase=new ArrayList<>();
        List<Consultatie> rez=new ArrayList<>();
        for(Consultatie c:consultatii)
            if(c.getData().compareTo(LocalDate.now())>0)
                consultatiiRamase.add(c);
            else if(c.getData().compareTo(LocalDate.now())==0)
                if(c.getOra().compareTo(LocalTime.now())>0)
                    consultatiiRamase.add(c);
        for(Consultatie c:consultatiiRamase)
            if(Objects.equals(c.getIdMedic(), medic.getId()))
                rez.add(c);
        for(int i=0; i<rez.size()-1; i++)
            for(int j=i+1; j<rez.size(); j++)
                if(rez.get(i).getData().compareTo(rez.get(j).getData())>0)
                    Collections.swap(rez,i,j);
                else if(rez.get(i).getData().compareTo(rez.get(j).getData())==0)
                {
                    if(rez.get(i).getOra().compareTo(rez.get(j).getOra())>0)
                        Collections.swap(rez,i,j);
                }
        return rez;
    }


    public List<Medic> getMediciSectie(Long idSectie)
    {
        List<Medic> medici=repo_medici.findAll();
        List<Medic> mediciSectie=new ArrayList<>();
        for(Medic m:medici)
            if(m.getIdSectie()==idSectie)
                mediciSectie.add(m);
        return mediciSectie;
    }


    public void adaugaConsultatie(Long idMedic,String cnp,String nume,LocalDate data,LocalTime ora)
    {
        List<Consultatie> consultatii=getConsultatii();
        Long max=0L;
        for(Consultatie c:consultatii)
            if(c.getId()>max)
                max=c.getId();
        String str_data=data.toString();
        String str_ora=ora.toString();
        Consultatie c1=new Consultatie(max+1,idMedic,cnp,nume,data,ora);
        repo_consultatii.adaugaConsultatie(max+1,idMedic,cnp,nume,str_data,str_ora);
        notifyObservers(new ConsultatieChangeEvent(ChangeEventType.ADD,c1));
    }


    @Override
    public void addObserver(Observer<ConsultatieChangeEvent> e)
    {
        obs.add(e);
    }

    @Override
    public void removeObserver(Observer<ConsultatieChangeEvent> e)
    {
        obs.remove(e);
    }

    @Override
    public void notifyObservers(ConsultatieChangeEvent entity)
    {
        obs.stream().forEach(x->x.update(entity));
    }
}
