from af import AF

def parse_file(path: str) -> AF:
    '''
    Parses a file defining a finite automaton and returns an AF object.
    '''
    states = set()
    alphabet = set()
    start = None
    finals = set()
    transitions = []

    with open(path, 'r', encoding='utf-8') as f:
        for raw in f:
            line = raw.strip()
            if not line or line.startswith('#'):
                continue
            if line.upper().startswith('STATES:'):
                states.update(x.strip() for x in line[len('STATES:'):].split(',') if x.strip())
            elif line.upper().startswith('ALPHABET:'):
                alphabet.update(x.strip() for x in line[len('ALPHABET:'):].split(',') if x.strip())
            elif line.upper().startswith('START:'):
                start = line[len('START:'):].strip()
            elif line.upper().startswith('FINAL:'):
                finals.update(x.strip() for x in line[len('FINAL:'):].split(',') if x.strip())
            else:
                transitions.append(line)

    af = AF(states=states, alphabet=alphabet, start=start, finals=finals)

    for t in transitions:
        if ':' not in t or '->' not in t:
            continue
        left, to = t.split('->')
        frm, sym = left.split(':')
        af.add_transition(frm.strip(), sym.strip(), to.strip())
    return af
