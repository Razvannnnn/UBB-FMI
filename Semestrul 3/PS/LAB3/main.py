import random
from random import choice, sample, randrange
from math import comb, perm
from matplotlib.pyplot import bar, hist, grid, show, legend
from scipy.stats import binom

#help(sample)
#help(comb)
#help(hist)
#help(binom.rvs)

def prob_a(bile, counts, simulari):
    count_a_si_b = 0
    count_a = 0
    for i in range(simulari):
        bile = sample(["rosu", "albastru", "verde"], counts=[5,3,2], k=3)
        if bile[0] == "rosu" or bile[1] == "rosu" or bile[2] == "rosu":
            uniq = set(bile)
            count_a+=1
            if len(uniq) == 1:
                count_a_si_b+=1

    prob = count_a_si_b/count_a
    print(prob)


def prob_b():
    probab_a = comb(5,3)/comb(10,3)
    probab_a_b = comb(10,3)
    probab_a_sau_b = probab_a_b/probab_a
    print(probab_a_sau_b)



def prob_2():
    data = [randrange(1,7) for _ in range(15)]
    bin_edges = [k+0.5 for k in range(7)]
    hist(data, bin_edges, density=True, rwidth=0.9, color='green', edgecolor='black', alpha=0.5, label='frecvente relative')
    distribution = dict([(i, 1/6) for i in range(1,7)])
    bar(distribution.keys(), distribution.values(), width=0.85, color='red', edgecolor='black', alpha=0.6, label='probabilitati')
    legend(loc = 'lower left')
    grid()
    show()

def prob_3():
    n = 5
    p = 0.6
    x = binom.rvs(n, p, size=1000)


    data = x
    bin_edges = [k+0.5 for k in range(6)]
    hist(data, bin_edges, density=True, rwidth=0.9, color='green', edgecolor='black', alpha=0.5, label='frecvente relative')
    distribution = dict([(i, binom.pmf(i,n,p)) for i in range(0,6)])
    bar(distribution.keys(), distribution.values(), width=0.85, color='red', edgecolor='black', alpha=0.6, label='probabilitati')
    legend(loc = 'lower left')
    grid()
    show()

    print(binom.cdf(5, n, p)-binom.cdf(2, n, p))













if __name__ == '__main__':
    bile = ["rosu", "albastru", "verde"]
    counts = [5, 3, 2]
    simulari = 5000
    #prob_b()
    #prob_a(bile, counts, simulari)
    #prob_2()
    prob_3()


