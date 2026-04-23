#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <arpa/inet.h>
#include <unistd.h>

int main() {
    int sock;
    struct sockaddr_in server;
    char hostname[100];
    uint32_t ip_address;

    sock = socket(AF_INET, SOCK_DGRAM, 0);
    if (sock < 0) {
        printf("Eroare la crearea socket-ului client");
        return 1;
    }

    memset(&server, 0, sizeof(server));
    server.sin_family = AF_INET;
    server.sin_port = htons(1234);
    server.sin_addr.s_addr = inet_addr("127.0.0.1");

    printf("Numele calculatorului: ");
    scanf("%99s", hostname);

    sendto(sock, hostname, strlen(hostname) + 1, 0, (struct sockaddr*)&server, sizeof(server));

    recvfrom(sock, &ip_address, sizeof(ip_address), 0, NULL, NULL);
    ip_address = ntohl(ip_address);

    if (ip_address == 0) {
        printf("Numele calculatorului nu se poate transforma in IP\n");
    } else {
        printf("Adresa IP pentru '%s' este: %u.%u.%u.%u\n",
            hostname,
            ip_address & 0xFF,
	    (ip_address >> 8) & 0xFF,
	    (ip_address >> 16) & 0xFF,
            (ip_address >> 24) & 0xFF);
    }

    close(sock);
    return 0;
}
