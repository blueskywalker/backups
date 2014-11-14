#!/usr/bin/env python

import sys


def fibo(n):    
    return  n if n <2 else fibo(n-2)+fibo(n-1)

def usage(name):
    print "%s n" % name

def main(args):
    if (len(args) < 2) :
        usage(args[0])
        sys.exit()
    
    print fibo(int(args[1]))
    
if __name__ == '__main__':
    main(sys.argv)