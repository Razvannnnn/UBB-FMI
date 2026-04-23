from parser import parse_file
from af import AF

def load_from_keyboard() -> AF:
    print('Enter the states (e.g., q0,q1,q2)')
    states = input('STATES: ').strip()
    print('Enter the alphabet (e.g., 0,1,a,b,+,-,x,X)')
    alphabet = input('ALPHABET: ').strip()
    start = input('START state: ').strip()
    finals = input('FINAL states: ').strip()
    print('Enter transitions (one per line, e.g., q0:0->q1). Empty line to finish.')
    transitions = []
    while True:
        line = input().strip()
        if not line:
            break
        transitions.append(line)

    af = AF()
    for s in [p.strip() for p in states.split(',') if p.strip()]:
        af.add_state(s)
    for a in [p.strip() for p in alphabet.split(',') if p.strip()]:
        af.add_symbol(a)
    af.start = start if start else None
    for f in [p.strip() for p in finals.split(',') if p.strip()]:
        af.finals.add(f)
    for t in transitions:
        left, right = t.split('->')
        frm, sym = left.split(':')
        af.add_transition(frm.strip(), sym.strip(), right.strip())
    return af

def display_menu():
    print('\n--- Finite Automaton Menu ---')
    print('1. Load from file')
    print('2. Load from keyboard')
    print('3. Display automaton')
    print('4. Check if sequence is accepted (DFA only)')
    print('5. Find longest accepted prefix (DFA only)')
    print('6. Exit')

def main():
    current_af = None
    while True:
        display_menu()
        opt = input('Choose option: ').strip()
        if opt == '1':
            path = input('Enter file path: ').strip()
            try:
                current_af = parse_file(path)
                print('Automaton loaded successfully.')
            except Exception as e:
                print('Error while parsing file:', e)
        elif opt == '2':
            try:
                current_af = load_from_keyboard()
                print('Automaton created successfully.')
            except Exception as e:
                print('Error while creating automaton:', e)
        elif opt == '3':
            print(current_af if current_af else 'No automaton loaded.')
        elif opt == '4':
            if not current_af:
                print('No automaton loaded.')
                continue
            if not current_af.is_deterministic():
                print('The automaton is not deterministic (NFA).')
                continue
            seq = input('Enter input sequence: ').strip()
            print('ACCEPTED' if current_af.accepts(seq) else 'REJECTED')
        elif opt == '5':
            if not current_af:
                print('No automaton loaded.')
                continue
            if not current_af.is_deterministic():
                print('The automaton is not deterministic (NFA).')
                continue
            seq = input('Enter input sequence: ').strip()
            pref = current_af.longest_accepted_prefix(seq)
            print(f'Longest accepted prefix: \"{pref}\"' if pref else 'No accepted prefix found.')
        elif opt == '6':
            print('Goodbye!')
            break
        else:
            print('Invalid option. Please try again.')

if __name__ == '__main__':
    main()
