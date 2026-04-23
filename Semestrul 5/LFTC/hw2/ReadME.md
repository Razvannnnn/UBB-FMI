# AFD / AF Project


Acest proiect contine un program Python care citeste un automat finit (posibil nedeterminist) dintr-un fisier sau de la tastatura, afiseaza elementele sale si, pentru automate deterministe (AFD), verifica daca o secventa este acceptata și determina cel mai lung prefix acceptat.


## Structura fisierului de intrare (EBNF)


```
<file> ::= <sections>
<sections> ::= { <section> }
<section> ::= "STATES:" <state-list> "\n"
| "ALPHABET:" <symbol-list> "\n"
| "START:" <state> "\n"
| "FINAL:" <state-list> "\n"
| "TRANSITIONS:" <transition-list>


<state-list> ::= <state> { "," <state> }
<symbol-list> ::= <symbol> { "," <symbol> }
<transition-list> ::= { <transition> }
<transition> ::= <from-state> ":" <symbol> "->" <to-state> "\n"


# lexemes
<state> ::= <identifier>
<identifier> ::= letter { letter | digit | "_" }
<symbol> ::= <char> ; a single visible character (e.g. 0-9, a, A, +, - , x, X)
<char> ::= any printable character except ',' ':' '\\n' '\\r'


# Observatii:
# - Separatorul intre state-uri si simboluri este virgula.
# - In sectiunea TRANSITIONS fiecare tranzitie e pe linie noua si are forma:
# q0:0->q1
# unde simbolul este un singur caracter. Dacă doriti un simbol spatiu, folositi '_space_'
# - Ordinea sectiunilor nu conteaza, dar toate sectiunile necesare (STATES, ALPHABET, START, FINAL, TRANSITIONS) trebuie prezente.


```


## Cum rulezi


1. `python3 main.py` - programul afiseaza un meniu.
2. Alege `Load from file` si da calea catre `test_dfa_int_constants.txt` sau alt fisier conform EBNF.
3. Poti afisa multimea starilor, alfabetul, tranzitiile si starile finale.
4. Pentru AFD, foloseste optiunile `Check sequence` si `Longest accepted prefix`.

