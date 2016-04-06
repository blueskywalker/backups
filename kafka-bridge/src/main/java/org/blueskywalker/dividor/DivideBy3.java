package org.blueskywalker.dividor;

/**
 * Created by kkim on 3/26/16.
 */
public class DivideBy3 {
    //
    // sum
    //
    public static int add_iter(int x, int y) {

        while(y!=0) {
            int c = x & y;
            x = x ^ y;
            y = c << 1;
        }

        return x;
    }

    public static int add(int x, int y) {
        if (y ==0)
            return x;
        else
            return add(x^y,(x&y)<<1);
    }

    public static int divideby3(int num) {
        int sum = 0;
        while (num > 3) {
            sum = add(num >> 2, sum);
            num = add(num >> 2, num & 3);
        }

        if (num == 3)
            sum = add(sum,1);

        return sum;
    }

    public static void main(String[] args) {
        System.out.println(add(4,5));
        System.out.println(add(14,5));
        System.out.println(divideby3(100));
    }
}
