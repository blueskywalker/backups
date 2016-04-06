package org.blueskywalker.node;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by kkim on 2/29/16.
 */
public class BinaryTree {

    public static class Node implements Comparable<Node> {
        int value;
        Node left;
        Node right;

        public Node(int value) {
            this.value = value;
        }

        @Override
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

        public Node copy() {
            Node ret = new Node(value);

            if(left!=null)
                ret.left=left.copy();

            if(right!=null)
                ret.right=right.copy();

            return ret;
        }

        @Override
        public String toString() {
            return "("+value+")";
        }
    }


    Node root;

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

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

    public BinaryTree copy() {
        BinaryTree bt = new BinaryTree();
        bt.setRoot(root.copy());
        return bt;
    }

    public void inOrder(Node node, Consumer<Node> function) {
        if(node == null)
            return;
        inOrder(node.left,function);
        function.accept(node);
        inOrder(node.right,function);
    }

    public static void main(String[] args) {
        List<Integer> range = IntStream.range(0, 31).boxed().collect(Collectors.toList());
        BinaryTree bt = new BinaryTree();
        bt.build(range,0,31);
        BinaryTree copy = bt.copy();

        copy.inOrder(copy.getRoot(), (n) -> {
            System.out.print(n);
        });

    }
}
