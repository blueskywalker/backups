#!/usr/bin/env python


import sys
from sets import Set

def decomposite(astr):
    for i in range(1,len(astr)):
        yield (astr[:i],astr[i:])
        for pswords in decomposite(astr[i:]):
            yield (astr[:i],) + pswords


def count_iterable(i):
    return sum( 1 for e in i )


def count(word):
    return count_iterable(decomposite(word))


def test():
    word="dogcatratbird"

    for atuple in decomposite(word):
        print atuple


def main():
    word="abcde"

    for sub in decomposite(word):
        print sub

    print count(word)

if __name__ == '__main__':
    main()

