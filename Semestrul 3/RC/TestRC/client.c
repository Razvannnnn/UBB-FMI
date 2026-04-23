#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>

#define SERVER_IP "127.0.0.1"
#define SERVER_PORT 12345
#define UDP_PORT 54321

void* udp_listener(void* arg) {
    int udp_sock = *(int*)arg;
    struct sockaddr_in peer_addr;
    socklen_t addr_len = sizeof(peer_addr);
    char buffer[1024];

    while (1) {
        int received = recvfrom(udp_sock, buffer, sizeof(buffer) - 1, 0, (struct sockaddr*)&peer_addr, &addr_len);
        if (received > 0) {
            buffer[received] = '\0';
            printf("Mesaj primit de la %s:%d - %s\n", inet_ntoa(peer_addr.sin_addr), ntohs(peer_addr.sin_port), buffer);
        }
    }
    return NULL;
}

int main() {
    int tcp_sock = socket(AF_INET, SOCK_STREAM, 0);
    struct sockaddr_in server_addr;

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(SERVER_PORT);
    inet_pton(AF_INET, SERVER_IP, &server_addr.sin_addr);

    connect(tcp_sock, (struct sockaddr*)&server_addr, sizeof(server_addr));

    char buffer[1024];
    snprintf(buffer, sizeof(buffer), "%d", UDP_PORT);
    send(tcp_sock, buffer, strlen(buffer), 0);

    recv(tcp_sock, buffer, sizeof(buffer), 0);
    char partner_ip[INET_ADDRSTRLEN];
    int partner_udp_port;
    sscanf(buffer, "%[^:]:%d", partner_ip, &partner_udp_port);

    printf("Conectat cu partenerul: %s:%d\n", partner_ip, partner_udp_port);

    int udp_sock = socket(AF_INET, SOCK_DGRAM, 0);
    struct sockaddr_in udp_addr, partner_addr;

    udp_addr.sin_family = AF_INET;
    udp_addr.sin_port = htons(UDP_PORT);
    udp_addr.sin_addr.s_addr = INADDR_ANY;

    bind(udp_sock, (struct sockaddr*)&udp_addr, sizeof(udp_addr));

    partner_addr.sin_family = AF_INET;
    partner_addr.sin_port = htons(partner_udp_port);
    inet_pton(AF_INET, partner_ip, &partner_addr.sin_addr);

    pthread_t listener_thread;
    pthread_create(&listener_thread, NULL, udp_listener, &udp_sock);

    while (1) {
        printf("Trimite mesaj: ");
        fgets(buffer, sizeof(buffer), stdin);
        sendto(udp_sock, buffer, strlen(buffer), 0, (struct sockaddr*)&partner_addr, sizeof(partner_addr));
    }

    close(tcp_sock);
    close(udp_sock);
    return 0;
}
