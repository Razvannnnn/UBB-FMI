#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>

#define PORT 8080

// Structura pentru a stoca informațiile unui client
typedef struct {
    int sock;
    struct sockaddr_in address;
    int udp_port;
} client_t;

// Coada de clienți
client_t *waiting_clients[100]; // Coada poate conține mai mulți clienți
int client_count = 0;
pthread_mutex_t clients_mutex = PTHREAD_MUTEX_INITIALIZER;

// Functia pentru a trimite datele dintre cei doi clienti
void pair_clients(client_t *cli1, client_t *cli2) {
    // Trimitem portul UDP si IP-ul clientului 2 la clientul 1
    send(cli1->sock, &cli2->udp_port, sizeof(cli2->udp_port), 0);
    send(cli1->sock, &cli2->address, sizeof(cli2->address), 0);

    // Trimitem portul UDP si IP-ul clientului 1 la clientul 2
    send(cli2->sock, &cli1->udp_port, sizeof(cli1->udp_port), 0);
    send(cli2->sock, &cli1->address, sizeof(cli1->address), 0);
}

// Functia pentru gestionarea clientilor
void *handle_client(void *arg) {
    client_t *cli = (client_t *)arg;
    int udp_port;

    // Primirea portului UDP de la client
    if (recv(cli->sock, &udp_port, sizeof(udp_port), 0) < 0) {
        perror("Recv failed");
        close(cli->sock);
        free(cli);
        pthread_exit(NULL);
    }
    cli->udp_port = udp_port;

    printf("Client %s:%d connected with UDP port %d\n",
           inet_ntoa(cli->address.sin_addr), ntohs(cli->address.sin_port), cli->udp_port);

    pthread_mutex_lock(&clients_mutex);
    
    // Adăugăm clientul la coadă de clienți
    waiting_clients[client_count++] = cli;

    // Dacă avem cel puțin 2 clienți, îi împerechem
    if (client_count >= 2) {
        // Luăm primii 2 clienți din coadă
        client_t *client1 = waiting_clients[0];
        client_t *client2 = waiting_clients[1];

        // Trimitem datele între clienți
        pair_clients(client1, client2);

        // Eliberăm locuri în coadă
        for (int i = 0; i < client_count - 2; i++) {
            waiting_clients[i] = waiting_clients[i + 2];
        }
        client_count -= 2;
    }

    pthread_mutex_unlock(&clients_mutex);

    pthread_exit(NULL);
}

// Functia pentru a gestiona conexiunea serverului
int main() {
    int server_fd, new_sock;
    struct sockaddr_in server_addr, client_addr;
    socklen_t addr_size;
    pthread_t tid;

    // Crearea unui socket TCP
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        perror("Socket creation failed");
        exit(1);
    }

    // Configurarea adresei serverului
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY;
    server_addr.sin_port = htons(PORT);

    // Legarea socketului la adresa
    if (bind(server_fd, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("Bind failed");
        exit(1);
    }

    // Așteptarea pentru clienți
    if (listen(server_fd, 3) < 0) {
        perror("Listen failed");
        exit(1);
    }

    printf("Server listening on port %d\n", PORT);

    // Acceptarea conexiunilor și crearea unui thread pentru fiecare client
    while (1) {
        addr_size = sizeof(client_addr);
        new_sock = accept(server_fd, (struct sockaddr *)&client_addr, &addr_size);

        if (new_sock < 0) {
            perror("Client connection failed");
            continue;
        }

        client_t *new_client = (client_t *)malloc(sizeof(client_t));
        new_client->sock = new_sock;
        new_client->address = client_addr;

        pthread_create(&tid, NULL, handle_client, (void *)new_client);
    }

    return 0;
}
