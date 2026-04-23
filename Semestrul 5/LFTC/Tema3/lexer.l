%{
#include <stdio.h>
#include <string.h>

int line = 1;
void yyerror(const char *msg) {
    fprintf(stderr, "Eroare lexicala la linia %d: %s\n", line, msg);
}
%}

%%

[ \t\r]+                ;
[\n]                    { line++; }

"//".*                  ;
"#".*                   ;

\"[^\"\n]*\"            { printf("STRING: %s (linia %d)\n", yytext, line); }

int                     { printf("KEYWORD: int\n"); }
float                   { printf("KEYWORD: float\n"); }
struct                  { printf("KEYWORD: struct\n"); }
if                      { printf("KEYWORD: if\n"); }
else                    { printf("KEYWORD: else\n"); }
while                   { printf("KEYWORD: while\n"); }
cin                     { printf("KEYWORD: cin\n"); }
cout                    { printf("KEYWORD: cout\n"); }
using                   { printf("KEYWORD: using\n"); }
namespace               { printf("KEYWORD: namespace\n"); }
main                    { printf("KEYWORD: main\n"); }

[0-9]+\.[0-9]+          { printf("REAL: %s\n", yytext); }
[0-9]+                  { printf("INT: %s\n", yytext); }

[A-Za-z_][A-Za-z0-9_]*  {
                            if(strlen(yytext) > 10)
                                yyerror("identificator prea lung");
                            else
                                printf("ID: %s\n", yytext);
                        }

"=="                    { printf("DELIM: %s\n", yytext); }
"!="                    { printf("DELIM: %s\n", yytext); }
"<="                    { printf("DELIM: %s\n", yytext); }
">="                    { printf("DELIM: %s\n", yytext); }
"&&"                    { printf("DELIM: %s\n", yytext); }
"||"                    { printf("DELIM: %s\n", yytext); }
"<<"                    { printf("DELIM: %s\n", yytext); }
">>"                    { printf("DELIM: %s\n", yytext); }
"::"                    { printf("DELIM: %s\n", yytext); }

";"     { printf("DELIM: ;\n"); }
","     { printf("DELIM: ,\n"); }
"{"     { printf("DELIM: {\n"); }
"}"     { printf("DELIM: }\n"); }
"("     { printf("DELIM: (\n"); }
")"     { printf("DELIM: )\n"); }
"+"     { printf("DELIM: +\n"); }
"-"     { printf("DELIM: -\n"); }
"*"     { printf("DELIM: *\n"); }
"/"     { printf("DELIM: /\n"); }
"%"     { printf("DELIM: %\n"); }
"="     { printf("DELIM: =\n"); }
"<"     { printf("DELIM: <\n"); }
">"     { printf("DELIM: >\n"); }
[.]     { printf("DELIM: .\n"); }

.                       { yyerror("caracter invalid"); }

%%

int yywrap() { return 1; }

int main() {
    yylex();
    return 0;
}
