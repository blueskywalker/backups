#!/usr/bin/env python

import random

#test_data= [i for i in range(10)]
#random.shuffle(test_data)

test_data=[8, 0, 4, 3, 9, 7, 1, 6, 5, 2]

def swap(data,i,j):
    data[i],data[j]=data[j],data[i]


def partition(data,start,end):
    endp=end-1
    pivot=random.randint(start,endp)
    swap(data,pivot,endp)
    i=start
    j=endp-1

    while i<=j:
        while data[i]<data[endp]: i+=1
        while data[endp]<data[j]: j-=1
        if i<j:
            swap(data,i,j)

    swap(data,i,endp)
    return i



def findKth(data,start,end,k):
    i=partition(data,start,end)
#    print data
    if i==k:
        return data[i]
    elif i<k:
        return findKth(data,i+1,end,k)
    else:
        return findKth(data,start,i,k)


print findKth(test_data,0,len(test_data),2)
print test_data
