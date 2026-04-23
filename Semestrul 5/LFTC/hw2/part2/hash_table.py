from typing import Generic, TypeVar

T = TypeVar("T")

class HashTable(Generic[T]):
    def __init__(self):
        self.table: dict[T, int] = {}
        self.counter = 0

    def insert(self, symbol: T):
        if symbol not in self.table:
            self.table[symbol] = self.counter
            self.counter += 1

    def get_index(self, symbol: T):
        return self.table.get(symbol)

    def inorder(self):
        return list(self.table.keys())
