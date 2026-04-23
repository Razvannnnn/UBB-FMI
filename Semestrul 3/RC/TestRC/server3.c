#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>

#define TCP_PORT 8080
#define BUFFER_SIZE 1024

// Structura pentru a stoca informatii despre clienti
typedef struct {
    int tcp_socket;
    struct sockaddr_in udp_address;
} client_info;

client_info clients[2]; // Doua clienti

// Functie pentru a procesa comunicatia intre clienti prin UDP
void *client_communication(void *arg) {
    client_info *client1 = (client_info *)arg;
    client_info *client2 = &clients[1]; // al doilea client

    // UDP socket pentru fiecare client
    int udp_socket1 = socket(AF_INET, SOCK_DGRAM, 0);
    int udp_socket2 = socket(AF_INET, SOCK_DGRAM, 0);

    if (udp_socket1 < 0 || udp_socket2 < 0) {
        perror("Eroare la crearea socketului UDP");
        return NULL;
    }

    // Clientul 1 trimite date clientului 2 si invers
    char buffer[BUFFER_SIZE];
    while (1) {
        // Citeste mesaj de la clientul 1
        ssize_t bytes_received = recvfrom(udp_socket1, buffer, BUFFER_SIZE, 0, (struct sockaddr *)&client1->udp_address, sizeof(client1->udp_address));
        if (bytes_received > 0) {
            buffer[bytes_received] = '\0'; // Termina stringul
            printf("Client 1: %s\n", buffer);
            // Trimite mesajul clientului 2
            sendto(udp_socket2, buffer, bytes_received, 0, (struct sockaddr *)&client2->udp_address, sizeof(client2->udp_address));
        }

        // Citeste mesaj de la clientul 2
        bytes_received = recvfrom(udp_socket2, buffer, BUFFER_SIZE, 0, (struct sockaddr *)&client2->udp_address, sizeof(client2->udp_address));
        if (bytes_received > 0) {
            buffer[bytes_received] = '\0';
            printf("Client 2: %s\n", buffer);
            // Trimite mesajul clientului 1
            sendto(udp_socket1, buffer, bytes_received, 0, (struct sockaddr *)&client1->udp_address, sizeof(client1->udp_address));
        }
    }

    // Inchide socketurile
    close(udp_socket1);
    close(udp_socket2);
    return NULL;
}

// Functie pentru a gestiona conexiunile TCP cu clientii
void *handle_client(void *arg) {
    int client_socket = *(int *)arg;
    char buffer[BUFFER_SIZE];
    int client_index = -1;

    // Primeste portul UDP de la client
    ssize_t bytes_received = recv(client_socket, buffer, BUFFER_SIZE, 0);
    if (bytes_received <= 0) {
        perror("Eroare la primirea portului UDP");
        close(client_socket);
        return NULL;
    }
    buffer[bytes_received] = '\0'; // Termina stringul
    int udp_port = atoi(buffer);

    // Seteaza adresa UDP
    struct sockaddr_in udp_address;
    memset(&udp_address, 0, sizeof(udp_address));
    udp_address.sin_family = AF_INET;
    udp_address.sin_addr.s_addr = INADDR_ANY;
    udp_address.sin_port = htons(udp_port);

    // Salveaza informatia clientului
    if (client_index == -1) {
        clients[0].tcp_socket = client_socket;
        clients[0].udp_address = udp_address;
        client_index = 0;
    } else {
        clients[1].tcp_socket = client_socket;
        clients[1].udp_address = udp_address;
    }

    // Daca am primit porturile de la amandoi clientii, pornim comunicatia
    if (clients[0].tcp_socket != -1 && clients[1].tcp_socket != -1) {
        pthread_t thread_id;
        pthread_create(&thread_id, NULL, client_communication, &clients[0]);
    }

    // Inchide socketul TCP
    close(client_socket);
    return NULL;
}

int main() {
    int server_socket, client_socket;
    struct sockaddr_in server_address, client_address;
    socklen_t client_address_len = sizeof(client_address);
    pthread_t thread_id;

    // Creaza socketul TCP
    server_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (server_socket < 0) {
        perror("Eroare la crearea socketului TCP");
        exit(1);
    }

    memset(&server_address, 0, sizeof(server_address));
    server_address.sin_family = AF_INET;
    server_address.sin_addr.s_addr = INADDR_ANY;
    server_address.sin_port = htons(TCP_PORT);

    // Leaga serverul la adresa si portul specificate
    if (bind(server_socket, (struct sockaddr *)&server_address, sizeof(server_address)) < 0) {
        perror("Eroare la bind");
        exit(1);
    }

    // Permite serverului sa asculte pentru conexiuni
    if (listen(server_socket, 2) < 0) {
        perror("Eroare la listen");
        exit(1);
    }

    printf("Serverul asteapta conexiuni la portul %d...\n", TCP_PORT);

    // Accepta doua conexiuni client
    for (int i = 0; i < 2; i++) {
        client_socket = accept(server_socket, (struct sockaddr *)&client_address, &client_address_len);
        if (client_socket < 0) {
            perror("Eroare la acceptarea conexiunii");
            continue;
        }
        printf("Clientul %d conectat\n", i + 1);
        pthread_create(&thread_id, NULL, handle_client, &client_socket);
    }

    // Asteapta sa se termine comunicarea
    pthread_exit(NULL);

    close(server_socket);
    return 0;
}
