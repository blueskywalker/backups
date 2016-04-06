package org.blueskywalker.lucene.test;

/**
 * Created by kkim on 2/18/16.
 */
public class MergeNode {

    public static class Node {
        public Object value;
        public Node next;
    }


    public Node merge(Node x, Node y) {
        Node ret=null;
        Node last=null;

        if (x==null && y==null)
            return null;

        if(x!=null) {
            ret=x;
            last=x;
        }

        if(y!=null) {
            if(ret==null) {
                ret = y;
            } else {
                ret.next = y;
            }
            last=y;
        }

        last.next=merge(x!=null?x.next:null,y!=null?y.next:null);

        return ret;
    }
}
