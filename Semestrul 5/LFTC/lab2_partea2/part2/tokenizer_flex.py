from tabela_simboluri import BST, FIPEntry, write_TS_bst, write_FIP
import sys

def main():
    TS_ident = BST()
    TS_const = BST()
    fip = []

    for line in sys.stdin:
        parts = line.strip().split("|")
        if len(parts) != 4:
            continue

        token_class, text, line_no, col_no = parts
        line_no = int(line_no)
        col_no = int(col_no)

        if token_class == "IDENT":
            node = TS_ident.search(text)
            if not node:
                node = TS_ident.insert(text)
            fip.append(FIPEntry("ID", node, line_no, col_no))

        elif token_class in ("INT", "REAL", "STRING"):
            node = TS_const.search(text)
            if not node:
                node = TS_const.insert(text, token_class)
            fip.append(FIPEntry("CONST", node, line_no, col_no))

        elif token_class == "DELIM":
            fip.append(FIPEntry("DELIM", text, line_no, col_no))

        elif token_class == "KEYWORD":
            fip.append(FIPEntry("KEYWORD", text, line_no, col_no))


    write_TS_bst(TS_ident, "TS_ident.txt")
    write_TS_bst(TS_const, "TS_const.txt")
    write_FIP(fip, "FIP.txt")

    print("Analiza lexicala finalizata cu succes!")

if __name__ == "__main__":
    main()
