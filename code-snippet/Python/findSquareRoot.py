#!/usr/bin/env python

def sqrt2(num):
	if num<0:
		raise ValueError

	if num==1:
		return 1

	low=0
	high=1+(num/2)
	while low+1<high:
		mid=low+(high-low)/2
		square = mid*mid
		if square == num:
			return mid
		elif square<num:
			low=mid
		else:
			high=mid

	return low


print sqrt2(17)
print sqrt2(30)
