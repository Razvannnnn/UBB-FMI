#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define SERVER_IP "127.0.0.1"
#define SERVER_PORT 8080

int main() {
    int sock;
    struct sockaddr_in server_addr;
    int udp_port;
    char buffer[1024];
    struct sockaddr_in partner_addr;

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
    int partner_udp_port;
    if (recv(sock, &partner_udp_port, sizeof(partner_udp_port), 0) < 0) {
        perror("Recv failed");
        close(sock);
        exit(1);
    }
    if (recv(sock, &partner_addr, sizeof(partner_addr), 0) < 0) {
        perror("Recv failed");
        close(sock);
        exit(1);
    }

    printf("Your partner's UDP port is %d\n", partner_udp_port);
    printf("Your partner's IP address is %s\n", inet_ntoa(partner_addr.sin_addr));

    close(sock);
    return 0;
}
