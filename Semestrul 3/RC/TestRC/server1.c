#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <sys/socket.h>

#define SERVER_PORT 256
#define SERVER_IP "127.0.0.1"

void error(const char *msg) {
    perror(msg);
    exit(EXIT_FAILURE);
}

int main() {
    int server_sock, client_sock1, client_sock2;
    socklen_t client_len;
    struct sockaddr_in server_addr, client_addr1, client_addr2;
    uint16_t udp_port1, udp_port2;
    struct in_addr client_ip1, client_ip2;
    int waiting_client = 0;  // Variabilă pentru a semnala dacă suntem în așteptare

    // Crearea socket-ului pentru server
    server_sock = socket(AF_INET, SOCK_STREAM, 0);
    if (server_sock < 0) error("Failed to create server socket");

    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY;
    server_addr.sin_port = htons(SERVER_PORT);

    // Legarea serverului la portul 256
    if (bind(server_sock, (struct sockaddr *) &server_addr, sizeof(server_addr)) < 0)
        error("Failed to bind server socket");

    // Serverul începe să asculte pe portul 256
    listen(server_sock, 5);
    printf("Server listening on port %d...\n", SERVER_PORT);

    client_len = sizeof(client_addr1);

    while (1) {
        // Acceptă primul client
        client_sock1 = accept(server_sock, (struct sockaddr *) &client_addr1, &client_len);
        if (client_sock1 < 0) error("Failed to accept first client connection");

        printf("First client connected from %s:%d\n",
               inet_ntoa(client_addr1.sin_addr), ntohs(client_addr1.sin_port));

        // Primește portul UDP de la primul client
        recv(client_sock1, &udp_port1, sizeof(udp_port1), 0);
        udp_port1 = ntohs(udp_port1);
        client_ip1 = client_addr1.sin_addr;

        // Setează waiting_client la 1 (înseamnă că serverul așteaptă al doilea client)
        waiting_client = 1;

        // Acceptă al doilea client
        client_sock2 = accept(server_sock, (struct sockaddr *) &client_addr2, &client_len);
        if (client_sock2 < 0) error("Failed to accept second client connection");

        printf("Second client connected from %s:%d\n",
               inet_ntoa(client_addr2.sin_addr), ntohs(client_addr2.sin_port));

        // Primește portul UDP de la al doilea client
        recv(client_sock2, &udp_port2, sizeof(udp_port2), 0);
        udp_port2 = ntohs(udp_port2);
        client_ip2 = client_addr2.sin_addr;

        // Trimit adresele IP și porturile UDP ale celor doi clienți către fiecare
        send(client_sock1, &client_ip2, sizeof(client_ip2), 0);
        send(client_sock1, &udp_port2, sizeof(udp_port2), 0);
        send(client_sock2, &client_ip1, sizeof(client_ip1), 0);
        send(client_sock2, &udp_port1, sizeof(udp_port1), 0);

        printf("Clients have been paired. Server is now ready for communication.\n");

        // După ce a făcut imperecherea, setează waiting_client la 0
        waiting_client = 0;

        // Închide socket-urile clientului
        close(client_sock1);
        close(client_sock2);
    }

    close(server_sock);
    return 0;
}
