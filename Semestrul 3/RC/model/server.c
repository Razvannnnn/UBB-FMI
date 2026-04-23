// server.c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>

#define TCP_PORT 12345
#define MAX_CLIENTS 10
#define BUFFER_SIZE 1024

typedef struct {
    struct sockaddr_in address;
    int udp_port;
    int sock;
} Client;

Client clients[MAX_CLIENTS];
int client_count = 0;
pthread_mutex_t clients_mutex = PTHREAD_MUTEX_INITIALIZER;

void broadcast_message(char *message, struct sockaddr_in sender_addr, int sender_port) {
    int udp_socket = socket(AF_INET, SOCK_DGRAM, 0);
    if (udp_socket < 0) {
        perror("UDP socket creation failed");
        return;
    }

    for (int i = 0; i < client_count; ++i) {
        if (clients[i].udp_port != sender_port || clients[i].address.sin_addr.s_addr != sender_addr.sin_addr.s_addr) {
            struct sockaddr_in client_addr = clients[i].address;
            client_addr.sin_port = htons(clients[i].udp_port);
            sendto(udp_socket, message, strlen(message), 0, (struct sockaddr*)&client_addr, sizeof(client_addr));
        }
    }
    close(udp_socket);
}

void *handle_client(void *arg) {
    Client *client = (Client *)arg;
    char buffer[BUFFER_SIZE];
    int bytes_received;

    while ((bytes_received = recv(client->sock, buffer, BUFFER_SIZE, 0)) > 0) {
        buffer[bytes_received] = '\0';
        printf("Received from client %s:%d - %s\n", inet_ntoa(client->address.sin_addr), client->udp_port, buffer);

        if (strcmp(buffer, "by") == 0) {
            printf("Client %s:%d disconnected\n", inet_ntoa(client->address.sin_addr), client->udp_port);
            break;
        }

        // Broadcast message to all other clients
        broadcast_message(buffer, client->address, client->udp_port);
    }

    close(client->sock);

    pthread_mutex_lock(&clients_mutex);
    for (int i = 0; i < client_count; ++i) {
        if (clients[i].sock == client->sock) {
            clients[i] = clients[client_count - 1];
            client_count--;
            break;
        }
    }
    pthread_mutex_unlock(&clients_mutex);

    free(client);
    return NULL;
}

int main() {
    int tcp_socket;
    struct sockaddr_in server_addr;

    tcp_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (tcp_socket < 0) {
        perror("TCP socket creation failed");
        return EXIT_FAILURE;
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY;
    server_addr.sin_port = htons(TCP_PORT);

    if (bind(tcp_socket, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0) {
        perror("Bind failed");
        close(tcp_socket);
        return EXIT_FAILURE;
    }

    if (listen(tcp_socket, MAX_CLIENTS) < 0) {
        perror("Listen failed");
        close(tcp_socket);
        return EXIT_FAILURE;
    }

    printf("Server is listening on TCP port %d\n", TCP_PORT);

    while (1) {
        Client *client = malloc(sizeof(Client));
        socklen_t addr_len = sizeof(client->address);
        client->sock = accept(tcp_socket, (struct sockaddr*)&client->address, &addr_len);

        if (client->sock < 0) {
            perror("Accept failed");
            free(client);
            continue;
        }

        // Receive UDP port from the client
        recv(client->sock, &client->udp_port, sizeof(client->udp_port), 0);
        client->udp_port = ntohs(client->udp_port);

        pthread_mutex_lock(&clients_mutex);
        clients[client_count++] = *client;
        pthread_mutex_unlock(&clients_mutex);

        pthread_t tid;
        pthread_create(&tid, NULL, handle_client, client);
    }

    close(tcp_socket);
    return 0;
}
