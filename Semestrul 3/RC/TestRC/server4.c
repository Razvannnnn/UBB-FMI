#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>

#define PORT 8080
#define MAX_CLIENTS 10

typedef struct {
    int socket;
    struct sockaddr_in address;
    uint16_t udp_port;
} ClientInfo;

ClientInfo client_queue[MAX_CLIENTS];
int queue_size = 0;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

void *handle_client(void *arg);
void send_partner_info(int client_socket, struct sockaddr_in partner_addr, uint16_t partner_udp_port);

int main() {
    int server_socket;
    struct sockaddr_in server_addr, client_addr;
    socklen_t client_len = sizeof(client_addr);

    server_socket = socket(AF_INET, SOCK_STREAM, 0);
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY;
    server_addr.sin_port = htons(PORT);

    bind(server_socket, (struct sockaddr *)&server_addr, sizeof(server_addr));
    listen(server_socket, MAX_CLIENTS);

    printf("Server is running on port %d\n", PORT);

    while (1) {
        int client_socket = accept(server_socket, (struct sockaddr *)&client_addr, &client_len);
        if (client_socket < 0) {
            perror("Connection error");
            continue;
        }

        pthread_t tid;
        ClientInfo *client_info = malloc(sizeof(ClientInfo));
        client_info->socket = client_socket;
        client_info->address = client_addr;
        
        pthread_create(&tid, NULL, handle_client, (void *)client_info);
    }
    close(server_socket);
    return 0;
}

void *handle_client(void *arg) {
    ClientInfo *client_info = (ClientInfo *)arg;
    uint16_t udp_port;

    read(client_info->socket, &udp_port, sizeof(udp_port));
    udp_port = ntohs(udp_port);
    client_info->udp_port = udp_port;

    pthread_mutex_lock(&mutex);
    if (queue_size == 0) {
        client_queue[queue_size++] = *client_info;
    } else {
        ClientInfo partner = client_queue[--queue_size];
        
        send_partner_info(client_info->socket, partner.address, partner.udp_port);
        send_partner_info(partner.socket, client_info->address, client_info->udp_port);

        close(client_info->socket);
        close(partner.socket);
    }
    pthread_mutex_unlock(&mutex);

    free(client_info);
    return NULL;
}

void send_partner_info(int client_socket, struct sockaddr_in partner_addr, uint16_t partner_udp_port) {
    char ip_str[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &partner_addr.sin_addr, ip_str, sizeof(ip_str));

    write(client_socket, ip_str, strlen(ip_str) + 1);
    partner_udp_port = htons(partner_udp_port);
    write(client_socket, &partner_udp_port, sizeof(partner_udp_port));
}
