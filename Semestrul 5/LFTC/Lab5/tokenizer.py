# tokenizer.py
import re
from dataclasses import dataclass
import sys

KEYWORDS = {
    "int",
    "float",
    "if",
    "else",
    "while",
    "read_int",
    "read_real",
    "print",
    "struct",
    "Point",
}

# regex-uri pentru token-uri

TOKENS_SPEC = [
    ("COMMENT", r"//[^\n]*|#[^\n]*"),
    ("WS", r"[ \t\r\f\v]+"),
    ("NEWLINE", r"\n"),
    # se trece peste
    ("OP_SHL", r"<<"),
    ("OP_SHR", r">>"),
    ("SCOPE", r"::"),  # std::cout
    # de scop
    ("OP_CMP", r"==|!=|<=|>=|<|>"),
    ("OP", r"\+|-|\*|/|%"),
    ("ASSIGN", r"="),
    # operatii
    ("DOT", r"\."),  # circle.radius
    #
    ("LBRACE", r"\{"),
    ("RBRACE", r"\}"),
    ("LPAREN", r"\("),
    ("RPAREN", r"\)"),
    ("COMMA", r","),
    ("SEMI", r";"),
    #
    ("STRING", r"\"([^\"\\]|\\.)*\""),  # "text", cu escape-uri
    # tipuri de date
    ("REAL", r"(?:\d+\.\d+)(?:[eE][+-]?\d+)?"),
    ("INT", r"(?:0|[1-9]\d*)"),
    ("IDENT", r"[A-Za-z][A-Za-z0-9_]{0,19}"),
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


def check_constant_style(tokens):
    i = 0
    n = len(tokens)

    def skip_ws_comments(j):
        return j

    while i < n:
        t = tokens[i]
        if t.kind == "IDENT":
            j = skip_ws_comments(i + 1)
            if j < n and tokens[j].kind == "ASSIGN":
                k = skip_ws_comments(j + 1)
                if k < n and tokens[k].kind in ("INT", "REAL"):
                    name = t.value
                    # daca numele nu incepe cu const , se sare
                    if not name.startswith("const_"):
                        i += 1
                        continue

                    # regula de UPPERCASE si <=10 lungimea
                    if not all(
                        ch.isupper() or ch.isdigit() or ch == "_" for ch in name
                    ):
                        raise LexicalError(
                            f"Constanta trebuie scrisa cu majuscule (linia {t.line}, col {t.col}): {name}"
                        )
                    if len(name) > 10:
                        raise LexicalError(
                            f"Constanta are mai mult de 10 caractere (linia {t.line}, col {t.col}): {name}"
                        )
        i += 1


def lex(source: str) -> list[Token]:
    tokens = []
    line = 1
    col = 1
    pos = 0
    n = len(source)

    while pos < n:
        m = MASTER_RE.match(source, pos)
        if not m:  # caracter necunoscut
            snippet = source[pos : pos + 20].replace(
                "\n", "\\n"
            )  # luam ultimele caractere si le afisam
            raise LexicalError(
                f"Caracter nevalid la linia {line}, coloana {col}: '{source[pos]}' ... \"{snippet}\""
            )

        kind = m.lastgroup
        text = m.group(kind)  # pyright:ignore
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
                if text in KEYWORDS:
                    kind = "KEYWORD"
                elif text.startswith("_"):
                    raise LexicalError(
                        f"Identificatorul nu poate incepe cu '_' (linia {line}, col {start_col}): {text}"
                    )
                tokens.append(Token(kind, text, line, start_col))
            else:
                tokens.append(Token(kind, text, line, start_col))

        consumed = len(text)
        col += consumed
        pos = m.end()
    return tokens


def print_tokens(tokens):
    for t in tokens:
        print(f"({t.line}:{t.col}) {t.kind:<11}  {t.value}")


def write_tokens(tokens, path):
    with open(path, "w", encoding="utf-8") as f:
        for t in tokens:
            f.write(f"({t.line}:{t.col}) {t.kind:<11}  {t.value}\n")


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
        print(f"Eroare lexicala : {e}", file=sys.stderr)
        sys.exit(2)

    try:
        check_constant_style(tokens)
    except LexicalError as e:
        print(f"Eroare lexicala : {e}", file=sys.stderr)
        sys.exit(2)

    if out:
        write_tokens(tokens, out)
        print(f"Am scris {len(tokens)} token-uri în {out}")
    else:
        print_tokens(tokens)


if __name__ == "__main__":
    main()
