import sys
import os
from tokenizer import Tokenizer
from keywords import TableOfSymbols
from fip_table import FipTable

def main():
    if len(sys.argv) != 2:
        print("Usage: python par2/main.py <source_file>")
        sys.exit(1)

    src = sys.argv[1]
    if not os.path.exists(src):
        print(f"File {src} not found")
        sys.exit(1)

    # Initialize tokenizer with the table of symbols
    table = TableOfSymbols("data/ts.csv")
    tokenizer = Tokenizer(table)

    with open(src, "r", encoding="utf-8") as f:
        code = f.read()

    try:
        tokens = tokenizer.extract_atoms(code)
    except ValueError as e:
        print(f"Lexical analysis failed:\n{e}")
        sys.exit(1)

    fip_table = FipTable(tokens)
    base_output_dir = os.path.join("part2", "outputs")

    fip_path = os.path.join(base_output_dir, "fip.txt")
    with open(fip_path, "w", encoding="utf-8") as f:
        for atom_code, ts_code in fip_table._table:
            f.write(f"{atom_code:>3}\t{ts_code if ts_code is not None else '-'}\n")
    print(f"FIP table written to {fip_path}")

    ts_path = os.path.join(base_output_dir, "ts.txt")
    with open(ts_path, "w", encoding="utf-8") as f:
        for word, index in fip_table._ts_table._word2index.items():
            f.write(f"{index:>3}\t{word}\n")
    print(f"TS table written to {ts_path}")

if __name__ == "__main__":
    main()
