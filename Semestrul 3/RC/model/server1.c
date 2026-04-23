#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>
#include <arpa/inet.h>

#define TCP_PORT 12345
#define MAX_CLIENTS 10
#define BUFFER_SIZE 1024

typedef struct {
    int tcp_socket;
    struct sockaddr_in address;
    int udp_port;
} Client;

Client clients[MAX_CLIENTS];
int client_count = 0;
pthread_mutex_t clients_mutex = PTHREAD_MUTEX_INITIALIZER;

void broadcast_message(const char *message, int sender_socket) {
    int udp_socket = socket(AF_INET, SOCK_DGRAM, 0);
    if (udp_socket < 0) {
        perror("UDP socket creation failed");
        return;
    }

    for (int i = 0; i < client_count; i++) {
        if (clients[i].tcp_socket != sender_socket) { // Skip sender
            struct sockaddr_in client_addr = clients[i].address;
            client_addr.sin_port = htons(clients[i].udp_port);

            sendto(udp_socket, message, strlen(message), 0, 
                   (struct sockaddr *)&client_addr, sizeof(client_addr));
        }
    }

    close(udp_socket);
}

void *handle_client(void *arg) {
    Client *client = (Client *)arg;
    char buffer[BUFFER_SIZE];

    while (1) {
        int bytes_received = recv(client->tcp_socket, buffer, BUFFER_SIZE - 1, 0);
        if (bytes_received <= 0) {
            printf("Client disconnected\n");
            break;
        }

        buffer[bytes_received] = '\0';  // Null-terminate the received string
        printf("Received message from client: %s\n", buffer);

        // Broadcast the message to other clients
        broadcast_message(buffer, client->tcp_socket);
    }

    // Remove client from clients array
    close(client->tcp_socket);

    pthread_mutex_lock(&clients_mutex);
    for (int i = 0; i < client_count; i++) {
        if (clients[i].tcp_socket == client->tcp_socket) {
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
    int tcp_socket = socket(AF_INET, SOCK_STREAM, 0);
    struct sockaddr_in server_addr, client_addr;

    if (tcp_socket < 0) {
        perror("TCP socket creation failed");
        exit(1);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY;
    server_addr.sin_port = htons(TCP_PORT);

    if (bind(tcp_socket, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("TCP bind failed");
        close(tcp_socket);
        exit(1);
    }

    if (listen(tcp_socket, MAX_CLIENTS) < 0) {
        perror("TCP listen failed");
        close(tcp_socket);
        exit(1);
    }

    printf("Server listening on TCP port %d\n", TCP_PORT);

    while (1) {
        socklen_t client_len = sizeof(client_addr);
        int new_socket = accept(tcp_socket, (struct sockaddr *)&client_addr, &client_len);

        if (new_socket < 0) {
            perror("TCP accept failed");
            continue;
        }

        // Receive the UDP port number from the client
        char port_buffer[BUFFER_SIZE];
        int bytes_received = recv(new_socket, port_buffer, BUFFER_SIZE - 1, 0);
        if (bytes_received <= 0) {
            close(new_socket);
            continue;
        }
        port_buffer[bytes_received] = '\0';
        int udp_port = atoi(port_buffer);

        // Create a new client object
        Client *client = (Client *)malloc(sizeof(Client));
        client->tcp_socket = new_socket;
        client->address = client_addr;
        client->udp_port = udp_port;

        pthread_mutex_lock(&clients_mutex);
        if (client_count < MAX_CLIENTS) {
            clients[client_count++] = *client;
            pthread_t thread;
            pthread_create(&thread, NULL, handle_client, (void *)client);
            pthread_detach(thread);
        } else {
            printf("Max clients reached\n");
            close(new_socket);
            free(client);
        }
        pthread_mutex_unlock(&clients_mutex);
    }

    close(tcp_socket);
    return 0;
}
