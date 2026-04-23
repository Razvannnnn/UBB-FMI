# Tema

## Enunț general

Scrierea unui **ANALIZOR LEXICAL** pentru un minilimbaj de programare (MLP), ales ca subset al unui limbaj existent.

---

## Specificarea minilimbajului de programare (MLP)

### Tipuri de date
- `int` → Tip de date ce reprezintă numere întregi.  
- `float` → Tip de date ce reprezintă numere reale.  
- `struct` → Folosit la definirea unui nou tip de date.

---

### Instrucțiuni
- **Atribuire** → `=`
- **Intrare/Ieșire** → `cin >>` / `cout <<`
- **Selecție**
  ```cpp
  if (cond) { ceva } 
  else { altceva }
- **Ciclare**
  ```cpp
  while (cond) { ceva }

### Restrictii
- Identificatorii încep cu literă sau _ și pot continua cu litere, cifre și _.
- Lungimea maximă este de 10 caractere.

### Gramatica in BNF

```
<program> ::= <directive> <blocuri-globale> <functie-principala>

<directive> ::= (vid) | <directiva> <directive>
<directiva> ::= <include> | <using>
<include> ::= #include < <nume-fisier> >
<nume-fisier> ::= ID | ID . ID | ID / ID | iostream
<using> ::= using namespace ID ;

<blocuri-globale> ::= (vid) | <definire-struct> <blocuri-globale>

<definire-struct> ::= struct <identifier> { <lista-campuri> } ;
<lista-campuri> ::= (vid) | <camp> <lista-campuri>
<camp> ::= <tip> ID ;

<functie-principala> ::= int main() { <instructiuni> }
<instructiuni> ::= (vid) | <instructiune> ; <instructiuni> | <instructiune-speciala> <instructiuni>
<instructiune> ::= <declaratie> | <atribuire> | <instructiune-citire> | <instructiune-afisare> | <expresie>
<instructiune-speciala> ::= <instructiune-conditionala> | <instructiune-bucla>

<declaratie> ::= <tip> <lista-declarari>
<lista-declarari> ::= <identificator-declarat> | <identificator-declarat> , <lista-declarari>
<identificator-declarat> ::= ID | ID = <expresie>
<tip> ::= int | float | struct ID

<atribuire> ::= <lvalue> = <expresie>
<lvalue> ::= ID | ID . ID
<expresie> ::= <termen> | <termen> <operator-binar> <expresie>
<termen> ::= ID | ( <expresie> )
<operator-binar> ::= + | - | * | / | % | == | != | < | > | <= | >= | && | ||

<instructiune-citire> ::= cin >> <lista-citire>
<lista-citire> ::= ID | ID >> <lista-citire>

<instructiune-afisare> ::= cout << <lista-afisare>
<lista-afisare> ::= <expresie> | <expresie> << <lista-afisare>

<instructiune-conditionala> ::= if ( <conditie> ) { <instructiuni> } else { <instructiuni> }
<conditie> ::= <expresie>

<instructiune-bucla> ::= while ( <conditie> ) { <instructiuni> }

<ID> ::= <start-char> | <start-char> <cont-char> | <start-char> <cont-char> <cont-char> | ... | <start-char> <cont-char>^9

<start-char> ::= <letter> | "_"
<cont-char> ::= <letter> | <digit> | "_"
<letter> ::= "A" | "B" | ... | "Z" | "a" | "b" | ... | "z"
<digit> ::= "0" | "1" | ... | "9"

```