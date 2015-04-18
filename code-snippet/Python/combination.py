#!/usr/bin/env python
import pprint
import itertools

pp = pprint.PrettyPrinter(indent=4)

def combinations(iterable,r):
    pool = tuple(iterable)
    n = len(pool)

    if r > n:
        return

    indices = list(range(r))
    yield tuple(pool[i] for i in indices)

    while True:
        for i in reversed(range(r)):
            if indices[i] != i + n - r:
                break
            else:
                return

        indices[i] += 1
        for j in range(i+1, r):
            indices[j] = indeces[j-1] + 1

            yield tuple(pool[i] for i in indices)



def comb(input,n):
    ret = [[]]

    for item in input:
        ret += [ [item] + sub for sub in ret ]

    return filter(lambda x: len(x)==n,ret)

def bitcomb(input):
    l = len(input)
    n = 1 << l

    ret=[]
    for i in range(n):
        subset=[]
        for b in range(l):
            if (i & (1<<b)) != 0:
                subset.append(input[b])
        ret.append(subset)

    return ret


test_data='abcd'
size=2

for item in itertools.combinations(test_data,size):
    print item

print

for item in combinations(test_data,size):
    print item

print

for item in comb(test_data,3):
    print item

print

for item in bitcomb(test_data):
    print item
