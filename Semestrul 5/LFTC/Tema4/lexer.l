%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "parser.tab.h"

int current_line = 1;
int col = 1;

void update_loc() {
    col += yyleng;
}
%}

IDENT   [A-Za-z_][A-Za-z0-9_]*
INT     [0-9]+
REAL    [0-9]+"."[0-9]+
STRING  \"([^\"\n]*)\"

%%

[ \t\r]+            { update_loc(); }
[\n]                { current_line++; col = 1; }

"//".* { update_loc(); }
"#".* { update_loc(); }

\"([^\"\n]*)\n      {
                        fprintf(stderr, "Eroare Lexicala: Sir de caractere neinchis la linia %d\n", current_line);
                        exit(1);
                    }

{STRING}            { update_loc(); return STRING_VAL; }

"int"               { update_loc(); return KW_INT; }
"float"             { update_loc(); return KW_FLOAT; }
"double"            { update_loc(); return KW_DOUBLE; } /* ADAGUAT PENTRU A SUPORTA TESTUL TĂU */
"struct"            { update_loc(); return KW_STRUCT; }
"if"                { update_loc(); return KW_IF; }
"else"              { update_loc(); return KW_ELSE; }
"while"             { update_loc(); return KW_WHILE; }
"cin"               { update_loc(); return KW_CIN; }
"cout"              { update_loc(); return KW_COUT; }
"using"             { update_loc(); return KW_USING; }
"namespace"         { update_loc(); return KW_NAMESPACE; }
"main"              { update_loc(); return KW_MAIN; }

"daca"              { update_loc(); return KW_DACA; }
"atunci"            { update_loc(); return KW_ATUNCI; }
"sfdaca"            { update_loc(); return KW_SFDACA; }

{REAL}              { update_loc(); return REAL_VAL; }
{INT}               { update_loc(); return INT_VAL; }

{IDENT}             { 
                        if (yyleng > 10) {
                             fprintf(stderr, "Eroare Lexicala: Identificator '%s' prea lung (>10 caractere) la linia %d\n", yytext, current_line);
                             exit(1);
                        }
                        update_loc(); 
                        return IDENT; 
                    }

"=="                { update_loc(); return EQ; }
"!="                { update_loc(); return NEQ; }
"<="                { update_loc(); return LE; }
">="                { update_loc(); return GE; }
"&&"                { update_loc(); return AND; }
"||"                { update_loc(); return OR; }
"<<"                { update_loc(); return LSHIFT; }
">>"                { update_loc(); return RSHIFT; }
"::"                { update_loc(); return SCOPE; }

";"                 { update_loc(); return SEMICOLON; }
","                 { update_loc(); return COMMA; }
"{"                 { update_loc(); return LBRACE; }
"}"                 { update_loc(); return RBRACE; }
"("                 { update_loc(); return LPAREN; }
")"                 { update_loc(); return RPAREN; }
"+"                 { update_loc(); return PLUS; }
"-"                 { update_loc(); return MINUS; }
"*"                 { update_loc(); return MUL; }
"/"                 { update_loc(); return DIV; }
"%"                 { update_loc(); return MOD; }
"="                 { update_loc(); return ASSIGN; }
"<"                 { update_loc(); return LT; }
">"                 { update_loc(); return GT; }
"."                 { update_loc(); return DOT; }

.                   {
                        fprintf(stderr, "Eroare Lexicala: Caracter necunoscut '%s' la linia %d\n", yytext, current_line);
                        exit(1);
                    }

%%

int yywrap() { return 1; }