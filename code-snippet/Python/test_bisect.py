#!/usr/bin/env python

grades = "FEDCBA"
breakpoints = [30, 44, 66, 75, 85]

from bisect import bisect


def grade(total):
    return grades[bisect(breakpoints,total)]

print grade(66)

print map(grade,[33, 99, 77, 44, 12, 88])
