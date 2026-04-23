#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define SERVER_IP "127.0.0.1"
#define SERVER_PORT 8080
#define UDP_PORT 9090

void hton_custom(uint16_t *value);
void ntoh_custom(uint16_t *value);

int main() {
    int tcp_socket, udp_socket;
    struct sockaddr_in server_addr, partner_addr;
    char partner_ip[INET_ADDRSTRLEN];
    uint16_t partner_udp_port;

    tcp_socket = socket(AF_INET, SOCK_STREAM, 0);
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(SERVER_PORT);
    inet_pton(AF_INET, SERVER_IP, &server_addr.sin_addr);

    connect(tcp_socket, (struct sockaddr *)&server_addr, sizeof(server_addr));

    uint16_t udp_port = UDP_PORT;
    hton_custom(&udp_port);
    write(tcp_socket, &udp_port, sizeof(udp_port));

    read(tcp_socket, partner_ip, INET_ADDRSTRLEN);
    read(tcp_socket, &partner_udp_port, sizeof(partner_udp_port));
    ntoh_custom(&partner_udp_port);

    printf("Partner IP: %s, Partner UDP Port: %d\n", partner_ip, partner_udp_port);

    udp_socket = socket(AF_INET, SOCK_DGRAM, 0);
    partner_addr.sin_family = AF_INET;
    partner_addr.sin_port = htons(partner_udp_port);
    inet_pton(AF_INET, partner_ip, &partner_addr.sin_addr);

    int num, partner_num;
    struct sockaddr_in local_addr;
    socklen_t addr_len = sizeof(local_addr);

    bind(udp_socket, (struct sockaddr *)&local_addr, sizeof(local_addr));

    while (1) {
        printf("Enter a number: ");
        scanf("%d", &num);

        hton_custom((uint16_t *)&num);
        sendto(udp_socket, &num, sizeof(num), 0, (struct sockaddr *)&partner_addr, sizeof(partner_addr));
        
        recvfrom(udp_socket, &partner_num, sizeof(partner_num), 0, (struct sockaddr *)&partner_addr, &addr_len);
        ntoh_custom((uint16_t *)&partner_num);

        printf("Partner's number: %d\n", partner_num);

        if (num == partner_num) {
            printf("Numbers match! Exiting...\n");
            break;
        }
    }
    close(udp_socket);
    close(tcp_socket);
    return 0;
}

void hton_custom(uint16_t *value) {
    *value = ((*value & 0xFF00) >> 8) | ((*value & 0x00FF) << 8);
}

void ntoh_custom(uint16_t *value) {
    hton_custom(value);  // hton and ntoh are symmetrical in this simple case
}
