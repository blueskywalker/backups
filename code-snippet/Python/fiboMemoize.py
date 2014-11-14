#!/usr/bin/env python

import sys

##
# Decorator
#

def memoize(f):
    cache={}
    def decorated_fucntion(*args):
        if args in cache:
            return cache[args]
        else :
            cache[args]=f(*args)
            return cache[args]
        
    return decorated_fucntion

def memoization(f):
    cache={}
    return lambda *args:cache[args] if args in cache else cache.update({args: f(*args)}) or cache[args]

@memoization
def fibo(n):    
    return n if n < 2 else fibo(n-2) + fibo(n-1)

def usage(name):
    print "%s n" % name

def main(args):
    if (len(args) < 2) :
        usage(args[0])
        sys.exit()

    sys.setrecursionlimit(2000)
    
    print fibo(int(args[1]))
    
if __name__ == '__main__':
    main(sys.argv)