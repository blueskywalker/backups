#!/usr/bin/env python

import sys
import random
import logging

TEST_DATA=[9,1,3,5,2,8,6,4,7]


def qsort(input):
    if input == []:
        return []
    else:
        return qsort([x for x in input[1:] if x<input[0]]) + \
               [input[0]] +\
               qsort([x for x in input[1:] if x>= input[0]])


def main():
    print qsort(TEST_DATA)


if __name__ == "__main__":
    main()
