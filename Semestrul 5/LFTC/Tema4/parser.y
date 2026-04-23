%{
#include <stdio.h>
#include <stdlib.h>

extern int yylex();
extern int current_line;

void yyerror(const char *s);
%}

%union {
    int intval;
    float floatval;
    char* strval;
}

%token <strval> IDENT
%token <intval> INT_VAL
%token <floatval> REAL_VAL
%token <strval> STRING_VAL

%token KW_INT KW_FLOAT KW_DOUBLE KW_STRUCT
%token KW_IF KW_ELSE KW_WHILE
%token KW_CIN KW_COUT KW_USING KW_NAMESPACE KW_MAIN
%token KW_DACA KW_ATUNCI KW_SFDACA

%token ASSIGN
%token EQ NEQ LT GT LE GE
%token AND OR
%token LSHIFT RSHIFT
%token SCOPE
%token PLUS MINUS MUL DIV MOD
%token SEMICOLON COMMA DOT
%token LBRACE RBRACE LPAREN RPAREN

%left OR
%left AND
%left EQ NEQ
%left LT GT LE GE
%left LSHIFT RSHIFT
%left PLUS MINUS
%left MUL DIV MOD
%nonassoc UMINUS

%nonassoc LOWER_THAN_ELSE
%nonassoc KW_ELSE

%%

program:
      headers declarations
    ;

headers:
    | headers KW_USING KW_NAMESPACE IDENT SEMICOLON
    ;

declarations:
    | declarations decl_struct
    | declarations decl_var
    | declarations func_def
    ;

decl_struct:
      KW_STRUCT IDENT LBRACE struct_members RBRACE SEMICOLON
    ;

struct_members:
    | struct_members type IDENT SEMICOLON
    ;

decl_var:
      type list_ident SEMICOLON
    ;

list_ident:
      IDENT
    | list_ident COMMA IDENT
    | IDENT ASSIGN expression
    | list_ident COMMA IDENT ASSIGN expression
    ;

type:
      KW_INT
    | KW_FLOAT
    | KW_DOUBLE
    | KW_STRUCT IDENT
    ;

func_def:
      type IDENT LPAREN param_list RPAREN block
    | KW_INT KW_MAIN LPAREN RPAREN block
    ;

param_list:
    | param_list_items
    ;

param_list_items:
      param_def
    | param_list_items COMMA param_def
    ;

param_def:
      type IDENT
    ;

block:
      LBRACE stmts RBRACE
    ;

stmts:
    | stmts stmt
    ;

stmt:
      decl_var
    | assignment SEMICOLON
    | if_stmt
    | while_stmt
    | daca_stmt
    | io_stmt
    | block
    | SEMICOLON
    ;

lvalue:
      IDENT
    | IDENT DOT IDENT
    ;

assignment:
      lvalue ASSIGN expression
    ;

if_stmt:
      KW_IF LPAREN condition RPAREN stmt %prec LOWER_THAN_ELSE
    | KW_IF LPAREN condition RPAREN stmt KW_ELSE stmt
    ;

while_stmt:
      KW_WHILE LPAREN condition RPAREN stmt
    ;

daca_stmt:
      KW_DACA LPAREN condition RPAREN KW_ATUNCI stmts KW_SFDACA
    ;

io_stmt:
      KW_CIN cin_list SEMICOLON
    | KW_COUT cout_list SEMICOLON
    ;

cin_list:
      RSHIFT lvalue
    | cin_list RSHIFT lvalue
    ;

cout_list:
      LSHIFT expression
    | cout_list LSHIFT expression
    ;

condition:
      expression
    ;

expression:
      expression PLUS expression
    | expression MINUS expression
    | expression MUL expression
    | expression DIV expression
    | expression MOD expression
    | expression EQ expression
    | expression NEQ expression
    | expression LT expression
    | expression GT expression
    | expression LE expression
    | expression GE expression
    | expression AND expression
    | expression OR expression
    | LPAREN expression RPAREN
    | IDENT
    | IDENT DOT IDENT
    | INT_VAL
    | REAL_VAL
    | STRING_VAL
    | MINUS expression %prec UMINUS
    ;

%%

void yyerror(const char *s) {
    fprintf(stderr, "Eroare Sintactica: %s la linia %d\n", s, current_line);
    exit(1);
}

int main(int argc, char** argv) {
    printf("Incepe analiza sintactica...\n");
    if (yyparse() == 0) {
        printf("Analiza sintactica finalizata cu SUCCES!\n");
    }
    return 0;
}