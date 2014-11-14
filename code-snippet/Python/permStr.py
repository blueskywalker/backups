#!/usr/bin/env python

""" Test Permustaion String. """

import sys
import os


def all_perms(astr):
    """ generate permutation strings. """
    if(len(astr) <= 1):
        yield astr
    else:
        for perm in all_perms(astr[1:]):
            for i in xrange(len(perm) + 1):
                yield perm[:i] + astr[0:1] + perm[i:]


def main(args):
#    if(len(args) < 2):
#        print("need string")
#        return

#     all_perms("abc")
     print list(all_perms(args[1]))


if __name__ == '__main__':
    main(sys.argv)
