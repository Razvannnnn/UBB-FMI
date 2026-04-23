package org.example.reteasocializare.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Comunitati {

    Network network;
    HashMap<Long, List<Long>> comunitati;

    public Comunitati(Network network) {
        this.network = network;
    }

    /**
     * Metoda care returneaza numarul de comunitati din retea
     * @return numarul de comunitati din retea
     */
    public int numarComunitati() {
        comunitati = new HashMap<Long, List<Long>>();

        network.getUtilizatori().forEach(utilizator -> {
            List<Long> prieteni = new ArrayList<>();
            network.getPrietenii().forEach(prietenie -> {
                if(prietenie.getIdUtilizator1().equals(utilizator.getId())) {
                    prieteni.add(prietenie.getIdUtilizator2());
                }
                if(prietenie.getIdUtilizator2().equals(utilizator.getId())) {
                    prieteni.add(prietenie.getIdUtilizator1());
                }
            });
            if(!prieteni.isEmpty()) {
                comunitati.put(utilizator.getId(), prieteni);
            }
        });

        List<Long> iduri = new ArrayList<>();
        network.getUtilizatori().forEach(utilizator -> {
            iduri.add(utilizator.getId());
        });

        AtomicInteger numarComunitati = new AtomicInteger();
        HashMap<Long, Boolean> vizitat = new HashMap<Long, Boolean>();
        iduri.forEach(i -> {
            if(!vizitat.containsKey(i)) {
                numarComunitati.getAndIncrement();
                vizitat.put(i, true);
                dfs(i, vizitat, null);
            }
        });
        return numarComunitati.get();
    }

    /**
     * Metoda care returneaza cea mai sociabila comunitate
     * @return lista de id-uri a celei mai sociabile comunitati
     */
    public List<Long> ceaMaiSociabila() {
        comunitati = new HashMap<Long, List<Long>>();
        final List<Long>[] maxi = new List[]{new ArrayList<>()};

        network.getUtilizatori().forEach(utilizator -> {
            List<Long> prieteni = new ArrayList<>();
            network.getPrietenii().forEach(prietenie -> {
                if(prietenie.getIdUtilizator1().equals(utilizator.getId())) {
                    prieteni.add(prietenie.getIdUtilizator2());
                }
                if(prietenie.getIdUtilizator2().equals(utilizator.getId())) {
                    prieteni.add(prietenie.getIdUtilizator1());
                }
            });
            if(!prieteni.isEmpty()) {
                comunitati.put(utilizator.getId(), prieteni);
            }
        });

        List<Long> iduri = new ArrayList<>();
        network.getUtilizatori().forEach(utilizator -> {
            iduri.add(utilizator.getId());
        });

        HashMap<Long, Boolean> vizitat = new HashMap<Long, Boolean>();
        iduri.forEach(i -> {
            if(!vizitat.containsKey(i)) {
                List<Long> comunitate = new ArrayList<>();
                vizitat.put(i, true);
                dfs(i, vizitat, comunitate);
                if(comunitate.size() > maxi[0].size()) {
                    maxi[0] = comunitate;
                }
            }
        });

        return maxi[0];
    }

    /**
     * Metoda care parcuge graful in adancime
     * @param i - nodul de la care incepe parcurgerea
     * @param vizitat - nodurile vizitate
     * @param comunitate - lista de id-uri a comunitatii
     */
    private void dfs(Long i, HashMap<Long, Boolean> vizitat, List<Long> comunitate) {
        if(comunitate != null) {
            comunitate.add(i);
        }
        if(comunitati.containsKey(i)) {
            comunitati.get(i).forEach(j -> {
                if(!vizitat.containsKey(j)) {
                    vizitat.put(j, true);
                    dfs(j, vizitat, comunitate);
                }
            });
        }
    }
}
