import re
from dataclasses import dataclass
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
def lex(source: str) -> list[Token]:
    tokens = []
    line = 1
    col = 1
    pos = 0
    n = len(source)

    while pos < n:
        m = MASTER_RE.match(source, pos)

        kind = m.lastgroup
        text = m.group(kind)
        start_col = col

        if kind == "NEWLINE":
            line += 1
            col = 1
            pos = m.end()
            continue
        elif kind in ("WS", "COMMENT"):
            # ignoram spatiile albe si comentariile
            pass
        else:
            if kind == "IDENT":
                # daca e keyword
                if text in KEYWORDS:
                    kind = "KEYWORD"
            tokens.append(Token(kind, text, line, start_col))

        consumed = len(text)
        col += consumed
        pos = m.end()
    return tokens



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
        tokens = lex(src)
    except LexicalError as e:
        print(f"Lexical error: {e}", file=sys.stderr)
        sys.exit(2)

    if out:
        write_tokens(tokens, out)
        print(f"Wrote {len(tokens)} tokens to {out}")
    else:
        print_tokens(tokens)


if __name__ == "__main__":
    main()
