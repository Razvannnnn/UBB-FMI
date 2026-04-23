# Aplicatie Masini

## Descriere

Aceasta aplicatie este un proiect simplu care demonstreaza folosirea unei interfete de tip **master-detail**, comunicarea cu un **REST service** si integrarea de **web sockets** pentru notificari de la server.

## Masina ca Item:

- Nume
- Cai putere
- Cilindri
- Greutate
- Numar usi
- Culoare
- Data fabricatiei
- etc.

## Functionalitati

- **Logare utilizator** – utilizatorul se autentifica pentru a accesa aplicatia.
- **Afisare lista masini** – dupa logare, se afiseaza o lista de masini primita de la server printr-un REST service.
- **Editare masina** – la selectarea unei masini din lista, utilizatorul poate edita numele acesteia.
- **Detalii masina (in lucru)** – urmeaza sa fie adaugata o pagina cu mai multe detalii despre masina selectata.
- **Notificari in timp real** – aplicatia va folosi web sockets pentru a primi notificari de la server.

## Tehnologii folosite

- REST API pentru obtinerea datelor
- Web Sockets pentru notificari
- Master-Detail UI pentru interactiunea cu lista si detaliile masinilor

## Status

Aplicatia este in dezvoltare. Momentan include logarea, afisarea listei de masini si posibilitatea de a edita numele unei masini.
