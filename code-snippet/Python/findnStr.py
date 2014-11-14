#!/usr/bin/env python

def find(head,rest):
	
	if rest == "":
		return True

	if len(head)>len(rest):
		return False

	if head == rest[:len(head)]:
		return find(head,rest[len(head):])

	return False

def nStr(data):
	half = len(data)/2

	for i in range(half):
		if find(data[:i+1],data[i+1:]):
			return True

	return False


print nStr("aaa")
print nStr("aabr")
print nStr("abcabc")
