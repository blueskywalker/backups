#!/usr/bin/env python
import random

class Node:
    def __init__(self,cargo=None,next=None):
        self.cargo = cargo
        self.next = next
        self.high = None

    def __str__(self):
        return str(self.cargo)


def push_node(head,node):
    if head is None:
       return node

    if node is not None:
        node.next = head
        head = node

    return head

def pop_node(head):
    if head is not None:
        tmp = head
        head = tmp.next
        tmp.next = None
        return tmp
    return head

def print_node(node):
    while node is not None:
        print node,node.high
        node = node.next


def populate_high(node):
    min_node=None

    while node is not None:
        if min_node is None:
            min_node = node
            node = node.next
            continue

        if node.cargo < min_node.cargo:
            node.high = min_node
            min_node = node
        else:
            less_node = min_node
            while less_node.high is not None and less_node.high.cargo < node.cargo:
                less_node = less_node.high

            node.high=less_node.high
            less_node.high = node

        node = node.next



if __name__ == "__main__":
    test_ints = [4, 5, 8, 1, 7, 3, 6, 9, 0, 2]

#    nodes=[ Node(i) for i in range(10)]
#    random.shuffle(nodes)

    head=None

    for val in test_ints:
        head=push_node(head,Node(val))


    populate_high(head)
    print_node(head)
