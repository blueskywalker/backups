#!/usr/bin/env python

import random

#test_data= [i for i in range(20)]
#random.shuffle(test_data)

test_data=[8, 0, 4, 3, 9, 7, 1, 10, 6, 5, 2]

def swap(data,i,j):
	data[i],data[j] = data[j],data[i]

def partition(data,start,end,pivot):
	swapIndex=start
	for i in range(start,end):
		if data[i] < pivot:
			swap(data,i,swapIndex)
			swapIndex+=1

	return swapIndex


def findKth(data,start,end,k):
	length = end - start

	if length<=5:
		return sorted(data[start:end])[k]

	numMedians=length/5
	medians=[findKth(data,start+5*i,start+5*(i+1),2) for i in range(numMedians)]
	pivot=findKth(medians,0,len(medians),len(medians)/2)
	
	i=partition(data,start,end,pivot)

	rank=i-start
	if k<rank:
		return findKth(data,start,i,k)
	else:
		return findKth(data,i,end,k-rank)




print test_data
print findKth(test_data,0,len(test_data),10)
print test_data