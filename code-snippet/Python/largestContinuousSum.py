#!/usr/bin/env python

import random

#test_data = [-1,-3,4,-3,7]

#test_data = [ random.randint(-100,100) for i in range(10)]
#test_data=[98, -9, -89, -66, -20, 6, 23, -95, -69, -36]
test_data=[42, -16, 61, 83, -50, 11, -87, -42, 43, -12]

def lcs(data):
	if len(data) == 0:
		return None

	maxSum=currentSum=data[0]

	for num in data[1:]:
		currentSum = max(currentSum+num,num)
		maxSum = max(maxSum,currentSum)

	return maxSum



print lcs(test_data)