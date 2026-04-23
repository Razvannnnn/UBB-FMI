#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <arpa/inet.h>
#include <netdb.h>

int main() {
  int s;
  struct sockaddr_in server, client;
  char hostname[101];
  struct hostent *host_entry;
  uint32_t ip_address;
  socklen_t client_len = sizeof(client);

  s = socket(AF_INET, SOCK_DGRAM, 0);
  if (s < 0) {
    perror("Eroare la crearea socketului server");
    return 1;
  }

  memset(&server, 0, sizeof(server));
  server.sin_port = htons(1234);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = INADDR_ANY;

  if (bind(s, (struct sockaddr *) &server, sizeof(server)) < 0) {
    perror("Eroare la bind");
    close(s);
    return 1;
  }

  while (1) {
    memset(hostname, 0, sizeof(hostname));

    if (recvfrom(s, hostname, sizeof(hostname), MSG_WAITALL, (struct sockaddr *) &client, &client_len) < 0) {
      perror("Eroare la primirea numelui de calculator");
      continue;
    }

    host_entry = gethostbyname(hostname);
    if (host_entry == NULL) {
      ip_address = 0;
    } else {
      struct in_addr **addr_list = (struct in_addr **) host_entry->h_addr_list;
      ip_address = addr_list[0]->s_addr;
    }

    if (sendto(s, &ip_address, sizeof(ip_address), 0, (struct sockaddr *) &client, client_len) < 0) {
      perror("Eroare la trimiterea adresei IP");
    }
  }

  close(s);
  return 0;
}
