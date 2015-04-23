__author__ = 'kkim'

from itertools import product

def perm(iterable, r=None):
    pool = tuple(iterable)
    n = len(pool)
    r = n if r is None else r

    for indics in product(range(n),repeat=r):
        if len(set(indics)) == r:
            yield tuple(pool[i] for i in indics)




inputs = [1,2,3,4]
r=3

for i in perm(inputs,r):
    print i


