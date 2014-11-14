#!/usr/bin/env python
from __future__ import division
from fractions import Fraction

import sys


if len(sys.argv) < 2:
    print "%s number" % (sys.argv[0])
    sys.exit(0)

numerator=1
denominator=int(sys.argv[1])

if denominator == 1:
    print "denominator should be greater than 1"
    sys.exit(0)

numeratorDict={}
output=['0','.']
numeratorDict[numerator]=len(output)


while True:
    if numerator < denominator:
        numerator *= 10

    if numerator < denominator:
        output.append('0')
        continue

    quotient,remainder=divmod(numerator,denominator)

    output.append(quotient)

    if remainder == 0:
        print ''.join(map(str,output))
        break

    if remainder in numeratorDict:
        repeat=numeratorDict[remainder]
        print ''.join(map(str,output[:repeat]) + ['('] + map(str,output[repeat:]) + [')'])
        break

    numerator=remainder
    numeratorDict[numerator]=len(output)


