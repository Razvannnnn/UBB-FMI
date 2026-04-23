%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int yylex();
void yyerror(const char *s);
extern int yylineno;
extern FILE* yyin;

FILE *asm_out;

// Helper - define data section variables
void add_data_decl(char* id) {
    fprintf(asm_out, "\t%s dd 0\n", id);
}

// Helper: ASM Header
void write_header() {
    fprintf(asm_out, "extern printf\n");
    fprintf(asm_out, "extern scanf\n");
    fprintf(asm_out, "section .data\n");
    fprintf(asm_out, "\tformat_in db \"%%d\", 0\n");
    fprintf(asm_out, "\tformat_out db \"%%d\", 10, 0\n");
}

// Helper: Start Text Section
void start_text() {
    fprintf(asm_out, "\nsection .text\n");
    fprintf(asm_out, "\tglobal main\n");
    fprintf(asm_out, "main:\n");
    fprintf(asm_out, "\tpush rbp\n");
    fprintf(asm_out, "\tmov rbp, rsp\n");
}

// Helper: Function Epilogue
void write_epilogue() {
    fprintf(asm_out, "\tmov rsp, rbp\n");
    fprintf(asm_out, "\tpop rbp\n");
    fprintf(asm_out, "\tret\n");
}

%}

%union {
    char* id;
    int val;
}

%token INT MAIN CIN COUT
%token READ_OP WRITE_OP ASSIGN
%token PLUS MINUS MULT DIV
%token LPAREN RPAREN LBRACE RBRACE SEMICOLON
%token <id> ID
%token <val> INT_CONST

%start program

%%

program:
    INT MAIN LPAREN RPAREN LBRACE { write_header(); } declarations { start_text(); } statement_list RBRACE { write_epilogue(); }
    ;

/* --- DECLARATIONS --- */
declarations:
    declarations declaration
    | /* empty */
    ;

declaration:
    INT ID SEMICOLON {
        add_data_decl($2);
        free($2);
    }
    ;

/* --- STATEMENTS --- */
statement_list:
    statement statement_list
    | /* empty */
    ;

statement:
    assign_stmt
    | read_stmt
    | write_stmt
    ;

assign_stmt:
    ID ASSIGN expression SEMICOLON {
        fprintf(asm_out, "\tpop rax\n");
        fprintf(asm_out, "\tmov [%s], eax\n", $1);
        free($1);
    }
    ;

read_stmt:
    CIN READ_OP ID SEMICOLON {
        fprintf(asm_out, "\tmov rsi, %s\n", $3);
        fprintf(asm_out, "\tmov rdi, format_in\n");
        fprintf(asm_out, "\txor rax, rax\n");
        fprintf(asm_out, "\tcall scanf\n");
        free($3);
    }
    ;

write_stmt:
    COUT WRITE_OP expression SEMICOLON {
        fprintf(asm_out, "\tpop rsi\n");
        fprintf(asm_out, "\tmov rdi, format_out\n");
        fprintf(asm_out, "\txor rax, rax\n");
        fprintf(asm_out, "\tcall printf\n");
    }
    ;



expression:
    expression PLUS term {
        fprintf(asm_out, "\tpop rbx\n");
        fprintf(asm_out, "\tpop rax\n");
        fprintf(asm_out, "\tadd rax, rbx\n");
        fprintf(asm_out, "\tpush rax\n");
    }
    | expression MINUS term {
        fprintf(asm_out, "\tpop rbx\n");
        fprintf(asm_out, "\tpop rax\n");
        fprintf(asm_out, "\tsub rax, rbx\n");
        fprintf(asm_out, "\tpush rax\n");
    }
    | term
    ;


term:
    term MULT factor {
        fprintf(asm_out, "\tpop rbx\n");
        fprintf(asm_out, "\tpop rax\n");
        fprintf(asm_out, "\timul rax, rbx\n");
        fprintf(asm_out, "\tpush rax\n");
    }
    | term DIV factor {
        fprintf(asm_out, "\tpop rbx\n");
        fprintf(asm_out, "\tpop rax\n");
        fprintf(asm_out, "\tcqo\n");
        fprintf(asm_out, "\tidiv rbx\n");
        fprintf(asm_out, "\tpush rax\n");
    }
    | factor
    ;



factor:
    ID {
        fprintf(asm_out, "\tmov eax, [%s]\n", $1);
        fprintf(asm_out, "\tpush rax\n");
        free($1);
    }
    | INT_CONST {
        fprintf(asm_out, "\tmov rax, %d\n", $1);
        fprintf(asm_out, "\tpush rax\n");
    }
    | LPAREN expression RPAREN
    ;

%%

void yyerror(const char *s) {
    fprintf(stderr, "Error at line %d: %s\n", yylineno, s);
    exit(1);
}

int main(int argc, char **argv) {
    if (argc < 2) {
        printf("Usage: %s <input_file>\n", argv[0]);
        return 1;
    }

    yyin = fopen(argv[1], "r");
    if (!yyin) {
        perror("Error opening file");
        return 1;
    }

    asm_out = fopen("output.asm", "w");
    if (!asm_out) {
        perror("Error creating output.asm");
        return 1;
    }

    yyparse();

    printf("Compilation successful! 'output.asm' generated.\n");

    fclose(asm_out);
    fclose(yyin);
    return 0;
}