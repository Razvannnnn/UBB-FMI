from dataclasses import dataclass

# BST Node pentru identif si constante
class BSTNode:
    def __init__(self, key, type_=None):
        self.key = key
        self.type = type_
        self.left = None
        self.right = None
        self.line_no = None

class BST:
    def __init__(self):
        self.root = None

    def search(self, key):
        cur = self.root
        while cur:
            if key == cur.key:
                return cur
            elif key < cur.key:
                cur = cur.left
            else:
                cur = cur.right
        return None

    def insert(self, key, type_=None):
        if self.root is None:
            self.root = BSTNode(key, type_)
            return self.root
        cur = self.root
        while True:
            if key == cur.key:
                return cur  # deja exista
            elif key < cur.key:
                if cur.left is None:
                    cur.left = BSTNode(key, type_)
                    return cur.left
                cur = cur.left
            else:
                if cur.right is None:
                    cur.right = BSTNode(key, type_)
                    return cur.right
                cur = cur.right

    def inorder(self):
        # ordine lexicografică
        res = []
        def _inorder(node):
            if not node: return
            _inorder(node.left)
            res.append(node)
            _inorder(node.right)
        _inorder(self.root)
        return res

# FIP Entry
@dataclass
class FIPEntry:
    token_class: str
    aux: object
    line: int = 0
    col: int = 0

# scrierea TS si FIP
def write_TS_bst(bst, path):
    nodes = bst.inorder()
    with open(path, "w", encoding="utf-8") as f:
        for idx, node in enumerate(nodes, start=1):
            node.line_no = idx
            if node.type:
                f.write(f"{idx}\t{node.key}\t{node.type}\n")
            else:
                f.write(f"{idx}\t{node.key}\n")

def write_FIP(fip_entries, path):
    with open(path, "w", encoding="utf-8") as f:
        for entry in fip_entries:
            if isinstance(entry.aux, BSTNode):
                line_no = entry.aux.line_no
            else:
                line_no = entry.aux
            f.write(f"{entry.token_class}\t{line_no}\n")
