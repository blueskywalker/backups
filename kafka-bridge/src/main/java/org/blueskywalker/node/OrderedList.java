package org.blueskywalker.node;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by kkim on 2/27/16.
 */
public class OrderedList {

    public static class Node implements Comparable<Node> {
        int value;
        Node greater;
        Node next;

        public Node(int value) {
            this.value = value;
        }

        @Override
        public int compareTo(Node other) {
            return value - other.value;
        }

        @Override
        public String toString() {
            return "(" + value + ")";
        }
    }

    Node head;
    Node smallest;

    public Node getHead() {
        return head;
    }

    public Node getSmallest() {
        return smallest;
    }

    public void add(Node node) {
        if (node==null)
            return;

        if (head==null) {
            head=node;
            return;
        }

        Node rest = head;
        head = node;
        node.next = rest;
    }

    public void print(Node node) {
        if(node == null)
            return;

        System.out.print(node);
        print(node.next);
    }

    public void printWithGreater(Node node) {
        if(node==null)
            return;

        System.out.print(node);
        printWithGreater(node.greater);
    }

    public Node travel(Node node,Node newNode) {
        if (node ==null || newNode == null)
            return null;

        if (node.greater==null)
            return node;

        if(node.greater.compareTo(newNode)<0)
            return travel(node.greater,newNode);

        return node;
    }

    public void ordered(Node node) {
        if (node==null)
            return;

        if (smallest==null) {
            smallest=node;
        } else {

            if (node.compareTo(smallest) < 0) {
                node.greater = smallest;
                smallest = node;
            } else {
                Node rightBefore = travel(smallest, node);
                node.greater = rightBefore.greater;
                rightBefore.greater = node;
            }
        }
        ordered(node.next);
    }
    public static void main(String[] args) {
        //List<Integer> range = IntStream.range(0, 16).boxed().collect(Collectors.toList());
        //Collections.shuffle(range);
        //System.out.println(Arrays.toString(range.toArray()));
        List<Integer> range = Arrays.asList(14, 0, 3, 11, 12, 6, 4, 15, 5, 10, 1, 9, 8, 2, 7, 13);

        OrderedList ordered = new OrderedList();

        for(Integer n : range) {
            ordered.add(new Node(n));
        }

        ordered.print(ordered.getHead());
        System.out.println();
        ordered.ordered(ordered.getHead());
        ordered.printWithGreater(ordered.getSmallest());
    }
}
