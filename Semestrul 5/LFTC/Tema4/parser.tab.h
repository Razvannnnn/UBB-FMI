/* A Bison parser, made by GNU Bison 3.8.2.  */

/* Bison interface for Yacc-like parsers in C

   Copyright (C) 1984, 1989-1990, 2000-2015, 2018-2021 Free Software Foundation,
   Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* DO NOT RELY ON FEATURES THAT ARE NOT DOCUMENTED in the manual,
   especially those whose name start with YY_ or yy_.  They are
   private implementation details that can be changed or removed.  */

#ifndef YY_YY_PARSER_TAB_H_INCLUDED
# define YY_YY_PARSER_TAB_H_INCLUDED
/* Debug traces.  */
#ifndef YYDEBUG
# define YYDEBUG 0
#endif
#if YYDEBUG
extern int yydebug;
#endif

/* Token kinds.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
  enum yytokentype
  {
    YYEMPTY = -2,
    YYEOF = 0,                     /* "end of file"  */
    YYerror = 256,                 /* error  */
    YYUNDEF = 257,                 /* "invalid token"  */
    IDENT = 258,                   /* IDENT  */
    INT_VAL = 259,                 /* INT_VAL  */
    REAL_VAL = 260,                /* REAL_VAL  */
    STRING_VAL = 261,              /* STRING_VAL  */
    KW_INT = 262,                  /* KW_INT  */
    KW_FLOAT = 263,                /* KW_FLOAT  */
    KW_DOUBLE = 264,               /* KW_DOUBLE  */
    KW_STRUCT = 265,               /* KW_STRUCT  */
    KW_IF = 266,                   /* KW_IF  */
    KW_ELSE = 267,                 /* KW_ELSE  */
    KW_WHILE = 268,                /* KW_WHILE  */
    KW_CIN = 269,                  /* KW_CIN  */
    KW_COUT = 270,                 /* KW_COUT  */
    KW_USING = 271,                /* KW_USING  */
    KW_NAMESPACE = 272,            /* KW_NAMESPACE  */
    KW_MAIN = 273,                 /* KW_MAIN  */
    KW_DACA = 274,                 /* KW_DACA  */
    KW_ATUNCI = 275,               /* KW_ATUNCI  */
    KW_SFDACA = 276,               /* KW_SFDACA  */
    ASSIGN = 277,                  /* ASSIGN  */
    EQ = 278,                      /* EQ  */
    NEQ = 279,                     /* NEQ  */
    LT = 280,                      /* LT  */
    GT = 281,                      /* GT  */
    LE = 282,                      /* LE  */
    GE = 283,                      /* GE  */
    AND = 284,                     /* AND  */
    OR = 285,                      /* OR  */
    LSHIFT = 286,                  /* LSHIFT  */
    RSHIFT = 287,                  /* RSHIFT  */
    SCOPE = 288,                   /* SCOPE  */
    PLUS = 289,                    /* PLUS  */
    MINUS = 290,                   /* MINUS  */
    MUL = 291,                     /* MUL  */
    DIV = 292,                     /* DIV  */
    MOD = 293,                     /* MOD  */
    SEMICOLON = 294,               /* SEMICOLON  */
    COMMA = 295,                   /* COMMA  */
    DOT = 296,                     /* DOT  */
    LBRACE = 297,                  /* LBRACE  */
    RBRACE = 298,                  /* RBRACE  */
    LPAREN = 299,                  /* LPAREN  */
    RPAREN = 300,                  /* RPAREN  */
    UMINUS = 301,                  /* UMINUS  */
    LOWER_THAN_ELSE = 302          /* LOWER_THAN_ELSE  */
  };
  typedef enum yytokentype yytoken_kind_t;
#endif

/* Value type.  */
#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
union YYSTYPE
{
#line 11 "parser.y"

    int intval;
    float floatval;
    char* strval;

#line 117 "parser.tab.h"

};
typedef union YYSTYPE YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define YYSTYPE_IS_DECLARED 1
#endif


extern YYSTYPE yylval;


int yyparse (void);


#endif /* !YY_YY_PARSER_TAB_H_INCLUDED  */
