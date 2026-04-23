import re
from dataclasses import dataclass
from tabela_simboluri import BST, FIPEntry, write_TS_bst, write_FIP, BSTNode
import sys

# CONFIGURATIE MLP # ==============================
KEYWORDS = {
    "int",
    "float",
    "struct",
    "if",
    "else",
    "while",
    "cin",
    "cout",
    "using",
    "namespace",
    "main",
}

# SPECIFICATII TOKENURI ==============================
TOKENS_SPEC = [
    # comentarii si directive preprocesor
    ("COMMENT", r"//[^\n]*|#[^\n]*"),

    ("WS", r"[ \t\r\f\v]+"),
    ("NEWLINE", r"\n"),

    # operatori si delimitatori
    ("OP_SHL", r"<<"),
    ("OP_SHR", r">>"),
    ("SCOPE", r"::"),
    ("OP_CMP", r"==|!=|<=|>=|<|>"),
    ("LOGIC_OP", r"&&|\|\|"),
    ("OP", r"\+|-|\*|/|%"),
    ("ASSIGN", r"="),

    ("DOT", r"\."),
    ("LBRACE", r"\{"),
    ("RBRACE", r"\}"),
    ("LPAREN", r"\("),
    ("RPAREN", r"\)"),
    ("COMMA", r","),
    ("SEMI", r";"),

    # constante
    ("STRING", r"\"([^\"\\]|\\.)*\""),
    ("REAL", r"(?:\d+\.\d+)(?:[eE][+-]?\d+)?"),
    ("INT", r"(?:0|[1-9]\d*)"),

    # identificatori
    ("IDENT", r"[A-Za-z_][A-Za-z0-9_]*"),
]

MASTER_RE = re.compile("|".join(f"(?P<{name}>{pat})" for name, pat in TOKENS_SPEC))


@dataclass
class Token:
    kind: str
    value: str
    line: int
    col: int


class LexicalError(Exception):
    pass


# ANALIZA LEXICALA ==============================
def lex(source: str):
    fip_entries = []
    TS_ident = BST()
    TS_const = BST()

    line = 1
    col = 1
    pos = 0
    n = len(source)

    while pos < n:
        m = MASTER_RE.match(source, pos)
        if not m:
            snippet = source[pos:pos+20].replace("\n", "\\n")
            raise LexicalError(
                f"Eroare lexicală la linia {line}, coloana {col}: caracter invalid '{source[pos]}' ... \"{snippet}\""
            )

        kind = m.lastgroup
        text = m.group(kind)
        start_col = col

        if kind == "NEWLINE":
            line += 1
            col = 1
            pos = m.end()
            continue

        elif kind in ("WS", "COMMENT"):
            pass

        else:
            if kind == "IDENT":
                # daca e keyword
                if text in KEYWORDS:
                    kind = "KEYWORD"
                    fip_entries.append(FIPEntry(kind, text, line, start_col))
                else:
                    # verif lungime identific
                    if len(text) > 10:
                        raise LexicalError(
                            f"Eroare lexicala la linia {line}, coloana {start_col}: "
                            f"identificatorul '{text}' depaseste lungimea maxima de 10 caractere."
                        )

                    # indentific valid => in TS
                    node = TS_ident.search(text)
                    if not node:
                        node = TS_ident.insert(text)
                    fip_entries.append(FIPEntry("ID", node, line, start_col))

            # Constante
            elif kind in ("INT", "REAL", "STRING"):
                node = TS_const.search(text)
                if not node:
                    node = TS_const.insert(text, kind)
                fip_entries.append(FIPEntry("CONST", node, line, start_col))

            # restu de tokenuri
            else:
                fip_entries.append(FIPEntry(kind, text, line, start_col))

        consumed = len(text)
        col += consumed
        pos = m.end()

    return fip_entries, TS_ident, TS_const




# AFISARE SI SCRIERE ==============================
def print_tokens(tokens):
    for t in tokens:
        print(f"({t.line}:{t.col}) {t.kind:<10} {t.value}")


def write_tokens(tokens, path):
    with open(path, "w", encoding="utf-8") as f:
        for t in tokens:
            f.write(f"({t.line}:{t.col}) {t.kind:<10} {t.value}\n")


# MAIN ==============================
def main():
    if len(sys.argv) < 2:
        print("Usage: python3 tokenizer.py <input_file> [--out tokens.txt]")
        sys.exit(1)

    inp = sys.argv[1]
    out = None
    if len(sys.argv) >= 4 and sys.argv[2] == "--out":
        out = sys.argv[3]

    with open(inp, "r", encoding="utf-8") as f:
        src = f.read()

    try:
        fip_entries, TS_ident, TS_const = lex(src)
    except LexicalError as e:
        print(f"Lexical error: {e}", file=sys.stderr)
        sys.exit(2)

    # Scriere TS și FIP
    write_TS_bst(TS_ident, "out_files/TS_ident.txt")
    write_TS_bst(TS_const, "out_files/TS_const.txt")
    write_FIP(fip_entries, "out_files/FIP.txt")

    print(f"TS_ident.txt, TS_const.txt și FIP.txt generate cu succes!")

    if out:
        # opțional: print tokens ca înainte
        with open(out, "w", encoding="utf-8") as f:
            for entry in fip_entries:
                if isinstance(entry.aux, BSTNode):
                    aux_val = entry.aux.key
                else:
                    aux_val = entry.aux
                f.write(f"{entry.token_class}\t{aux_val}\n")
        print(f"Wrote {len(fip_entries)} entries to {out}")



if __name__ == "__main__":
    main()
