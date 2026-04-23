import sys
from automatFinit import AF

def main_menu():
    """Meniul principal al programului."""
    af = AF()

    while True:
        print("1. Citeste AF de la tastatura")
        print("2. Citeste AF dintr-un fisier")

        if af.Q:
            print("3. Afiseaza elemente AF")
            if af.is_afd():
                print("4. Verifica secventa (AFD)")
                print("5. Cel mai lung prefix acceptat (AFD)")
            else:
                print("   (AFN - Optiunile 4 si 5 nu sunt disponibile)")

        print("0. Iesire")

        opt = input("Alegeti o optiune: ").strip()

        if opt == '0':
            sys.exit()

        elif opt == '1':
            af = AF()
            af.read_keyboard()

        elif opt == '2':
            af = AF()
            filename = input("Nume fisier (ex: constante_intregi.txt): ").strip()
            if filename:
                af.read_file(filename)

        elif af.Q:

            if opt == '3':
                af.show_elements()

            elif opt == '4' and af.is_afd():
                seq = input("Secventa de verificat: ").strip()
                af.check_sequence(seq)

            elif opt == '5' and af.is_afd():
                seq = input("Secventa (pentru prefix): ").strip()
                prefix = af.longest_accepted_prefix(seq)
                if prefix is not None:
                    print(f"Cel mai lung prefix acceptat: '{prefix}'")
                else:
                    print("Niciun prefix acceptat.")

            else:
                print("Optiune invalida sau indisponibila pentru AFN.")

        else:
            print("Incarcati intai 1 sau 2.")

if __name__ == "__main__":
    main_menu()