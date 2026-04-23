#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <arpa/inet.h>
#include <stdlib.h>

int main(int argc, char** argv){
        int c;
        struct sockaddr_in server;

        c = socket(AF_INET, SOCK_STREAM, 0);
        if (c < 0){
                printf("Eroare la crearea socketului client\n");
                return 1;
        }

        memset(&server, 0, sizeof(server));
        server.sin_port = htons(atoi(argv[2]));
        server.sin_family = AF_INET;
        server.sin_addr.s_addr = inet_addr(argv[1]);

        if (connect(c, (struct sockaddr*) &server, sizeof(server)) < 0){
                printf("Eroare la connect\n");
                return 1;
        }


        uint16_t port;
        printf("\nPort=");
        scanf("%hu", &port);

        port = htons(port);
        send(c, &port, sizeof(port), 0);

        char sir[50];
        while (strcmp(sir, "by") != 0){
                fgets(sir, sizeof(sir), stdin);
                sir[strcspn(sir, "\n")] = 0;
                send(c, sir, strlen(sir), 0);
        }

        int sUDP;
        struct sockaddr_in serverUDP, clientUDP;

        sUDP = socket(AF_INET, SOCK_DGRAM, 0);

        memset(&serverUDP, 0, sizeof(serverUDP));
        serverUDP.sin_port = port;
        serverUDP.sin_family = AF_INET;
        serverUDP.sin_addr.s_addr = INADDR_ANY;



        if (bind(sUDP, (struct sockaddr *) &serverUDP, sizeof(serverUDP)) < 0) {
                printf("Eroare la bind\n");
                return 1;
        }

        int l = sizeof(clientUDP);
        memset(&clientUDP, 0, sizeof(clientUDP));

        while (1){
                char mesaj[50];
                int l_m;
                recvfrom(sUDP, &l_m, sizeof(int), MSG_WAITALL, (struct sockaddr *) &clientUDP, &l);
                recvfrom(sUDP, mesaj, l_m, MSG_WAITALL, (struct sockaddr *) &clientUDP, &l);
                mesaj[l_m] = '\0';
                printf("%s\n", mesaj);
        }
}
