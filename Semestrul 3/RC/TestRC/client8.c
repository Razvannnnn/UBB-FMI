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

    // Crearea unui socket TCP
    if ((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        perror("Socket creation failed");
        exit(1);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(SERVER_PORT);
    server_addr.sin_addr.s_addr = inet_addr(SERVER_IP);

    // Conectarea la server
    if (connect(sock, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("Connection to server failed");
        exit(1);
    }

    printf("Enter your UDP port: ");
    scanf("%d", &udp_port);

    // Trimiterea portului UDP către server
    if (send(sock, &udp_port, sizeof(udp_port), 0) < 0) {
        perror("Send failed");
        close(sock);
        exit(1);
    }

    // Așteptarea împerecherii
    if (recv(sock, &partner_udp_port, sizeof(partner_udp_port), 0) < 0) {
        perror("Recv failed");
        close(sock);
        exit(1);
    }

    // Primirea adresei IP a partenerului
    if (recv(sock, &partner_addr, sizeof(partner_addr), 0) < 0) {
        perror("Recv failed");
        close(sock);
        exit(1);
    }

    printf("Your partner's UDP port is %d\n", partner_udp_port);
    printf("Your partner's IP address is %s\n", inet_ntoa(partner_addr.sin_addr));

    // Crearea unui socket UDP pentru comunicarea cu partenerul
    int udp_sock;
    struct sockaddr_in udp_addr, partner_udp_addr;

    if ((udp_sock = socket(AF_INET, SOCK_DGRAM, 0)) < 0) {
        perror("UDP socket creation failed");
        exit(1);
    }

    udp_addr.sin_family = AF_INET;
    udp_addr.sin_port = htons(udp_port);
    udp_addr.sin_addr.s_addr = INADDR_ANY;

    if (bind(udp_sock, (struct sockaddr *)&udp_addr, sizeof(udp_addr)) < 0) {
        perror("Bind failed");
        exit(1);
    }

    // Loop pentru a trimite si compara numere pana cand sunt egale
    int num, partner_num;
    int numbers_are_equal = 0; // Flag pentru a determina cand numerele sunt egale

    while (!numbers_are_equal) {
        // Trimite un numar catre partener
        printf("Enter a number to send to your partner: ");
        scanf("%d", &num);

        partner_udp_addr.sin_family = AF_INET;
        partner_udp_addr.sin_port = htons(partner_udp_port);
        partner_udp_addr.sin_addr = partner_addr.sin_addr;

        if (sendto(udp_sock, &num, sizeof(num), 0, (struct sockaddr *)&partner_udp_addr, sizeof(partner_udp_addr)) < 0) {
            perror("Sendto failed");
            close(udp_sock);
            exit(1);
        }

        // Așteaptă răspuns de la partener
        addr_size = sizeof(partner_udp_addr);
        if (recvfrom(udp_sock, &partner_num, sizeof(partner_num), 0, (struct sockaddr *)&partner_udp_addr, &addr_size) < 0) {
            perror("Recvfrom failed");
            close(udp_sock);
            exit(1);
        }

        printf("Your partner sent: %d\n", partner_num);

        // Compară numerele
        if (num > partner_num) {
            printf("Your number is larger.\n");
        } else if (num < partner_num) {
            printf("Your partner's number is larger.\n");
        } else {
            printf("Both numbers are equal. Exiting...\n");
            numbers_are_equal = 1;  // Numerele sunt egale, terminăm loop-ul
        }
    }

    close(udp_sock);
    close(sock);
    return 0;
}
