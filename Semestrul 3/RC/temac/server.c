#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netdb.h>

int main() {
    int sock;
    struct sockaddr_in server, client;
    socklen_t client_len = sizeof(client);
    char hostname[100];
    uint32_t ip_address;

    sock = socket(AF_INET, SOCK_DGRAM, 0);
    if (sock < 0) {
        printf("Eroare la crearea socket-ului server");
        return 1;
    }

    memset(&server, 0, sizeof(server));
    server.sin_family = AF_INET;
    server.sin_port = htons(1234);
    server.sin_addr.s_addr = INADDR_ANY;

    if (bind(sock, (struct sockaddr*)&server, sizeof(server)) < 0) {
        printf("Eroare la bind");
        return 1;
    }

    while (1) {
        recvfrom(sock, hostname, sizeof(hostname), 0, (struct sockaddr*)&client, &client_len);

        struct hostent *he = gethostbyname(hostname);
        if (he == NULL) {
            ip_address = 0;
        } else {
            ip_address = *((uint32_t*)he->h_addr);
        }

        ip_address = htonl(ip_address);
        sendto(sock, &ip_address, sizeof(ip_address), 0, (struct sockaddr*)&client, client_len);
    }

    close(sock);
    return 0;
}
