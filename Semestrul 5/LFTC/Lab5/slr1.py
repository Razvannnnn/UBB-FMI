import sys


class SLRParser:
    def __init__(self, grammar_file):
        # Structuri de date
        self.grammar = {}
        self.start_symbol = ""
        self.terminals = set()
        self.non_terminals = set()

        # SLR
        self.first = {}
        self.follow = {}
        self.canonical_collection = []  # lista de stari
        self.goto_transitions = {}
        self.action_table = {}
        self.goto_table = {}
        self.is_slr = True

        self.load_grammar(grammar_file)
        self.compute_first()
        self.compute_follow()
        self.construct_canonical_collection()
        self.create_parsing_table()

    def load_grammar(self, file_path):
        try:
            with open(file_path, 'r') as f:
                lines = f.readlines()
        except FileNotFoundError:
            print("nu exista fisierul")
            sys.exit(1)

        for i, line in enumerate(lines):
            line = line.strip()
            if not line: continue

            # Format: S -> A B
            lhs, rhs = line.split('->') # neterminal, productie
            lhs = lhs.strip()
            rhs_parts = [x.strip() for x in rhs.split('|')]  # Suport pt |

            if i == 0:
                self.start_symbol = lhs

            self.non_terminals.add(lhs)

            if lhs not in self.grammar:
                self.grammar[lhs] = []

            for part in rhs_parts:
                symbols = part.split()
                self.grammar[lhs].append(symbols)
                # identificare terminale
                for sym in symbols:
                    if not sym[0].isupper() and sym != "epsilon":
                        self.terminals.add(sym)

        # Augmentare gramatică (S' -> S)
        self.augmented_start = self.start_symbol + "'"
        self.grammar[self.augmented_start] = [[self.start_symbol]]
        self.non_terminals.add(self.augmented_start)
        self.terminals.add('$')  # End marker

    # 1. CALCUL FIRST
    def compute_first(self):
        for nt in self.non_terminals:
            self.first[nt] = set()

        changed = True
        while changed:
            changed = False
            for lhs in self.grammar:
                for rhs in self.grammar[lhs]:
                    # reguli de calcul first
                    if rhs[0] in self.terminals:
                        if rhs[0] not in self.first[lhs]:
                            self.first[lhs].add(rhs[0])
                            changed = True
                    elif rhs[0] in self.non_terminals:
                        # adauga first de la primul simbol neterminal
                        original_size = len(self.first[lhs])
                        self.first[lhs].update(self.first[rhs[0]])
                        if len(self.first[lhs]) > original_size:
                            changed = True

    # 2. CALCUL FOLLOW
    def compute_follow(self):
        for nt in self.non_terminals:
            self.follow[nt] = set()
        self.follow[self.start_symbol].add('$')  # Regula 1

        changed = True
        while changed:
            changed = False
            for lhs in self.grammar:
                for rhs in self.grammar[lhs]:
                    for i, symbol in enumerate(rhs):
                        if symbol in self.non_terminals:
                            # daca e ultimul simbol sau ce urmeaza e epsilon
                            if i == len(rhs) - 1:
                                if not self.follow[lhs].issubset(self.follow[symbol]):
                                    self.follow[symbol].update(self.follow[lhs])
                                    changed = True
                            else:
                                next_sym = rhs[i + 1]
                                if next_sym in self.terminals:
                                    if next_sym not in self.follow[symbol]:
                                        self.follow[symbol].add(next_sym)
                                        changed = True
                                elif next_sym in self.non_terminals:
                                    if not self.first[next_sym].issubset(self.follow[symbol]):
                                        self.follow[symbol].update(self.first[next_sym])
                                        changed = True

    # 3. LR(0) ITEMS
    # inchiderea unei stari
    def closure(self, items):
        current_items = set(items)
        changed = True
        while changed:
            changed = False
            temp = current_items.copy()
            for lhs, rhs, dot in current_items:
                if dot < len(rhs):  # daca punctul e in fata unui neterminal
                    sym = rhs[dot]
                    if sym in self.non_terminals:
                        for prod in self.grammar[sym]:
                            new_item = (sym, tuple(prod), 0)
                            if new_item not in temp:
                                temp.add(new_item)
                                changed = True
            current_items = temp
        return frozenset(current_items)

    # deplasarea dintr o stare in alta
    def goto(self, items, symbol):
        next_items = set()
        for lhs, rhs, dot in items:
            if dot < len(rhs) and rhs[dot] == symbol:
                next_items.add((lhs, rhs, dot + 1))
        return self.closure(next_items)

    def construct_canonical_collection(self):
        start_prod = self.grammar[self.augmented_start][0]
        initial_item = (self.augmented_start, tuple(start_prod), 0)
        I0 = self.closure({initial_item})

        self.canonical_collection = [I0]

        processed = 0
        while processed < len(self.canonical_collection):
            current_state = self.canonical_collection[processed]

            # simbolurile pe care putem avansa
            symbols = set()
            for lhs, rhs, dot in current_state:
                if dot < len(rhs):
                    symbols.add(rhs[dot])

            for sym in symbols:
                next_state = self.goto(current_state, sym)
                if not next_state: continue

                if next_state not in self.canonical_collection:
                    self.canonical_collection.append(next_state)

                idx_from = processed
                idx_to = self.canonical_collection.index(next_state)
                self.goto_transitions[(idx_from, sym)] = idx_to

            processed += 1

    # 4. TABELA SLR
    def create_parsing_table(self):
        for i, state in enumerate(self.canonical_collection):
            # A. SHIFT si GOTO bazate pe tranzitii
            for (idx, sym), next_idx in self.goto_transitions.items():
                if idx == i:
                    if sym in self.terminals:
                        self.add_action(i, sym, ('shift', next_idx))
                    elif sym in self.non_terminals:
                        self.goto_table[(i, sym)] = next_idx

            # B. REDUCE si ACCEPT
            for lhs, rhs, dot in state:
                if dot == len(rhs):  # punct final -> reducere
                    if lhs == self.augmented_start:
                        self.add_action(i, '$', ('accept',))
                    else:
                        # reducem doar pentru simbolurile din FOLLOW(lhs)
                        if lhs in self.follow:
                            for f_sym in self.follow[lhs]:
                                self.add_action(i, f_sym, ('reduce', lhs, rhs))

    def add_action(self, state, symbol, action):
        key = (state, symbol)
        if key in self.action_table:
            prev_action = self.action_table[key]
            # conflict
            if prev_action != action:
                self.is_slr = False
                print(f"CONFLICT la starea {state}, simbol '{symbol}':")
                print(f"  Existent: {prev_action}")
                print(f"  Nou: {action}")
        else:
            self.action_table[key] = action

    def parse_input(self, input_str):
        if not self.is_slr:
            return "Gramatica nu este SLR(1)"

        tokens = input_str.strip().split() + ['$']
        stack = [0]
        cursor = 0

        print(f"\nAnaliza secventei: '{input_str}'")
        print(f"{'STIVA':<60} {'INTRARE':<60} {'ACTIUNE'}")
        print("-" * 150)

        derivation = []

        while True:
            current_state = stack[-1]
            current_token = tokens[cursor]

            action = self.action_table.get((current_state, current_token))

            stack_str = str(stack)
            input_str_disp = " ".join(tokens[cursor:])

            if action is None:
                print(f"{stack_str:<60} {input_str_disp:<60} EROARE")
                return f"Eroare sintactica la simbolul '{current_token}'"

            if action[0] == 'shift':
                print(f"{stack_str:<60} {input_str_disp:<60} SHIFT {action[1]}")
                stack.append(current_token)  # push simbol
                stack.append(action[1])  # push stare
                cursor += 1

            elif action[0] == 'reduce':
                lhs, rhs = action[1], action[2]
                print(f"{stack_str:<60} {input_str_disp:<60} REDUCE {lhs}->{' '.join(rhs)}")
                derivation.append(f"{lhs} -> {' '.join(rhs)}")

                # Pop 2 * lungime RHS
                for _ in range(2 * len(rhs)):
                    stack.pop()

                top_state = stack[-1]
                next_state = self.goto_table.get((top_state, lhs))

                if next_state is None:
                    return "Eroare interna GOTO"

                stack.append(lhs)
                stack.append(next_state)

            elif action[0] == 'accept':
                print(f"{stack_str:<60} {input_str_disp:<60} ACCEPT")
                return "SUCCES! Secventa este acceptata.\n\nProductii aplicate:\n" + "\n".join(derivation)


if __name__ == "__main__":
    parser = SLRParser("gramatica.txt")

    print("--- Verificare Gramatica ---")
    if parser.is_slr:
        print("Gramatica ESTE de tip SLR(1). Putem continua.")
    else:
        print("Gramatica NU este SLR(1) (vezi conflictele mai sus).")
        sys.exit()

    # valid: id * id + id
    # invalid: id + * id

    secventa = input("\nIntrodu secventa de intrare (simboluri separate prin spatiu):\n> ")
    rezultat = parser.parse_input(secventa)
    print("\nREZULTAT FINAL:")
    print(rezultat)