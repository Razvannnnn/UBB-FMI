#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define SERVER_IP "127.0.0.1"
#define SERVER_PORT 8080

int main() {
    int sock;
    struct sockaddr_in server_addr, partner_addr;
    int udp_port;
    int partner_udp_port;
    socklen_t addr_size;
    char buffer[1024];

    if ((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        perror("Eroare la crearea socketului");
        exit(1);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(SERVER_PORT);
    server_addr.sin_addr.s_addr = inet_addr(SERVER_IP);

    if (connect(sock, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("Eroare la conectarea la server");
        exit(1);
    }

    printf("Introdu portul tau UDP: ");
    scanf("%d", &udp_port);

    if (send(sock, &udp_port, sizeof(udp_port), 0) < 0) {
        perror("Eroare la trimiterea datelor");
        close(sock);
        exit(1);
    }

    if (recv(sock, &partner_udp_port, sizeof(partner_udp_port), 0) < 0) {
        perror("Eroare la primirea datelor");
        close(sock);
        exit(1);
    }

    if (recv(sock, &partner_addr, sizeof(partner_addr), 0) < 0) {
        perror("Eroare la primirea adresei partenerului");
        close(sock);
        exit(1);
    }

    printf("Portul UDP al partenerului este %d\n", partner_udp_port);
    printf("Adresa IP a partenerului este %s\n", inet_ntoa(partner_addr.sin_addr));

    int udp_sock;
    struct sockaddr_in udp_addr, partner_udp_addr;

    if ((udp_sock = socket(AF_INET, SOCK_DGRAM, 0)) < 0) {
        perror("Eroare la crearea socketului UDP");
        exit(1);
    }

    udp_addr.sin_family = AF_INET;
    udp_addr.sin_port = htons(udp_port);
    udp_addr.sin_addr.s_addr = INADDR_ANY;

    if (bind(udp_sock, (struct sockaddr *)&udp_addr, sizeof(udp_addr)) < 0) {
        perror("Eroare la bind pe socketul UDP");
        exit(1);
    }

    int num, partner_num;
    int numbers_are_equal = 0;

    while (!numbers_are_equal) {
        printf("Introdu un numar pentru a-l trimite partenerului: ");
        scanf("%d", &num);

        partner_udp_addr.sin_family = AF_INET;
        partner_udp_addr.sin_port = htons(partner_udp_port);
        partner_udp_addr.sin_addr = partner_addr.sin_addr;

        if (sendto(udp_sock, &num, sizeof(num), 0, (struct sockaddr *)&partner_udp_addr, sizeof(partner_udp_addr)) < 0) {
            perror("Eroare la trimiterea numarului");
            close(udp_sock);
            exit(1);
        }

        addr_size = sizeof(partner_udp_addr);
        if (recvfrom(udp_sock, &partner_num, sizeof(partner_num), 0, (struct sockaddr *)&partner_udp_addr, &addr_size) < 0) {
            perror("Eroare la primirea numarului de la partener");
            close(udp_sock);
            exit(1);
        }

        printf("Partenerul a trimis: %d\n", partner_num);

        if (num > partner_num) {
            printf("Numarul tau este mai mare.\n");
        } else if (num < partner_num) {
            printf("Numarul partenerului este mai mare.\n");
        } else {
            printf("Numerele sunt egale. Oprire...\n");
            numbers_are_equal = 1;
        }
    }

    close(udp_sock);
    close(sock);
    return 0;
}
