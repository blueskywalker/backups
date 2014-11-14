#!/usr/bin/env python

alist=["dog","cat","rat","bird","horse"]

def concat(w):
    return "prefix"+w

print(map(concat,alist))
print(map((lambda x:"prefix"+x),alist))

