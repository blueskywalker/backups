#!/usr/bin/env python

import sys

sys.setrecursionlimit(1500)


def square(n):
    return n * n

def powerOf2(n):
    if(n==0): return 1

    if n%2==0:  return square(powerOf2(n//2)) 
    else: return 2 * square(powerOf2(n//2))

def usage(name):
    print "%s n" %(name)

def main(args):
#    print  "args %s %s" % (args[0], args[1])
    if(len(args) < 2):
        usage(args[0])
        sys.exit(0)

    print powerOf2(int(args[1]))

if __name__ == "__main__":
    main(sys.argv)
