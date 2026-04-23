package com.example.faptebune.Service;



import com.example.faptebune.Entities.Nevoie;
import com.example.faptebune.Repository.NevoieDataBaseRepository;

import java.util.ArrayList;
import java.util.List;


public class NevoieService{

    private final NevoieDataBaseRepository nevoieDataBaseRepository;

    public NevoieService(NevoieDataBaseRepository nevoieDataBaseRepository){
        this.nevoieDataBaseRepository = nevoieDataBaseRepository;
    }


    public List<Nevoie> getAllNevoi(){
        return nevoieDataBaseRepository.getAllNevoi();
    }

    public Nevoie updateNevoie(long idNevoie, long idPersoana) {
        return nevoieDataBaseRepository.updateNevoie(idNevoie, idPersoana);
    }

    public long getMaxId(){
        return nevoieDataBaseRepository.getMaxId();
    }

    public Nevoie adaugaNevoie(Nevoie nevoie) {
        Nevoie nevoie1 = nevoieDataBaseRepository.adaugaNevoie(nevoie);
        return nevoie1;
    }
}
