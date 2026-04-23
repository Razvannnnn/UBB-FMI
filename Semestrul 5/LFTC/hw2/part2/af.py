from typing import Dict, Set, Optional

class AF:
    """
    Represents a deterministic or non-deterministic finite automaton.
    """
    def __init__(self, states=None, alphabet=None, start=None, finals=None, transitions=None):
        self.states: Set[str] = set(states or [])
        self.alphabet: Set[str] = set(alphabet or [])
        self.start: Optional[str] = start
        self.finals: Set[str] = set(finals or [])
        self.transitions: Dict[str, Dict[str, Set[str]]] = {}

        if transitions:
            for s, trans in transitions.items():
                for sym, tos in trans.items():
                    for t in tos:
                        self.add_transition(s, sym, t)

    def add_state(self, state: str):
        self.states.add(state)

    def add_symbol(self, symbol: str):
        self.alphabet.add(symbol)

    def add_transition(self, frm: str, symbol: str, to: str):
        if frm not in self.transitions:
            self.transitions[frm] = {}
        if symbol not in self.transitions[frm]:
            self.transitions[frm][symbol] = set()
        self.transitions[frm][symbol].add(to)
        self.states.update([frm, to])
        self.alphabet.add(symbol)

    def is_deterministic(self) -> bool:
        for s in self.states:
            if s in self.transitions:
                for sym, targets in self.transitions[s].items():
                    if len(targets) > 1:
                        return False
        return True

    def accepts(self, sequence: str) -> bool:
        if not self.is_deterministic():
            raise RuntimeError("Accept check only works for DFA")
        cur = self.start
        for ch in sequence:
            if cur not in self.transitions or ch not in self.transitions[cur]:
                return False
            cur = next(iter(self.transitions[cur][ch]))
        return cur in self.finals

    def longest_accepted_prefix(self, sequence: str) -> str:
        cur = self.start
        last_accept = -1
        for i, ch in enumerate(sequence):
            if cur not in self.transitions or ch not in self.transitions[cur]:
                break
            cur = next(iter(self.transitions[cur][ch]))
            if cur in self.finals:
                last_accept = i
        return sequence[:last_accept + 1] if last_accept >= 0 else ''
