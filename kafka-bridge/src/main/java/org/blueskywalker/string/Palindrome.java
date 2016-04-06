package org.blueskywalker.string;

/**
 * Created by kkim on 3/10/16.
 */
public class Palindrome {

    public static boolean isPal(String input) {
        int start=0;
        int end = input.length()-1;

        while(start<end) {
            if (input.charAt(start) != input.charAt(end))
                return false;
        }

        return true;
    }

    public static boolean isPalWord(String a, String b) {
        return isPal(a + b);
    }


    public static boolean isPalindrome(String in) {
        if (in.length()<2) return true;

        if (in.charAt(0) == in.charAt(in.length()-1))
            return isPalindrome(in.substring(1,in.length()-1));

        return false;
    }

    public static void main(String[] args) {
        System.out.println(Palindrome.isPalindrome("bob"));
        System.out.println(Palindrome.isPalindrome("cop"));
    }

}
