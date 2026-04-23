#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>

#define PORT 8080

typedef struct {
    int sock;
    struct sockaddr_in address;
    int udp_port;
} client_t;

client_t *waiting_clients[100];
int client_count = 0;
pthread_mutex_t clients_mutex = PTHREAD_MUTEX_INITIALIZER;

void pair_clients(client_t *cli1, client_t *cli2) {
    send(cli1->sock, &cli2->udp_port, sizeof(cli2->udp_port), 0);
    send(cli1->sock, &cli2->address, sizeof(cli2->address), 0);
    send(cli2->sock, &cli1->udp_port, sizeof(cli1->udp_port), 0);
    send(cli2->sock, &cli1->address, sizeof(cli1->address), 0);
}

void *handle_client(void *arg) {
    client_t *cli = (client_t *)arg;
    int udp_port;

    if (recv(cli->sock, &udp_port, sizeof(udp_port), 0) < 0) {
        perror("Eroare la primirea datelor");
        close(cli->sock);
        free(cli);
        pthread_exit(NULL);
    }
    cli->udp_port = udp_port;

    printf("Client %s:%d conectat cu port UDP %d\n",
           inet_ntoa(cli->address.sin_addr), ntohs(cli->address.sin_port), cli->udp_port);

    pthread_mutex_lock(&clients_mutex);
    
    waiting_clients[client_count++] = cli;

    if (client_count >= 2) {
        client_t *client1 = waiting_clients[0];
        client_t *client2 = waiting_clients[1];
        pair_clients(client1, client2);

        for (int i = 0; i < client_count - 2; i++) {
            waiting_clients[i] = waiting_clients[i + 2];
        }
        client_count -= 2;
    }

    pthread_mutex_unlock(&clients_mutex);

    pthread_exit(NULL);
}

int main() {
    int server_fd, new_sock;
    struct sockaddr_in server_addr, client_addr;
    socklen_t addr_size;
    pthread_t tid;

    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        perror("Eroare la crearea socketului");
        exit(1);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY;
    server_addr.sin_port = htons(PORT);

    if (bind(server_fd, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("Eroare la bind");
        exit(1);
    }

    if (listen(server_fd, 3) < 0) {
        perror("Eroare la ascultare");
        exit(1);
    }

    printf("Serverul asculta pe portul %d\n", PORT);

    while (1) {
        addr_size = sizeof(client_addr);
        new_sock = accept(server_fd, (struct sockaddr *)&client_addr, &addr_size);

        if (new_sock < 0) {
            perror("Eroare la conectarea clientului");
            continue;
        }

        client_t *new_client = (client_t *)malloc(sizeof(client_t));
        new_client->sock = new_sock;
        new_client->address = client_addr;

        pthread_create(&tid, NULL, handle_client, (void *)new_client);
    }

    return 0;
}
