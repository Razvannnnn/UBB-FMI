#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>
#include <arpa/inet.h>

#define SERVER_IP "127.0.0.1"
#define TCP_PORT 12345
#define UDP_PORT 54321  // Each client should have a unique UDP port
#define BUFFER_SIZE 1024

int udp_socket;

void *receive_udp_messages(void *arg) {
    struct sockaddr_in sender_addr;
    socklen_t addr_len = sizeof(sender_addr);
    char buffer[BUFFER_SIZE];

    while (1) {
        int bytes_received = recvfrom(udp_socket, buffer, BUFFER_SIZE - 1, 0,
                                      (struct sockaddr *)&sender_addr, &addr_len);
        if (bytes_received > 0) {
            buffer[bytes_received] = '\0';
            printf("Received UDP: %s\n", buffer);
        }
    }
}

int main() {
    int tcp_socket = socket(AF_INET, SOCK_STREAM, 0);
    struct sockaddr_in server_addr;

    if (tcp_socket < 0) {
        perror("TCP socket creation failed");
        exit(1);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = inet_addr(SERVER_IP);
    server_addr.sin_port = htons(TCP_PORT);

    if (connect(tcp_socket, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("TCP connection failed");
        close(tcp_socket);
        exit(1);
    }

    // Send UDP port to server
    char port_buffer[BUFFER_SIZE];
    snprintf(port_buffer, BUFFER_SIZE, "%d", UDP_PORT);
    send(tcp_socket, port_buffer, strlen(port_buffer), 0);

    // Create UDP socket for receiving broadcasts
    udp_socket = socket(AF_INET, SOCK_DGRAM, 0);
    struct sockaddr_in udp_addr;
    udp_addr.sin_family = AF_INET;
    udp_addr.sin_addr.s_addr = INADDR_ANY;
    udp_addr.sin_port = htons(UDP_PORT);

    if (bind(udp_socket, (struct sockaddr *)&udp_addr, sizeof(udp_addr)) < 0) {
        perror("UDP bind failed");
        close(tcp_socket);
        exit(1);
    }

    // Start thread to receive UDP messages
    pthread_t udp_thread;
    pthread_create(&udp_thread, NULL, receive_udp_messages, NULL);

    // Main loop to send messages to the server
    char buffer[BUFFER_SIZE];
    while (1) {
        printf("Enter message: ");
        fgets(buffer, BUFFER_SIZE, stdin);
        buffer[strcspn(buffer, "\n")] = '\0'; // Remove newline character

        send(tcp_socket, buffer, strlen(buffer), 0);

        if (strcmp(buffer, "by") == 0) {
            break;
        }
    }

    close(tcp_socket);
    close(udp_socket);
    return 0;
}
