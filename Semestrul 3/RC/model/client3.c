// client.c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>

#define PORT 8080
#define BUFFER_SIZE 1024

void *receive_messages(void *socket_desc) {
    int sock = *(int *)socket_desc;
    char buffer[BUFFER_SIZE];
    int bytes_read;

    while ((bytes_read = recv(sock, buffer, sizeof(buffer), 0)) > 0) {
        buffer[bytes_read] = '\0';
        printf("Message from another client: %s", buffer);
    }
    
    return NULL;
}

int main() {
    int sock;
    struct sockaddr_in server_addr;
    pthread_t recv_thread;

    sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        perror("Socket creation failed");
        exit(EXIT_FAILURE);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = inet_addr("127.0.0.1"); // Serverul rulează pe localhost

    if (connect(sock, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("Connection failed");
        close(sock);
        exit(EXIT_FAILURE);
    }

    printf("Connected to server. You can start typing messages.\n");

    // Creează un fir pentru a primi mesaje de la server
    pthread_create(&recv_thread, NULL, receive_messages, &sock);

    char message[BUFFER_SIZE];
    while (1) {
        fgets(message, BUFFER_SIZE, stdin); // Citește mesajul de la tastatură
        send(sock, message, strlen(message), 0); // Trimite mesajul la server
    }

    close(sock);
    return 0;
}
