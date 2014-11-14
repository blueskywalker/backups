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



test_data='abcd'
size=2

for item in itertools.permutations(test_data,size):
    print item
print
for item in combinations(test_data,size):
    print item
