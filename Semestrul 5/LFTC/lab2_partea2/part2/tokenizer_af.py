import sys
from automatFinit import AF
from tabela_simboluri import BST, FIPEntry, write_TS_bst, write_FIP, BSTNode

KEYWORDS = {
    "int", "float", "struct", "if", "else", "while",
    "cin", "cout", "using", "namespace", "main"
}

DELIMITERS = {
    ";", ",", "{", "}", "(", ")", "+", "-", "*", "/", "%",
    "=", "<", ">", "<=", ">=", "==", "!=", "&&", "||", "::", "<<", ">>", "."
}

def load_afs():
    af_ident = AF()
    af_ident.read_file("af_ident.txt")

    af_int = AF()
    af_int.read_file("af_int.txt")

    af_real = AF()
    af_real.read_file("af_real.txt")

    return af_ident, af_int, af_real


def lex(source: str, af_ident: AF, af_int: AF, af_real: AF):
    fip_entries = []
    TS_ident = BST()
    TS_const = BST()

    i = 0
    line = 1
    col = 1
    n = len(source)

    def advance(idx, ch):
        nonlocal line, col
        if ch == '\n':
            line += 1
            col = 1
        else:
            col += 1
        return idx + 1

    while i < n:
        ch = source[i]

        if ch.isspace():
            i = advance(i, ch)
            continue

        if ch == '/' and i + 1 < n and source[i + 1] == '/':
            while i < n and source[i] != '\n':
                i = advance(i, source[i])
            continue

        if ch == '#':
            while i < n and source[i] != '\n':
                i = advance(i, source[i])
            continue

        if ch == '"':
            start_col = col
            literal = '"'
            i = advance(i, ch)
            while i < n and source[i] != '"':
                if source[i] == '\n':
                    raise Exception(f"Eroare: string neinchis la linia {line}, coloana {start_col}")
                literal += source[i]
                i = advance(i, source[i])
            if i < n and source[i] == '"':
                literal += '"'
                i = advance(i, source[i])
            node = TS_const.search(literal)
            if not node:
                node = TS_const.insert(literal, "STRING")
            fip_entries.append(FIPEntry("CONST", node, line, start_col))
            continue


        # testeaza identificator
        prefix_ident = af_ident.longest_accepted_prefix(source[i:])
        prefix_int = af_int.longest_accepted_prefix(source[i:])
        prefix_real = af_real.longest_accepted_prefix(source[i:])

        longest = max(
            [(prefix_ident, "IDENT"), (prefix_real, "REAL"), (prefix_int, "INT")],
            key=lambda x: len(x[0]) if x[0] else 0
        )
        token_text, token_type = longest

        if token_text:
            start_col = col
            # daca e keyword
            if token_type == "IDENT" and token_text in KEYWORDS:
                fip_entries.append(FIPEntry("KEYWORD", token_text, line, start_col))
            elif token_type == "IDENT":
                if len(token_text) > 10:
                    raise Exception(
                        f"Eroare lexicala la linia {line}, coloana {start_col}: "
                        f"identificatorul '{token_text}' depaseste lungimea maxima de 10 caractere."
                    )
                node = TS_ident.search(token_text)
                if not node:
                    node = TS_ident.insert(token_text)
                fip_entries.append(FIPEntry("ID", node, line, start_col))
            elif token_type in ("INT", "REAL"):
                node = TS_const.search(token_text)
                if not node:
                    node = TS_const.insert(token_text, token_type)
                fip_entries.append(FIPEntry("CONST", node, line, start_col))

            # avansam
            for c in token_text:
                i = advance(i, c)
            continue

        # testeaza delimitatori si operatori
        found_delim = None
        for delim in sorted(DELIMITERS, key=len, reverse=True):
            if source.startswith(delim, i):
                found_delim = delim
                break

        if found_delim:
            fip_entries.append(FIPEntry("DELIM", found_delim, line, col))
            for c in found_delim:
                i = advance(i, c)
            continue

        # caracterul e invalid
        snippet = source[i:i+10].replace('\n', '\\n')
        raise Exception(
            f"Eroare lexicală la linia {line}, coloana {col}: caracter invalid '{ch}' în contextul \"{snippet}\""
        )

    return fip_entries, TS_ident, TS_const


# --- Main ---
def main():
    if len(sys.argv) < 2:
        print("Usage: python3 tokenizer_af.py <input_file>")
        sys.exit(1)

    inp = sys.argv[1]
    with open(inp, "r", encoding="utf-8") as f:
        src = f.read()

    af_ident, af_int, af_real = load_afs()

    try:
        fip_entries, TS_ident, TS_const = lex(src, af_ident, af_int, af_real)
    except Exception as e:
        print(f"Lexical error: {e}", file=sys.stderr)
        sys.exit(2)

    # Scriere TS și FIP
    write_TS_bst(TS_ident, "output/TS_ident.txt")
    write_TS_bst(TS_const, "output/TS_const.txt")
    write_FIP(fip_entries, "output/FIP.txt")

    print("Analiza lexicala finalizata. FIP si TS generate cu succes!")


if __name__ == "__main__":
    main()
