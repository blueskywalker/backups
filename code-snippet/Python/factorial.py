#!/usr/bin/env python

import sys
import os


#
# tail-recursive call
#
def fact(n):
    if(n<2): return 1
    return n * fact(n-1)


def fact_args(n,offset=1):
    if(n==1):
        return offset
    else:
        return fact_args(n-1,n*offset)

def printArgs():
    for arg in sys.argv:
        print arg

def main(args):
    if(len(args)<2):
        print("need number")
        sys.exit(1)

    print(fact_args(int(args[1]),1))

if __name__ == '__main__':
    main(sys.argv)

