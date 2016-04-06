package org.blueskywalker.node;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by kkim on 2/26/16.
 */
public class BinaryTreeWithNext {

    public static class Node  implements Comparable<Node> {
        int value;
        Node left;
        Node right;
        Node next;

        public Node(int value) {
            this.value = value;
        }

        public int compareTo(Node other) {
            return value-other.value;
        }

        public void addChild(Node node) {
            if (node != null) {
                if(compareTo(node)>0) {
                    if (left==null)
                        left = node;
                    else
                        left.addChild(node);
                } else {
                    if (right==null)
                        right = node;
                    else
                        right.addChild(node);
                }
            }
        }

        @Override
        public String toString() {
            return "(" + value + ")";
        }
    }

    Node root;

    // build binary search tree
    public void build(Node node) {
        if (root == null)
            root = node;
        else
            root.addChild(node);
    }

    public void build(List<Integer> list, int start, int end) {
        if(start<end) {
            int mid =(start + end) / 2;
            build(new Node(list.get(mid)));
            build(list,start,mid);
            build(list,mid+1,end);
        }
    }

    public void inorderPrint(Node node) {
        if(node == null)
            return;

        inorderPrint(node.left);
        System.out.println(node.value);
        inorderPrint(node.right);
    }


    public Node leftMost(Node node) {
        if (node==null)
            return null;

        if (node.left!=null)
            return node.left;

        if (node.right!=null)
            return node.right;

        return leftMost(node.next);
    }

    public void buildNext(Queue<Node> queue,Node node) {

        if(node==null) {
            return;
        }

        queue.add(node);

        while(true) {
            Node current = queue.poll();

            if (current==null)
                break;

            if (current.left != null) {
                if (current.right != null) {
                    current.left.next = current.right;
                } else {
                    current.left.next = leftMost(current.next);
                }
                queue.add(current.left);
            }

            if (current.right != null) {
                current.right.next = leftMost(current.next);
                queue.add(current.right);
            }
        }
    }

    public void hierarchyPrint(Queue<Node> queue, Node node) {
        if(node==null)
            return;

        queue.add(node);
        queue.add(new Node(-1));

        while(true) {
            Node current = queue.poll();
            if(current==null)
                break;

            if(current.value==-1) {
                System.out.println();
                if(queue.size()>0) {
                    queue.add(new Node(-1));
                }
            } else
                System.out.print(current);

            if(current.left !=null) queue.add(current.left);
            if(current.right!=null) queue.add(current.right);
        }
    }

    public void printUsingNext(Node node) {
        if(node==null)
            return;

        System.out.print(node);
        Node next = node.next;
        while(next!=null) {
            System.out.print(next);
            next=next.next;
        }
        System.out.println();

        printUsingNext(leftMost(node));
    }

    public Node getRoot() {
        return root;
    }

    public static void main(String[] args) {
        List<Integer> range = IntStream.range(0, 31).boxed().collect(Collectors.toList());

        //Collections.shuffle(range);
        //System.out.println(Arrays.toString(range.toArray()));
        //List<Integer> range = Arrays.asList(30, 3, 20, 25, 5, 21, 24, 22, 26, 16, 4, 7, 2, 27, 23, 14, 1, 10, 18, 17, 0, 9, 6, 28, 19, 13, 15, 29, 12, 11, 8);

        BinaryTreeWithNext btn = new BinaryTreeWithNext();
        btn.build(range, 0, 31);

        //btn.inorderPrint(btn.getRoot());
        Queue<Node> queue = new ArrayBlockingQueue<Node>(100);
        //btn.hierarchyPrint(queue,btn.getRoot());
        //queue.clear();
        btn.buildNext(queue,btn.getRoot());
        btn.printUsingNext(btn.getRoot());
    }
}
