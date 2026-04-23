package com.example.rezerva.service;

import com.example.rezerva.domain.Cautari;
import com.example.rezerva.domain.City;
import com.example.rezerva.domain.TrainStation;
import com.example.rezerva.repo.CityRepo;
import com.example.rezerva.repo.TrainStationRepo;
import com.example.rezerva.utils.observer.ObservableImplementat;

import java.util.ArrayList;
import java.util.List;

public class Service extends ObservableImplementat {
    private CityRepo cityRepo;
    private TrainStationRepo trainStationRepo;
    private List<Cautari> cautari=new ArrayList<>();

    public Service(CityRepo cityRepo, TrainStationRepo trainStationRepo) {
        this.cityRepo=cityRepo;
        this.trainStationRepo=trainStationRepo;
    }

    public List<City> getCities() {
        return cityRepo.getAll();
    }
    public List<TrainStation> getTrainStations() {
        return trainStationRepo.getAll();
    }

    public String getNameById(String idOras){
        for(City c: cityRepo.getAll()){
            if(c.getId().equals(idOras))
                return c.getNume();
        }
        return null;
    }

    public void addCautare(String idOm, String idDeparture, String idDestination){
        List<Cautari> cautariBun=new ArrayList<>();
        for(Cautari caut: cautari){
            if(!caut.getIdOm().equals(idOm))
                cautariBun.add(caut);
        }

        Cautari c=new Cautari(idOm,idDeparture,idDestination);
        cautari.clear();
        cautari.addAll(cautariBun);
        cautari.add(c);

        for(Cautari ca: cautari){
            System.out.println(ca);
        }
    }

    public Integer nrPersoane(String from,String to){
        Integer cnt=0;
        for(Cautari c: cautari)
        {
            System.out.println("IN NRPERSOANE" + c.toString());
            System.out.println("FROM: " +  from + " DEPARTURE: "+c.getIdDeparture());
            if(c.getIdDeparture().equals(from) && c.getIdDestination().equals(to)) {
                cnt++;
                System.out.println("SI AICI INTRA");

            }
        }
        return cnt-1;
    }
}
