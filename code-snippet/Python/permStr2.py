#!/usr/bin/eny python


import sys
import os

def swap(c,i,j):
    c = list(c)
    c[i],c[j] = c[j],c[i]
    return ''.join(c)


def perm2(inStr):

    if len(inStr)==1:
        return [inStr]

    ret=list()
    for i in range(len(inStr)):
        s=swap(inStr,0,i)
        ret.extend(map((lambda x:s[:1]+x),perm2(s[1:])))

    return ret


def perm3(inStr):
    if(len(inStr)==1):
        yield inStr
    else:
        for perm in perm3(inStr[1:]):
            for i in xrange(len(perm)+1):
                yield perm[:i] + inStr[0:1] + perm[i:]

def main(args):

    if(len(args) < 2):
        print("need string")
        return

#    print(perm2(args[1]))

    print(list(perm3(args[1])))


if __name__ == '__main__':
    main(sys.argv)
