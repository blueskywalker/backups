#!/usr/bin/env python

import random
import collections

class Node:
    def __init__(self,cargo=None,left=None,right=None):
        self.cargo = cargo
        self.left = left
        self.right = right

    def __str__(self):
        return str(self.cargo)


def insert_node(head,node):
    if head is None:
       return node
    else:
        if node.cargo < head.cargo:
            head.left=insert_node(head.left,node)
        else:
            head.right=insert_node(head.right,node)

    return head


def print_inorder(node):
    if node is not None:
        print_inorder(node.left)
        print node
        print_inorder(node.right)


def print_tree(node):
    queue = collections.deque()
    if node is not None:
        queue.appendleft(node)
        queue.appendleft('\n')

    while len(queue) != 0:
        node = queue.pop()
        if node is None:
            print 'N',
            continue
        else:
            print node,

        if node == '\n':
            if len(queue)!=0: queue.appendleft('\n')
            continue

        queue.appendleft(node.left)
        queue.appendleft(node.right)

    print


def max_length(tree):
    if tree is None:
       return 0

    return max(max_length(tree.left),max_length(tree.right)) + 1

def print_align(left,value):
    print ' '*left,value,


def pretty_print(node):
    if node is None:
        return
    queue = collections.deque()
    level=1
    length = max_length(node)
    n = 2**(length+1) - 1


    d = 2**level
    queue.appendleft((n/d,node))
    queue.appendleft((None,'\n'))
    level+=1

    flat = [ ' ' for i in range(n) ]
    while len(queue) != 0:
        loc,node = queue.pop()
        if node is None:
            continue
        elif node == '\n':
            print ''.join(map(str,flat))
            flat = [ ' ' for i in range(n) ]
            if len(queue)!=0: queue.appendleft((None,'\n'))
            level+=1
            continue
        else:
            flat[loc]=node

        d = 2**level
        queue.appendleft((loc-n/d,node.left))
        queue.appendleft((loc+1+n/d,node.right))


    print ''.join(flat)

def copy_tree(node):
    newNode=None
    if node is not None:
        newNode=Node(node.cargo)
        newNode.left  = copy_tree(node.left)
        newNode.right = copy_tree(node.right)

    return newNode


def copy_mirror_tree(node):
    newNode=None
    if node is not None:
        newNode=Node(node.cargo)
        newNode.right = copy_tree(node.left)
        newNode.left = copy_tree(node.right)

    return newNode


def is_mirror_tree(tree1,tree2):

    if tree1 is None and tree2 is None:
        return True
    elif tree1 is None or tree2 is None:
        return False

    if tree1.cargo != tree2:
        return False

    if not is_mirror_tree(tree1.left,tree2.right):
        return False

    if not is_mirror_tree(tree.right,tree.left):
        return False

    return True


def find_common_lowest_ancestor(node, p, q):
    if node == None: return None
    if node == p or node == q:
        return node

    l=find_common_lowest_ancestor(node.left,p,q)
    r=find_common_lowest_ancestor(node.right,p,q)

    if l is not None and r is not None:
        return node

    if l is not None:
        return l

    if r is not None:
        return r


backtrace={}

def build_path(node,prev):
    global backtrace

    if node is None:
        return

    build_path(node.left,node)
    backtrace[node]=prev
    build_path(node.right,node)







if __name__ == "__main__":
    test_ints = [4, 5, 8, 1, 7, 3, 6, 9, 0, 2]

#    nodes=[ Node(i) for i in range(10)]
#    random.shuffle(nodes)

    tree1=None

    p=q=None

    for val in test_ints:
        node=Node(val)
        if val == 6: p = node
        if val == 9: q = node
        tree1=insert_node(tree1,node)

    print_tree(tree1)
#    print_inorder(tree1)

#    tree2=copy_mirror_tree(tree1)
#    print_tree(tree2)

#    print is_mirror_tree(tree1,tree2)
#    pretty_print(tree1)

    print find_common_lowest_ancestor(tree1,p,q)
