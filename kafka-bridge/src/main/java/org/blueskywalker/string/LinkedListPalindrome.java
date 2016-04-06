package org.blueskywalker.string;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kkim on 3/26/16.
 */
public class LinkedListPalindrome {

    public static class Node {
        public Node(int data) {
            this.data = data;
        }

        int data;
        Node link;
    }

    public boolean isPalindrome()
    {
        Node node = isPalindrome(head, head);
        if(node == null)
            return false;
        return true;
    }

    private Node isPalindrome(Node left, Node right)
    {
        if(right == null)
        {
            return left;
        }

        left = isPalindrome(left, right.link);
        if(left != null)
        {
            boolean palindrome = left.data == right.data ? true : false;
            if(palindrome)
            {
                left = (left.link != null) ? left.link : left;
                return left;
            }
        }
        return null;
    }

    Node head;

    public void add(int data) {
        if(head==null)
            head=new Node(data);
        else {
            Node newNode = new Node(data);
            newNode.link = head;
            head = newNode;
        }

    }

    public static void main(String[] args) {

        List<Integer> data = Arrays.asList(1,2,3,4,5,4,3,2,1);

        LinkedListPalindrome llp = new LinkedListPalindrome();

        for(Integer n: data) {
            llp.add(n);
        }

        System.out.println(llp.isPalindrome());
    }
}
