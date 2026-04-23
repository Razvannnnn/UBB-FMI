%{
#include <stdio.h>
#include "palindrom.tab.h"
%}

%option noyywrap

%%

[0-9]   { 
            yylval.ival = atoi(yytext);
            return DIGIT; 
        }

":"     { return COLON; }

\n      { return EOL; }

[ \t]+

.       { printf("Caracter invalid: %s\n", yytext); }

%%
