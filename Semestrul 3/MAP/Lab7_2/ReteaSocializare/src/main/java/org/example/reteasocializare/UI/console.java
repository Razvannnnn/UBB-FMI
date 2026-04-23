package org.example.reteasocializare.UI;

import org.example.reteasocializare.Domain.Prietenie;
import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Domain.Validators.ValidatorException;
import org.example.reteasocializare.Service.Comunitati;
import org.example.reteasocializare.Service.Network;

import java.util.List;
import java.util.Objects;

public class console {

    private Network network;
    private Comunitati comunitati;

    public console(Network network) {
        this.network = network;
        this.comunitati = new Comunitati(network);
    }

    void Menu() {
        System.out.println("       MENU");
        System.out.println("1. Adauga utilizator");
        System.out.println("2. Sterge utilizator");
        System.out.println("3. Adauga prietenie");
        System.out.println("4. Sterge prietenie");
        System.out.println("5. Afiseaza utilizatorii");
        System.out.println("6. Afiseaza numarul de comunitati");
        System.out.println("7. Afiseaza cea mai sociabila comunitate");
        System.out.println("0. Exit");
    }

    public void run() {
        boolean ok = true;
        Menu();
        while (ok) {
            int optiune = -1;
            while (optiune == -1) {
                System.out.println("Introduceti optiunea: ");
                try {
                    String line = System.console().readLine();
                    optiune = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    System.out.println("Optiunea trebuie sa fie un numar!");
                }
            }
            switch (optiune) {
                case 1:
                    addUtilizator();
                    break;
                case 2:
                    removeUtilizator();
                    break;
                case 3:
                    addPrietenie();
                    break;
                case 4:
                    removePrietenie();
                    break;
                case 5:
                    afisareUtilizatori();
                    break;
                case 6:
                    numarComunitati();
                    break;
                case 7:
                    ceaMaiSociabila();
                    break;
                case 0:
                    ok = false;
                    break;
                default:
                    System.out.println("Optiune invalida!");
                    break;
            }
        }
    }

    void addUtilizator() {
        try {
            System.out.println("Introduceti numele utilizatorului: ");
            String nume = System.console().readLine();
            System.out.println("Introduceti prenumele utilizatorului: ");
            String prenume = System.console().readLine();
            System.out.println("Introduceti username-ul: ");
            String usename = System.console().readLine();
            System.out.println("Introduceti parola: ");
            String password = System.console().readLine();
            network.addUtilizator(new Utilizator(usename, nume, prenume, password));
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    void removeUtilizator() {
        System.out.println("Introduceti id-ul utilizatorului: ");
        Long id = Long.parseLong(System.console().readLine());
        try {
            network.removeUtilizator(id);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    void addPrietenie() {
        System.out.println("Introduceti id-ul utilizatorului 1: ");
        String line1 = System.console().readLine();
        System.out.println("Introduceti id-ul utilizatorului 2: ");
        String line2 = System.console().readLine();
        try {
            Long id1 = Long.parseLong(line1);
            Long id2 = Long.parseLong(line2);
            Integer status = 3;
            network.addPrietenie(new Prietenie(id1, id2, status));
        } catch (NumberFormatException e) {
            System.out.println("Id-ul trebuie sa fie un numar!");
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    void removePrietenie() {
        System.out.println("Introduceti id-ul utilizatorului 1: ");
        String line1 = System.console().readLine();
        System.out.println("Introduceti id-ul utilizatorului 2: ");
        String line2 = System.console().readLine();
        try {
            Long id1 = Long.parseLong(line1);
            Long id2 = Long.parseLong(line2);
            network.removePrietenie(id1, id2);
        } catch (NumberFormatException e) {
            System.out.println("Id-ul trebuie sa fie un numar!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    void afisareUtilizatori() {
        if(network.getUtilizatori().iterator().hasNext()) {
            System.out.println("Utilizatorii sunt: ");
            for(Utilizator u : network.getUtilizatori()) {
                System.out.println("ID: " + u.getId() + " Nume: " + u.getNume() + " Prenume: " + u.getPrenume());
            }
        } else {
            System.out.println("Nu exista utilizatori!");
        }
    }

    void afisarePrietenii() {
        if(network.getUtilizatori().iterator().hasNext()) {
            for(Utilizator u: network.getUtilizatori()) {
                if(u.getPrieteni().isEmpty()) {
                    System.out.println("Utilizatorul " + u.getId() + " nu are prieteni!");
                } else {
                    System.out.println("Utilizatorul " + u.getId() + " are urmatorii prieteni: ");
                    for(Utilizator prieten : u.getPrieteni()) {
                        System.out.println("(ID: " + prieten.getId() + ") " + prieten.getNume() + " " + prieten.getPrenume());
                    }
                }
            }
        } else {
            System.out.println("Nu exista utilizatori!");
        }
    }

    void numarComunitati() {
        System.out.println("Numarul de comunitati este: " + comunitati.numarComunitati());
    }

    void ceaMaiSociabila() {
        List<Long> maxi = comunitati.ceaMaiSociabila();
        if(maxi.isEmpty()) {
            System.out.println("Nu exista comunitati!");
        } else maxi.forEach(System.out::println);
    }
}
