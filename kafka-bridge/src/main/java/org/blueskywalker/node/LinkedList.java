package org.blueskywalker.node;

import sun.awt.image.ImageWatched;

/**
 * Created by kkim on 2/26/16.
 */
public class LinkedList {


    public static class Node {
        Object value;
        Node next;

        public Node(Object value) {
            this.value = value;
            this.next = null;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

    }

    Node root;

    public Node getRoot() {
        return root;
    }


    public LinkedList add(Node node) {
        if (node==null)
            return this;

        Node rest = root;
        node.next = rest;
        root = node;

        return this;
    }

    public Node nextMost(Node node) {
        if (node==null)
            return null;

        if (node.next!=null) {
            node = nextMost(node.next);
        }

        return node;
    }

    public Node copy(Node node) {
        if(node==null)
            return null;

        Node newNode = new Node(node.value);
        newNode.setNext(copy(node.next));

        return newNode;
    }

    public LinkedList addTail(Node node) {
        if(node!=null)
            return this;

        if(root==null) {
            root=node;
            return this;
        }

        nextMost(root).next = node;

        return this;
    }

    public Node pop() {
        if(root==null)
            return null;

        Node ret = root;
        root = root.next;
        return ret;
    }



    public static Node merge(Node x, Node y) {
        if(x==null && y==null)
            return null;

        if(x==null)
            return y;
        if(y==null)
            return x;

        Node xnext = x.next;
        x.next=y;
        y.next= merge(xnext,y.next);
        return x;
    }

    public void reverse() {
        LinkedList rev = new LinkedList();

        while(root!=null) {
            rev.add(pop());
        }

        root=rev.getRoot();
    }


    public void merge(LinkedList other) {
        root = merge(root,other.root);
    }

    public static void print(Node node) {
        if(node!=null) {
            System.out.print(node.value.toString());
            if(node.next!=null) {
                System.out.print(",");
                print(node.next);
            }
        }
    }

    public void print() {
        print(root);
    }


    public static void main(String[] args) {

        LinkedList a = new LinkedList().add(new Node(4)).add(new Node(3)).add(new Node(2)).add(new Node(1));
        LinkedList b = new LinkedList().add(new Node(9)).add(new Node(8)).add(new Node(7)).add(new Node(6)).add(new Node(5));

        a.merge(b);
        a.print();

    }
}
