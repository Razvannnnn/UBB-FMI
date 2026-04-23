package org.example.reteasocializare;

import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Domain.Validators.PrietenieValidator;
import org.example.reteasocializare.Domain.Validators.UtilizatorValidator;
import org.example.reteasocializare.Repository.DB.UtilizatorDBRepository;
import org.example.reteasocializare.Repository.DB.PrietenieDBRepository;
import org.example.reteasocializare.Service.Network;
import org.example.reteasocializare.UI.console;


public class Main {
    public static void main(String[] args) {

        UtilizatorDBRepository utilizatorDBRepository = new UtilizatorDBRepository("jdbc:postgresql://localhost:5432/postgres", "postgres", "parola", new UtilizatorValidator());
        PrietenieDBRepository prietenieDBRepository = new PrietenieDBRepository("jdbc:postgresql://localhost:5432/postgres", "postgres", "parola", new PrietenieValidator(utilizatorDBRepository));

        Network network = new Network(utilizatorDBRepository, prietenieDBRepository);

        console console = new console(network);

        console.run();
    }

}
