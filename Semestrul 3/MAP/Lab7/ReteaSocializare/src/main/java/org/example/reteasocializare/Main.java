package org.example.reteasocializare;

import org.example.reteasocializare.Domain.Prietenie;
import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Domain.Validators.PrietenieValidator;
import org.example.reteasocializare.Domain.Validators.UtilizatorValidator;
import org.example.reteasocializare.Repository.DB.PrietenieDBRepository;
import org.example.reteasocializare.Repository.DB.UtilizatorDBRepository;
import org.example.reteasocializare.Repository.File.PrieteniiRepository;
import org.example.reteasocializare.Repository.File.UtilizatorRepository;
import org.example.reteasocializare.Repository.Memory.InMemoryRepository;
import org.example.reteasocializare.Service.Network;
import UI.console;

import java.io.Console;

public class Main {
    public static void main(String[] args) {
        /*
        InMemoryRepository<Long, Utilizator> repositoryUtilizator = new InMemoryRepository<>(new UtilizatorValidator());
        InMemoryRepository<Long, Prietenie> repositoryPrietenie = new InMemoryRepository<>(new PrietenieValidator(repositoryUtilizator));
        */

        /*
        UtilizatorRepository utilizatorRepository = new UtilizatorRepository(new UtilizatorValidator(), "C:\\Users\\razva\\Desktop\\MAP\\Lab4\\ReteaSocializare\\src\\Repository\\File\\utilizatori.txt");
        PrieteniiRepository prieteniiRepository = new PrieteniiRepository(new PrietenieValidator(utilizatorRepository), "C:\\Users\\razva\\Desktop\\MAP\\Lab4\\ReteaSocializare\\src\\Repository\\File\\prietenii.txt");
         */

        UtilizatorDBRepository utilizatorDBRepository = new UtilizatorDBRepository("jdbc:postgresql://localhost:5432/postgres", "postgres", "parola", new UtilizatorValidator());
        PrietenieDBRepository prietenieDBRepository = new PrietenieDBRepository("jdbc:postgresql://localhost:5432/postgres", "postgres", "parola", new PrietenieValidator(utilizatorDBRepository));

        Network network = new Network(utilizatorDBRepository, prietenieDBRepository);

        console console = new console(network);
        console.run();
    }
}