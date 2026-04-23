class Gramatica:
    def __init__(self, fisier):
        self.reguli = {}
        self.fisier = fisier
        self.EPSILON = ['@', 'eps', 'epsilon']
        self.citeste_gramatica()

    def citeste_gramatica(self):
        try:
            with open(self.fisier, 'r') as f:
                for linie in f:
                    linie = linie.strip()
                    if not linie:
                        continue
                    if "->" in linie:
                        parti = linie.split("->")
                        stanga = parti[0].strip()

                        if len(parti) > 1:
                            dreapta = parti[1].strip()
                        else:
                            dreapta = ""

                        if dreapta in self.EPSILON:
                            dreapta = "epsilon"

                        if stanga not in self.reguli:
                            self.reguli[stanga] = []

                        self.reguli[stanga].append(dreapta)
                    else:
                        print(f"Linia '{linie}' nu respecta formatul")

        except FileNotFoundError:
            print(f"Eroare fisier")

    def afiseaza_reguli(self, neterminal):
        if neterminal in self.reguli:
            print(f"Regulile de productie pt '{neterminal}':")
            for productie in self.reguli[neterminal]:
                print(f"{neterminal} -> {productie}")
        else:
            print(f"Neterminalul '{neterminal}' nu a fost gasit")

    def afiseaza_toate(self, neterminal):
        gasit = False
        print(f"Regulile de productie pt '{neterminal}':")

        for stanga, lista_dreapta in self.reguli.items():
            for dreapta in lista_dreapta:
                if stanga == neterminal or neterminal in dreapta:
                    print(f"{stanga} -> {dreapta}")
                    gasit = True

        if not gasit:
            print(f"Neterminalul '{neterminal}' nu a fost gasit")


if __name__ == "__main__":
    fisier_gramatica = "gramatica.txt"
    g = Gramatica(fisier_gramatica)

    simbol = input("Neterminal: ").strip()
    #g.afiseaza_reguli(simbol)
    g.afiseaza_toate(simbol)