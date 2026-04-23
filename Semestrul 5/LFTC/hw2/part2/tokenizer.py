from parser import parse_file
from keywords import TableOfSymbols
import re

class Tokenizer:
    def __init__(self, table_of_symbols: TableOfSymbols):
        self._table_of_symbols = table_of_symbols
        self._symbols2code = table_of_symbols.get_symbol_to_code_dict()
        self._sign_dict = table_of_symbols.get_signification_to_symbol_dict()

        # load automata for identifiers and constants
        self.af_identifier = parse_file("data/af_identifier.txt")
        self.af_integer = parse_file("data/af_integer.txt")
        self.af_real = parse_file("data/af_real.txt")

        # collect known separators and operators
        self.delimiters = [
            "==", "!=", "<=", ">=", "&&", "||",  # multi-char first
            "+", "-", "*", "/", "%", "=", "<", ">", "!", ".",
            "(", ")", "{", "}", "[", "]", ";", ","
        ]

        # escape for regex
        escaped = [re.escape(d) for d in self.delimiters]
        # pattern: split on spaces or delimiters, keeping them
        self.token_pattern = re.compile(r"(" + "|".join(escaped) + r"|\s+)")

    def _split_string(self, text: str):
        """Split source code into tokens, preserving delimiters but skipping pure whitespace."""
        parts = self.token_pattern.split(text)
        for part in parts:
            token = part.strip()
            if token:
                yield token

    def extract_atoms(self, text: str):
        atoms = []
        for word in self._split_string(text):
            token_list = self._extract_atoms_from_word(word)
            atoms.extend(token_list)
        return atoms

    def _extract_atoms_from_word(self, word: str):
        # --- Direct matches (keywords, operators, delimiters) ---
        if word in self._symbols2code:
            return [(self._symbols2code[word], None)]

        # --- Identifiers ---
        if self.af_identifier.accepts(word):
            return [(self._symbols2code["ID"], word)]

        # --- Constants (integer or real) ---
        if self.af_integer.accepts(word) or self.af_real.accepts(word):
            return [(self._symbols2code["CONST"], word)]

        # --- If nothing matches ---
        raise ValueError(f"Lexical error: Cannot tokenize '{word}'")
