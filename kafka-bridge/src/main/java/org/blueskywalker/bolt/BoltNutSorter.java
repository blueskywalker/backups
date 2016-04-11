package org.blueskywalker.bolt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by kkim on 4/9/16.
 */
public class BoltNutSorter {

    public static <T extends Comparable> int partition(List<T> list,int start,int end, T pivot) {

        while(start<end) {
            while(start<end && list.get(start).compareTo(pivot)<0) start++;
            while(start<end && list.get(end).compareTo(pivot)>0) end--;
            if (start<end) Collections.swap(list,start,end);
        }

        return end;
    }

    public static <T extends Comparable> void sortBoltNut(List<T> nut, List<T> bolt,int start,int end) {

        if(end<=start) return;

        T pivot = nut.get((start+end)>>1);
        int mid1 = partition(nut,start,end,pivot);
        int mid2 = partition(bolt,start,end,pivot);

        assert mid1 == mid2;

        sortBoltNut(nut,bolt,start,mid1-1);
        sortBoltNut(nut,bolt,mid2+1,end);
    }

    public static void main(String[] args) {

        /*
        List<Integer> bolt=
                IntStream.rangeClosed(1,20).boxed().collect(Collectors.toList());

        Collections.shuffle(bolt);
        System.out.println(bolt);

        ArrayList<Integer> nut = new ArrayList<>(bolt);
        Collections.shuffle(nut);
        System.out.println(nut);
        */
        List<Integer> bolt = Arrays.asList(16, 7, 13, 9, 15, 4, 17, 20, 18, 3, 8, 1, 12, 2, 14, 5, 19, 6, 11, 10);
        List<Integer> nut = Arrays.asList(20, 6, 14, 16, 3, 12, 10, 11, 7, 8, 1, 19, 4, 13, 5, 2, 18, 15, 17, 9);


        sortBoltNut(nut,bolt,0,nut.size()-1);
        System.out.println(nut);
        System.out.println(bolt);
    }
}

