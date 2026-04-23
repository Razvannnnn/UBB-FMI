#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>

#define MAX_CLIENTS 2
#define PORT 8080

// Structura pentru a stoca informațiile unui client
typedef struct {
    int sock;
    struct sockaddr_in address;
    int udp_port;
} client_t;

// Variabile globale
client_t *clients[MAX_CLIENTS];
pthread_mutex_t clients_mutex = PTHREAD_MUTEX_INITIALIZER;

// Functia pentru gestionarea clientilor
void *handle_client(void *arg) {
    client_t *cli = (client_t *)arg;
    char buffer[1024];

    // Primirea portului UDP de la client
    if (recv(cli->sock, &cli->udp_port, sizeof(cli->udp_port), 0) < 0) {
        perror("Recv failed");
        close(cli->sock);
        free(cli);
        pthread_exit(NULL);
    }

    printf("Client %s:%d connected with UDP port %d\n",
           inet_ntoa(cli->address.sin_addr), ntohs(cli->address.sin_port), cli->udp_port);

    // Asteptam sa avem doi clienti
    pthread_mutex_lock(&clients_mutex);

    if (clients[0] != NULL && clients[1] == NULL) {
        clients[1] = cli;
        // Imperecherea clientilor
        send(clients[0]->sock, &cli->udp_port, sizeof(cli->udp_port), 0);
	send(clients[0]->sock, &cli->address, sizeof(cli->address), 0);
        send(cli->sock, &clients[0]->udp_port, sizeof(clients[0]->udp_port), 0);
	send(cli->sock, &clients[0]->address, sizeof(clients[0]->address), 0);
    } else if (clients[0] == NULL) {
        clients[0] = cli;
    } else {
        printf("More than two clients are connected, rejecting.\n");
        send(cli->sock, "Server is full.", 15, 0);
        close(cli->sock);
        free(cli);
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
