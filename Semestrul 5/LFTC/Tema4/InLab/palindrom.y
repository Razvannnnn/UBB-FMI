%{
#include <stdio.h>
#include <stdlib.h>

void yyerror(const char *s);
int yylex();

void check_palindrom_time(int h1, int h2, int m1, int m2);
%}

%union {
    int ival;
}

%token <ival> DIGIT
%token COLON EOL

%%

program:
    | program line
    ;

line:
    time_entry EOL
    | EOL
    | error EOL { yyerrok; }
    ;

time_entry:
    DIGIT DIGIT COLON DIGIT DIGIT 
    {
        check_palindrom_time($1, $2, $4, $5);
    }
    ;

%%

void check_palindrom_time(int h1, int h2, int m1, int m2) {
    int hour = h1 * 10 + h2;
    int minute = m1 * 10 + m2;

    if (hour > 23 || minute > 59) {
        printf("ora invalida\n");
        return;
    }

    if (h1 == m2 && h2 == m1) {
        printf("ESTE PALINDROM\n");
    } else {
        printf("NU ESTE PALINDROM\n");
    }
}

void yyerror(const char *s) {
    fprintf(stderr, "Eroare formatare: %s\n", s);
}

int main() {
    printf("Format cc:cc :\n");
    yyparse();
    return 0;
}
