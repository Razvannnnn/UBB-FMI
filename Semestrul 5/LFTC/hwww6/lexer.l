%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "parser.tab.h"

void yyerror(const char *s);
%}

%option noyywrap
%option yylineno

%%

[ \t\r\n]+          ;

"int"               { return INT; }
"main"              { return MAIN; }
"cin"               { return CIN; }
"cout"              { return COUT; }

">>"                { return READ_OP; }
"<<"                { return WRITE_OP; }
"="                 { return ASSIGN; }
"+"                 { return PLUS; }
"-"                 { return MINUS; }
"*"                 { return MULT; }
"/"                 { return DIV; }

"("                 { return LPAREN; }
")"                 { return RPAREN; }
"{"                 { return LBRACE; }
"}"                 { return RBRACE; }
";"                 { return SEMICOLON; }

[a-zA-Z][a-zA-Z0-9_]* {
    yylval.id = strdup(yytext);
    return ID;
}

[0-9]+ {
    yylval.val = atoi(yytext);
    return INT_CONST;
}

. {
    fprintf(stderr, "Unexpected character: '%s' (ASCII %d) at line %d\n", yytext, (unsigned char)yytext[0], yylineno);
    exit(1);
}

%%