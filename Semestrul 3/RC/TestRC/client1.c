#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <sys/socket.h>

#define SERVER_IP "127.0.0.1"
#define SERVER_PORT 1234
#define LOCAL_UDP_PORT 9090

void error(const char *msg) {
    perror(msg);
    exit(EXIT_FAILURE);
}

int main() {
    int tcp_sock, udp_sock;
    struct sockaddr_in server_addr, partner_addr;
    uint16_t partner_udp_port;
    struct in_addr partner_ip;
    uint16_t udp_port = htons(LOCAL_UDP_PORT);

    // Configurare TCP pentru conectarea la server
    tcp_sock = socket(AF_INET, SOCK_STREAM, 0);
    if (tcp_sock < 0) error("TCP socket creation failed");

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(SERVER_PORT);
    inet_pton(AF_INET, SERVER_IP, &server_addr.sin_addr);

    if (connect(tcp_sock, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0)
        error("TCP connection failed");

    // Trimite portul UDP local la server
    send(tcp_sock, &udp_port, sizeof(udp_port), 0);

    // Primesc IP-ul și portul UDP al partenerului
    recv(tcp_sock, &partner_ip, sizeof(partner_ip), 0);
    recv(tcp_sock, &partner_udp_port, sizeof(partner_udp_port), 0);
    partner_udp_port = ntohs(partner_udp_port);

    printf("Partner IP: %s, Partner UDP Port: %d\n", inet_ntoa(partner_ip), partner_udp_port);

    // Configurare UDP pentru comunicare cu partenerul
    udp_sock = socket(AF_INET, SOCK_DGRAM, 0);
    if (udp_sock < 0) error("UDP socket creation failed");

    struct sockaddr_in local_udp_addr;
    local_udp_addr.sin_family = AF_INET;
    local_udp_addr.sin_addr.s_addr = INADDR_ANY;
    local_udp_addr.sin_port = htons(LOCAL_UDP_PORT);

    if (bind(udp_sock, (struct sockaddr*)&local_udp_addr, sizeof(local_udp_addr)) < 0)
        error("UDP bind failed");

    partner_addr.sin_family = AF_INET;
    partner_addr.sin_addr = partner_ip;
    partner_addr.sin_port = htons(partner_udp_port);

    int num, received_num;
    socklen_t addr_len = sizeof(partner_addr);

    // Trimite și primește numere prin UDP
    while (1) {
        printf("Enter a number: ");
        scanf("%d", &num);

        // Trimite numărul către partener
        int network_num = htonl(num);
        sendto(udp_sock, &network_num, sizeof(network_num), 0, (struct sockaddr*)&partner_addr, addr_len);

        // Primește numărul de la partener
        recvfrom(udp_sock, &received_num, sizeof(received_num), 0, (struct sockaddr*)&partner_addr, &addr_len);
        received_num = ntohl(received_num);

        printf("Received number from partner: %d\n", received_num);

        if (num != received_num) break; // Încheiem dialogul dacă numerele sunt diferite
    }

    close(tcp_sock);
    close(udp_sock);
    return 0;
}
