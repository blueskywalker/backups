#!/usr/bin/env python

from __future__ import division
from random import random

# init means and data to random values
# use real data in your code

means = [random() for i in range(10)]
data = [random() for i in range(1000)]


param = 0.01 # bigger numbers make the means change faster
#must be between 0 and 1

for x in data:
    closest_k = 0
    smallest_error = 9999
    for center,mean in enumerate(means):
        error = abs (x-mean)
        if error < smallest_error:
            smallest_error = error
            closest_k = center
        means[closest_k] = means[closest_k] * (1-param) + x*(param)


print means
