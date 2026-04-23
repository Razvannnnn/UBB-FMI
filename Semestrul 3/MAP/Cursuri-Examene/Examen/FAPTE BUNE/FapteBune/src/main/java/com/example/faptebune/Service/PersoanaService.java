package com.example.faptebune.Service;


import com.example.faptebune.Entities.Persoana;
import com.example.faptebune.Repository.PersoanaDataBaseRepository;
import java.util.List;

public class PersoanaService {

    private PersoanaDataBaseRepository persoanaDataBaseRepository;

    public PersoanaService(PersoanaDataBaseRepository persoanaDataBaseRepository){
        this.persoanaDataBaseRepository = persoanaDataBaseRepository;
    }

    public Persoana addPersoana(Persoana persoana) {
        return persoanaDataBaseRepository.addPersoana(persoana);
    }

    public boolean verifyIfAPersonExist(String username){
        return persoanaDataBaseRepository.verifyIfAPersonExist(username);
    }

    public long getMaxId(){
        return persoanaDataBaseRepository.getMaxId();
    }

    public List<Persoana> getAllPersoane() {
        return persoanaDataBaseRepository.getAllPersoane();
    }


    public Persoana findOnePersoana(long idPersoana){
        return persoanaDataBaseRepository.findOnePersoana(idPersoana);
    }

}
