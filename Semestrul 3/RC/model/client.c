// client.c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>

#define SERVER_IP "127.0.0.1"
#define TCP_PORT 12345
#define UDP_PORT 54321  // Unique UDP port for each client
#define BUFFER_SIZE 1024

int udp_socket;

void *receive_udp_messages(void *arg) {
    char buffer[BUFFER_SIZE];
    struct sockaddr_in sender_addr;
    socklen_t addr_len = sizeof(sender_addr);

    while (1) {
        int bytes_received = recvfrom(udp_socket, buffer, BUFFER_SIZE, 0, (struct sockaddr*)&sender_addr, &addr_len);
        if (bytes_received > 0) {
            buffer[bytes_received] = '\0';
            printf("Received UDP: %s\n", buffer);
        }
    }
    return NULL;
}

int main() {
    int tcp_socket;
    struct sockaddr_in server_addr, udp_addr;

    // Set up TCP connection
    tcp_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (tcp_socket < 0) {
        perror("TCP socket creation failed");
        return EXIT_FAILURE;
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(TCP_PORT);
    inet_pton(AF_INET, SERVER_IP, &server_addr.sin_addr);

    if (connect(tcp_socket, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0) {
        perror("Connect failed");
        close(tcp_socket);
        return EXIT_FAILURE;
    }

    // Send UDP port to server
    int udp_port_net = htons(UDP_PORT);
    send(tcp_socket, &udp_port_net, sizeof(udp_port_net), 0);

    // Set up UDP socket for receiving messages
    udp_socket = socket(AF_INET, SOCK_DGRAM, 0);
    udp_addr.sin_family = AF_INET;
    udp_addr.sin_addr.s_addr = INADDR_ANY;
    udp_addr.sin_port = htons(UDP_PORT);
    bind(udp_socket, (struct sockaddr*)&udp_addr, sizeof(udp_addr));

    pthread_t udp_thread;
    pthread_create(&udp_thread, NULL, receive_udp_messages, NULL);

    // Message sending loop
    char buffer[BUFFER_SIZE];
    while (1) {
        printf("Enter message (or 'by' to quit): ");
        fgets(buffer, BUFFER_SIZE, stdin);
        buffer[strcspn(buffer, "\n")] = '\0';

        send(tcp_socket, buffer, strlen(buffer), 0);
        if (strcmp(buffer, "by") == 0) {
            break;
        }
    }

    close(tcp_socket);
    close(udp_socket);
    return 0;
}
