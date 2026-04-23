#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define SERVER_IP "127.0.0.1"
#define TCP_PORT 8080
#define UDP_BUFFER_SIZE 1024

// Functie pentru a trimite numere prin UDP
void send_udp_message(int udp_socket, struct sockaddr_in *udp_address) {
    char buffer[UDP_BUFFER_SIZE];
    int num = 1;
    while (1) {
        snprintf(buffer, UDP_BUFFER_SIZE, "%d", num++);
        sendto(udp_socket, buffer, strlen(buffer), 0, (struct sockaddr *)udp_address, sizeof(*udp_address));
        printf("Trimis mesaj: %s\n", buffer);
        sleep(1); // Asteapta un secunda
    }
}

int main() {
    int tcp_socket, udp_socket;
    struct sockaddr_in server_address, udp_address;
    char udp_port_str[10];
    socklen_t udp_address_len = sizeof(udp_address);

    // Creaza socketul TCP
    tcp_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (tcp_socket < 0) {
        perror("Eroare la crearea socketului TCP");
        exit(1);
    }

    // Conecteaza clientul la server
    memset(&server_address, 0, sizeof(server_address));
    server_address.sin_family = AF_INET;
    server_address.sin_port = htons(TCP_PORT);
    server_address.sin_addr.s_addr = inet_addr(SERVER_IP);

    if (connect(tcp_socket, (struct sockaddr *)&server_address, sizeof(server_address)) < 0) {
        perror("Eroare la conectarea la server");
        exit(1);
    }

    // Cere portul UDP de la utilizator
    printf("Introdu portul UDP: ");
    fgets(udp_port_str, sizeof(udp_port_str), stdin);
    udp_port_str[strcspn(udp_port_str, "\n")] = 0; // Eliminam caracterul newline

    // Trimite portul UDP la server
    send(tcp_socket, udp_port_str, strlen(udp_port_str), 0);

    // Creaza socketul UDP
    udp_socket = socket(AF_INET, SOCK_DGRAM, 0);
    if (udp_socket < 0) {
        perror("Eroare la crearea socketului UDP");
        exit(1);
    }

    // Seteaza adresa UDP
    memset(&udp_address, 0, sizeof(udp_address));
    udp_address.sin_family = AF_INET;
    udp_address.sin_addr.s_addr = INADDR_ANY;
    udp_address.sin_port = htons(atoi(udp_port_str));

    // Leaga socketul UDP
    if (bind(udp_socket, (struct sockaddr *)&udp_address, sizeof(udp_address)) < 0) {
        perror("Eroare la bind UDP");
        exit(1);
    }

    // Trimite mesaje prin UDP
    send_udp_message(udp_socket, &udp_address);

    // Inchide socketurile
    close(udp_socket);
    close(tcp_socket);
    return 0;
}
