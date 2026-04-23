%{
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int line = 1;
int col = 1;

void print_token(const char* type, const char* text) {
    printf("%s|%s|%d|%d\n", type, text, line, col);
}
%}

IDENT   [A-Za-z_][A-Za-z0-9_]*
INT     [0-9]+
REAL    [0-9]+"."[0-9]+
STRING  \"([^\"\n]*)\"

%%

[ \t\r]+            { col += yyleng; }

[\n]                { line++; col = 1; }

"//".*              { col += yyleng; }
"#".*               { col += yyleng; }

\"([^\"\n]*)\n      {
                       fprintf(stderr, 
                          "Eroare: sir neinchis la linia %d, coloana %d\n",
                          line, col);
                       exit(1);
                   }

{STRING}            { print_token("STRING", yytext); col += yyleng; }

int                 { print_token("KEYWORD", "int"); col += yyleng; }
float               { print_token("KEYWORD", "float"); col += yyleng; }
struct              { print_token("KEYWORD", "struct"); col += yyleng; }
if                  { print_token("KEYWORD", "if"); col += yyleng; }
else                { print_token("KEYWORD", "else"); col += yyleng; }
while               { print_token("KEYWORD", "while"); col += yyleng; }
cin                 { print_token("KEYWORD", "cin"); col += yyleng; }
cout                { print_token("KEYWORD", "cout"); col += yyleng; }
using               { print_token("KEYWORD", "using"); col += yyleng; }
namespace           { print_token("KEYWORD", "namespace"); col += yyleng; }
main                { print_token("KEYWORD", "main"); col += yyleng; }

daca                { print_token("KEYWORD", "daca"); col += yyleng; }
atunci              { print_token("KEYWORD", "atunci"); col += yyleng; }
sfdaca              { print_token("KEYWORD", "sfdaca"); col += yyleng; }

{REAL}              { print_token("REAL", yytext); col += yyleng; }
{INT}               { print_token("INT", yytext); col += yyleng; }

{IDENT} {
            if (yyleng > 10) {
                fprintf(stderr,
                    "Eroare: identificator prea lung '%s' la linia %d, coloana %d\n",
                    yytext, line, col);
                exit(1);
            }
            print_token("IDENT", yytext);
            col += yyleng;
        }

"=="|"!="|"<="|">="|"&&"|"||"|"<<"|">>"|"::" {
                    print_token("DELIM", yytext); col += yyleng;
}

";"|","|"{"|"}"|"("|")"|"+"|"-"|"*"|"/"|"%"|"="|"<"|">"|"." {
                    print_token("DELIM", yytext); col += yyleng;
}

. {
      fprintf(stderr,
         "Eroare lexicala: caracter necunoscut '%s' la linia %d, coloana %d\n",
         yytext, line, col);
      exit(1);
  }

%%

int main() {
    yylex();
    return 0;
}

int yywrap() { return 1; }
