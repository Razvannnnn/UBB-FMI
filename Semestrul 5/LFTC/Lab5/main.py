import sys
from tokenizer import lex, LexicalError
from slr1 import SLRParser


def map_token_to_terminal(token):
    kind = token.kind
    value = token.value

    keywords_and_symbols = {
        'if', 'else', 'while', 'read_int', 'read_real', 'print',
        'int', 'float', 'struct', 'Point',
        '+', '-', '*', '/', '%', '=',
        '(', ')', '{', '}', ';', ',',
        '==', '<', '>', '<=', '>=', '!='
    }

    if value in keywords_and_symbols:
        return value

    # 2. Maparea categoriilor lexicale
    if kind == 'IDENT':
        return 'id'
    elif kind == 'INT':
        return 'const_int'
    elif kind == 'REAL':
        return 'const_real'

    # ignora comentarii
    return None


def main():
    # 1. verificam argumentele
    if len(sys.argv) < 2:
        sys.exit(1)

    input_file = sys.argv[1]
    grammar_file = "gramatica_mini.txt"

    # 2. analiza lexicala
    print(f"--- 1. Analiza Lexicala a fisierului '{input_file}' ---")
    try:
        with open(input_file, 'r', encoding='utf-8') as f:
            source_code = f.read()

        tokens = lex(source_code)

    except LexicalError as e:
        print(f"EROARE LEXICALA: {e}")
        sys.exit(1)
    except FileNotFoundError:
        print(f"Eroare: Fisierul '{input_file}' nu exista.")
        sys.exit(1)

    # 3. generare fip
    parser_input_list = []
    print("\n--- 2. Conversie Tokeni -> Intrare Parser (FIP) ---")
    for t in tokens:
        terminal = map_token_to_terminal(t)
        if terminal:
            parser_input_list.append(terminal)
        else:
            pass

    parser_input_str = " ".join(parser_input_list)
    print(f"Secventa de terminale: {parser_input_str}\n")

    # 4. analiza sintactica
    print(f"--- 3. Analiza Sintactica (SLR) ---")
    slr = SLRParser(grammar_file)

    if not slr.is_slr:
        print("Verificati fisierul gramatica_mini.txt pentru conflicte.")
        sys.exit(1)
    rezultat = slr.parse_input(parser_input_str)

    print("\nREZULTAT ANALIZA SINTACTICA:")
    print(rezultat)


if __name__ == "__main__":
    main()