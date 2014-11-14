#!/usr/bin/env python

import random

#test_data=[ random.randint(0,100) for i in range(10)]
test_data=[1, 69, 88, 37, 94, 21, 1, 32, 26, 2]
print test_data

def pairSum(data, k):
	if len(data)<2:
		return

	data.sort()

	left, right =  (0,len(data)-1)
	while left<right:
		currentSum = data[left] + data[right]
		if currentSum == k:
			print data[left],data[right]
			left+=1
		elif currentSum<k:
			left+=1
		else:
			right-=1



def pairSumWithExtraSpace(data,k):
	if len(data)<2:
		return

	seen = set()
	output = set()

	for num in data:
		target = k - num
		if target not in seen:
			seen.add(num)
		else:
			output.add((min(num,target),max(num,target)))

	print '\n'.join(map(str,list(output)))


#pairSum(test_data,71)
pairSumWithExtraSpace(test_data,71)