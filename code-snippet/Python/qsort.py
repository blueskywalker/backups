#!/usr/bin/env python

import sys
import random
import logging

TEST_DATA=[9, 1, 3,5,2,8,6,4,7]

def partition(input):

    last = len(input)-1
    pivot = random.randint(0,last)

    logging.debug( '[%d:%d]:%d' % (0,last,pivot) )

    input[pivot],input[last] = input[last],input[pivot]

    x=0
    y=last-1

    while x <= y :
        while input[x] < input[last]:
            x += 1

        while input[y] > input[last]:
            y -= 1

        if x < y:
            input[x],input[y] = input[y],input[x]


    input[x],input[last] = input[last],input[x]

    return x


def qsort(input):

    if input==[]:
        return []

    pivot = partition(input)

    return qsort(input[:pivot]) + [input[pivot]] + qsort(input[pivot+1:])


def main():
    logging.basicConfig(stream=sys.stderr, level=logging.DEBUG)
    print qsort(TEST_DATA)


if __name__ == "__main__":
    main()

