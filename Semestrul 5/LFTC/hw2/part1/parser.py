from af import AF

def parse_file(path: str) -> AF:
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
                stuff = line[len('STATES:'):].strip()
                parts = [p.strip() for p in stuff.split(',') if p.strip()]
                states.update(parts)
            elif line.upper().startswith('ALPHABET:'):
                stuff = line[len('ALPHABET:'):].strip()
                parts = [p.strip() for p in stuff.split(',') if p.strip()]
                alphabet.update(parts)
            elif line.upper().startswith('START:'):
                start = line[len('START:'):].strip()
            elif line.upper().startswith('FINAL:'):
                stuff = line[len('FINAL:'):].strip()
                parts = [p.strip() for p in stuff.split(',') if p.strip()]
                finals.update(parts)
            elif line.upper().startswith('TRANSITIONS:'):
                rest = line[len('TRANSITIONS:'):].strip()
                if rest:
                    transitions.append(rest)
            else:
                transitions.append(line)

    af = AF(states=states, alphabet=alphabet, start=start, finals=finals)
    for t in transitions:
        try:
            left, right = t.split('->')
            frm, sym = left.split(':')
            frm = frm.strip()
            sym = sym.strip()
            to = right.strip()
            af.add_transition(frm, sym, to)
        except Exception as e:
            raise ValueError(f"Bad transition line '{t}': {e}")
    return af
