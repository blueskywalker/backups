#!/usr/bin/env python
import sys
import pprint

pp = pprint.PrettyPrinter(indent=4)

def perm(given):
    if len(given) == 0:
        return [[]]
    else:
        return [ [x] + ys for x in given for ys in perm(delete(given,x)) ]

def delete(given,item):
    lc = given[:]
    lc.remove(item)
    return lc

def main():
    pp.pprint(perm(list("abc")))

if __name__ == "__main__":
    main()
