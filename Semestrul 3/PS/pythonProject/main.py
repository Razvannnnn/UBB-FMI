
from scipy.stats import uniform
from math import log
from matplotlib.pyplot import bar, show, hist, grid, legend, xticks , yticks

def my_rand_vals(valori, probabilitati, N):
    x=[]
    U = uniform.rvs(size = N)
    for i in range(N):
        pozitie = 0
        sum_prob = probabilitati[0];
        while U[i] > sum_prob:
            pozitie+=1
            sum_prob+=probabilitati[pozitie]
        x.append(valori[pozitie])
    return x, U

def histograma():
    valori, probabilitati = range(4), (0.46, 0.4, 0.10, 0.04)

    data = my_rand_vals(valori, probabilitati, 1000)
    bin_edges = [i+0.5 for i in range(-1, 4)]


def my_exp_rand_vals(alpha, N):
    u = uniform.rvs(size=N)
    X = [-1/alpha * log(1-u[i]) for i in range(N)]
    return X




if __name__ == '__main__':
    x,U = my_rand_vals(range(4), (0.46, 0.4, 0.1, 0.04), 10)
    print(x)
    print(U)