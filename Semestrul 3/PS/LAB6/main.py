from scipy.stats import norm, expon, uniform
from scipy.integrate import quad
from numpy import mean, std, linspace, multiply
from matplotlib.pyplot import show, hist, grid, legend, xticks, plot


def f1():
    data = norm.rvs(loc=165, scale=10, size=5000)
    hist(data, bins=14, density=True, range=(130, 200), label='Frecvente relative')
    x = linspace(130, 200, 1000)
    plot(x, norm.pdf(x, loc=165, scale=10), 'r-', label='Functia de densitate')
    xticks(range(130, 200, 5))
    legend(loc='upper right')
    grid()
    show()

    sum((160<=data)&(data<=170))/5000
    mean((160<=data)&(data<=170))
    norm.cdf(170, loc=165, scale=10) - norm.cdf(160, loc=165, scale=10)

def f2():
    n = 5000
    r = uniform.rvs(size=n)
    data = expon.rvs(loc=0, scale=5, size=n)*(r<0.4) + uniform.rvs(loc=4, scale=2, size=n)*(r>=0.4)
    mean(data)
    data.std()

    mean(data<5)
    expon.cdf(5, loc=0, scale=5)*(r<0.4) + uniform.cdf(5, loc=4, scale=2)*(r>=0.4)

def f3():
    n = 5000
    u = uniform.rvs(loc=-1, scale=4, size=n)
    g = lambda x : expon(-x**2)
    mean(4*g(u))

    quad(g, -1, 3)

if __name__ == '__main__':
    f2()