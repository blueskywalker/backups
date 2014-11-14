#!/usr/bin/env python

import sys

memo = {0:0,1:1}

def fibo(n):    
    if not n in memo:
        memo[n] = fibo(n-2) + fibo(n-1)
    return memo[n]

def usage(name):
    print "%s n" % name

def main(args):
    if (len(args) < 2) :
        usage(args[0])
        sys.exit()
    
    print fibo(int(args[1]))
    
if __name__ == '__main__':
    main(sys.argv)