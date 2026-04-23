#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>

int main() {
  int c;
  struct sockaddr_in server;
  char host[101];
  uint32_t ip;
  socklen_t server_len = sizeof(server);

  c = socket(AF_INET, SOCK_DGRAM, 0);
  if (c < 0) {
    printf("Eroare la crearea socketului client\n");
    return 1;
  }

  memset(&server, 0, sizeof(server));
  server.sin_port = htons(1234);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr("127.0.0.1");

  printf("Introduceti numele calculatorului: ");
  fgets(host, 101, stdin);
  host[strcspn(host, "\n")] = 0;

  if(sendto(c, host, strlen(host)+1, 0, (struct sockaddr *) &server, sizeof(server)) < 0) {
    printf("Eroare la trimiterea numelui de calculator\n");
    return 1;
  }

  if(recvfrom(c, &ip, sizeof(ip), MSG_WAITALL, (struct sockaddr *) &server, &server_len) < 0) {
    printf("Eroare la primirea adresei ip\n");
    return 1;
  }

  if(ip == 0) printf("Numele calculatorului nu poate fi transformat in adresa ip\n");
  else {
    struct in_addr adresa_ip;
    adresa_ip.s_addr = ip;
    printf("Adresa IP: %s\n", inet_ntoa(adresa_ip));
  }

  close(c);
}
