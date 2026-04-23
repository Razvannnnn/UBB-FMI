#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <unistd.h>

int nr_users = 0;
struct utilizator{
        char mesaj[100][50];
        char addr_ip[20];
        uint16_t port;
        int n;
}users[20];


void citire_mesaj(int c, char mesaj[50]){
        int bytes_primit;

        bytes_primit = recv(c, mesaj, 49, 0);
        mesaj[bytes_primit] = '\0';
}

int main(){
        int s, c;
        struct sockaddr_in server, client, serverUDP;

        s = socket(AF_INET, SOCK_STREAM, 0);
        if (s < 0){
                printf("Eroare la crearea socketului client\n");
                return 1;
        }

        memset(&server, 0, sizeof(server));
        server.sin_port = htons(1235);
        server.sin_family = AF_INET;
        server.sin_addr.s_addr = INADDR_ANY;

        if (bind(s, (struct sockaddr*) &server, sizeof(server)) < 0){
                printf("Eraore la bind");
                return 1;
        }

        int pd[2];
        if (pipe(pd) == -1){
                printf("pipe");
                return 1;
        }
        if (fork() == 0){
        close(pd[0]);
        listen(s, 5);
        int l = sizeof(client);
        memset(&client, 0, sizeof(client));
        while (1){
                c = accept(s, (struct sockaddr*) &client, &l);

                if (fork() == 0){
                        char mesaj[100][50];
                        uint16_t k = 0;

                        char *addr_ip = inet_ntoa(client.sin_addr);
                        printf("%s\n", addr_ip);
                        uint16_t port;
                        recv(c, &port, sizeof(port), 0);
                        port = ntohs(port);


                        while (1){
                                citire_mesaj(c, mesaj[k]);

                                if (strcmp(mesaj[k++], "by") == 0){
                                        break;
                                }
                        }
                        close(c);

                        int lungime = strlen(addr_ip);
                        write(pd[1], &lungime, sizeof(int));
                        write(pd[1], addr_ip, strlen(addr_ip));
                        write(pd[1], &port, sizeof(port));

                        for (uint16_t i = 0; i < k; i ++){
                                lungime = strlen(mesaj[i]);
                                write(pd[1], &lungime, sizeof(int));
                                write(pd[1], mesaj[i], strlen(mesaj[i]));
                        }

                        exit(0);
                }
        }
        close(pd[1]);
        }
        else{
                close(pd[1]);
                while (1){
                        printf("merge\n");
                        int l;
                        users[nr_users].n = 0;
                        read(pd[0], &l, sizeof(int));
                        read(pd[0], users[nr_users].addr_ip, l);
                        read(pd[0], &users[nr_users].port, sizeof(uint16_t));
                        printf("%s %hu\n", users[nr_users].addr_ip, users[nr_users].port);
                        while (1){
                                read(pd[0], &l, sizeof(int));
                                read(pd[0], users[nr_users].mesaj[users[nr_users].n++], l);
                                printf("%s\n", users[nr_users].mesaj[users[nr_users].n - 1]);
                                if (strcmp(users[nr_users].mesaj[users[nr_users].n - 1], "by") == 0){
                                        break;
                                }
                        }
                        nr_users++;


                        printf("Nr users=%d\n", nr_users);
                        if (nr_users > 1){
                                for (int i = 0; i < nr_users; i ++){
                                        int cUDP;
                                        cUDP = socket(AF_INET, SOCK_DGRAM, 0);

                                        memset(&serverUDP, 0, sizeof(serverUDP));
                                        serverUDP.sin_port = htons(users[i].port);
                                        serverUDP.sin_family = AF_INET;
                                        serverUDP.sin_addr.s_addr = inet_addr(users[i].addr_ip);

                                        for (int j = 0; j < nr_users; j++){
                                                if (i != j){
                                                int l_m;
                                                for (int nr = 0; nr < users[j].n; nr++){
                                                        l_m = strlen(users[j].mesaj[nr]);
                                                        sendto(cUDP, &l_m, sizeof(int), 0, (struct sockaddr *) &serverUDP, sizeof(serverUDP));
                                                        sendto(cUDP, users[j].mesaj[nr], strlen(users[j].mesaj[nr]), 0, (struct sockaddr *) &serverUDP, sizeof(serverUDP));
                                                }
                                                l_m = strlen(users[j].addr_ip);
                                                sendto(cUDP, &l_m, sizeof(int), 0, (struct sockaddr *) &serverUDP, sizeof(serverUDP));
                                                sendto(cUDP, users[j].addr_ip, strlen(users[j].addr_ip), 0, (struct sockaddr *) &serverUDP, sizeof(serverUDP));

                                                }
                                        }

                                        //sendto(c, &k, sizeof(k), 0, (struct sockaddr *) &server, sizeof(server));
                                }
                                nr_users = 0;
                        }
                }
        }

        return 0;
}
