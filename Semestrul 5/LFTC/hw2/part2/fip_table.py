import pandas as pd
from ts_table import TsTable

class FipTable:
    def __init__(self, atom_pairs):
        self._ts_table = TsTable()
        self._table = []

        for atom_pair in atom_pairs:
            if atom_pair[1] is not None:
                self._ts_table += atom_pair[1]

        self._ts_table()

        for atom_pair in atom_pairs:
            if atom_pair[1] is not None:
                self._table.append((atom_pair[0], self._ts_table[atom_pair[1]]))
            else:
                self._table.append((atom_pair[0], None))

    def __str__(self):
        return (
            "FIP TABLE\n"
            + pd.DataFrame(self._table, columns=['Atom code', 'TS code']).to_markdown(index=False)
            + "\nTS TABLE\n"
            + str(self._ts_table)
        )
