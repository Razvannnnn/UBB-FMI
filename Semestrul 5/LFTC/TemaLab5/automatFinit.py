class AF:
    def __init__(self, Q=None, Sigma=None, q0=None, F=None, delta=None):
        self.Q = Q if Q is not None else set()
        self.Sigma = Sigma if Sigma is not None else set()
        self.q0 = q0
        self.F = F if F is not None else set()
        self.delta = delta if delta is not None else {}

    def is_afd(self):
        for q_sursa, simboluri in self.delta.items():
            for simbol, Q_dest in simboluri.items():
                if len(Q_dest) > 1:
                    return False
        return True

    def read_keyboard(self):
        print("--- Citire AF de la Tastatura ---")

        self.Q = set(input("Starile (separate prin virgula): ").strip().split(','))
        self.Sigma = set(input("Alfabetul (separate prin virgula): ").strip().split(','))
        self.q0 = input("Starea initiala: ").strip()

        if self.q0 not in self.Q:
            print(f"Eroare: Starea initiala '{self.q0}' nu este in Q.")
            return

        self.F = set(input("Starile finale (separate prin virgula): ").strip().split(','))
        if not self.F.issubset(self.Q):
            print("Eroare: Unele stari finale nu sunt in Q.")
            return

        self.delta = {}
        print("Tranzitiile (format: q_sursa,a->q_dest1,q_dest2) sau 'stop':")

        while True:
            line = input("> ").strip()
            if line.lower() == 'stop':
                break

            try:
                sursa_simbol, destinatii_str = line.split('->')
                q_sursa, simbol = sursa_simbol.split(',')
                q_sursa = q_sursa.strip()
                simbol = simbol.strip()
                Q_dest = set(q.strip() for q in destinatii_str.split(','))

                if q_sursa not in self.delta:
                    self.delta[q_sursa] = {}

                if simbol in self.delta[q_sursa]:
                    self.delta[q_sursa][simbol].update(Q_dest)
                else:
                    self.delta[q_sursa][simbol] = Q_dest

            except ValueError:
                print(f"Format incorect: {line}. Incercati din nou sau tastati 'stop'.")
            except Exception as e:
                print(f"Eroare: {e}")

    def read_file(self, filename):
        try:
            with open(filename, 'r') as f:
                lines = [line.strip() for line in f if line.strip()]
        except FileNotFoundError:
            print(f"Eroare: Fisierul '{filename}' nu a fost gasit.")
            return

        section = None
        self.delta = {}

        for line in lines:
            if line.startswith("Stari:"):
                self.Q = set(line.split(':')[1].strip().split(','))
            elif line.startswith("Alfabet:"):
                self.Sigma = set(line.split(':')[1].strip().split(','))
            elif line.startswith("Stare_Initiala:"):
                self.q0 = line.split(':')[1].strip()
            elif line.startswith("Stari_Finale:"):
                self.F = set(line.split(':')[1].strip().split(','))
            elif line.startswith("Tranzitii:"):
                section = "Tranzitii"
            elif section == "Tranzitii":
                try:
                    sursa_simbol, destinatii_str = line.split('->')
                    q_sursa, simbol = sursa_simbol.split(',')
                    q_sursa = q_sursa.strip()
                    simbol = simbol.strip()
                    Q_dest = set(q.strip() for q in destinatii_str.split(','))

                    if q_sursa not in self.delta:
                        self.delta[q_sursa] = {}

                    if simbol in self.delta[q_sursa]:
                        self.delta[q_sursa][simbol].update(Q_dest)
                    else:
                        self.delta[q_sursa][simbol] = Q_dest
                except ValueError:
                    print(f"Avertisment: Linia '{line}' ignorata (format incorect).")

        if self.q0 and self.q0 not in self.Q:
            print(f"Eroare: Starea initiala '{self.q0}' nu este in Q.")
            self.q0 = None
        if not self.F.issubset(self.Q):
            print("Avertisment: Starile finale au fost filtrate la cele din Q.")
            self.F = self.F.intersection(self.Q)

        print(f"AF citit cu succes. Tip: {'AFD' if self.is_afd() else 'AFN'}.")

    def show_elements(self):
        print("\n--- Elemente AF ---")
        print(f"Tip: {'AFD' if self.is_afd() else 'AFN'}")
        print(f"1. Starile (Q): {self.Q}")
        print(f"2. Alfabetul (Sigma): {self.Sigma}")

        print("3. Tranzitiile (delta):")
        for q_sursa, simboluri in sorted(self.delta.items()):
            for simbol, Q_dest in sorted(simboluri.items()):
                Q_dest_str = ",".join(sorted(list(Q_dest)))
                print(f"    ({q_sursa}, {simbol}) -> {{{Q_dest_str}}}")

        print(f"   Starea Initiala (q0): {self.q0}")
        print(f"4. Starile Finale (F): {self.F}")

    def _afd_transition(self, q_current, simbol):
        if q_current in self.delta and simbol in self.delta[q_current]:
            return next(iter(self.delta[q_current][simbol]), None)
        return None

    def check_sequence(self, sequence):
        if not self.is_afd():
            print("Eroare: Operatie doar pentru AFD.")
            return False

        if self.q0 is None: return False

        q_current = self.q0

        for simbol in sequence:
            if simbol not in self.Sigma:
                print(f"Simbolul '{simbol}' nu este in Sigma. Respins.")
                return False

            q_next = self._afd_transition(q_current, simbol)

            if q_next is None:
                print(f"Blocaj la '{simbol}' din '{q_current}'. Respins.")
                return False

            q_current = q_next

        accepted = q_current in self.F
        print(f"Finalizat in starea '{q_current}'. {'ACCEPTAT' if accepted else 'RESPINS'}.")
        return accepted

    def longest_accepted_prefix(self, sequence):
        if not self.is_afd():
            print("Eroare: Operatie doar pentru AFD.")
            return None

        if self.q0 is None: return None

        q_current = self.q0
        prefix_accepted = None

        # Stringul vid
        if q_current in self.F:
            prefix_accepted = ""

        for i, simbol in enumerate(sequence):
            if simbol not in self.Sigma:
                break

            q_next = self._afd_transition(q_current, simbol)

            if q_next is None:
                break

            q_current = q_next

            if q_current in self.F:
                prefix_accepted = sequence[:i + 1]

        return prefix_accepted