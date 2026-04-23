from scipy.stats import bernoulli, binom, hypergeom, geom
from matplotlib.pyplot import bar, show, hist, grid, legend, xticks

def ex1():
    p = 0.5
    pasi = 10
    nod = 0
    pozitiiNod = []

    for i in range(pasi):
        pozitiiNod.append(nod)
        print("Pas ", i, "Nod: ", nod)
        nod += (bernoulli.rvs(p) * 2 - 1)

    data = []

    n = 1000

    for i in range(n):
        nod = 0
        for j in range(pasi):
            nod += (bernoulli.rvs(p) * 2 - 1)
        data.append(nod)

    left = min(data)
    right = max(data)

    bin_edges = [k + 0.5 for k in range(left - 1, right + 1)]
    hist(data, bin_edges, density=True, rwidth=1, color='blue', edgecolor='black')
    show()


if __name__ == '__main__':
    ex1()