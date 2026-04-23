import pandas as pd
from hash_table import HashTable

class TsTable:
    def __init__(self):
        self._table = HashTable[str]()
        self._word2index = {}

    def __add__(self, word: str):
        self._table.insert(word)
        return self

    def __contains__(self, word: str):
        return word in self._table.table

    def __call__(self):
        self._word2index = {word: i for i, word in enumerate(self._table.inorder())}

    def __getitem__(self, word: str):
        return self._word2index[word]

    def __str__(self):
        return (
            pd.DataFrame.from_dict(self._word2index, orient='index', columns=['TS Code'])
            .to_markdown()
        )
