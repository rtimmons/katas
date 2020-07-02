package rytim.kata;

/*

Quote [sic] all over, but interesting problem:


   You are given a list of word. Find if two words can be joined to-gather to form a
   palindrome. eg Consider a list {bat, tab, cat} Then bat and tab can be joined to
   gather to form a palindrome.

    Expecting a O(nk) solution where n = number of works and k is length

    There can be multiple pairs

TODO: below is O(nk^2) not sure if O(nk) is possible if dictionary is comprised of
words of different lengths - have to test for palindrome-ness rather than just
checking each word to see if its reverse is in the dictionary.
Worth more thought


 */

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

public class FindPalindromePairs {

    // O(k) time, O(1) space (or O(k) if you acknowledge
    // the harsh reality that .toCharArray() does a copy).
    static boolean isPalindrome(String in) {
        char[] letters = in.toCharArray();
        for(int i=0; i < letters.length/2; i++) {
            if(letters[i] != letters[letters.length-1-i]) {
                return false;
            }
        }
        return true;
    }

    // O(k) time and space
    static String rev(String in) {
        char[] out = new char[in.length()];
        char[] letters = in.toCharArray();
        for(int i = letters.length-1; i >= 0; i--) {
            out[letters.length-1 - i] = letters[i];
        }
        return new String(out);
    }

    static String findPalindrome(List<String> dict) {
        Set<String> copy = new HashSet<>(dict);
        // O(n)
        for(String s : copy) {
            String rev = rev(s); // O(k)
            int len = rev.length();

            for(int upto=0;upto<=len;upto++) { // loop body is O(k) run O(k) times
                // O(k)
                String part = rev.substring(0, upto);
                // O(k)
                String guess = part + s;
                //      O(k)                 O(1)              O(k)
                if(!part.equals(s) && copy.contains(part) && isPalindrome(guess)) {
                    return guess;
                }
            }
        }
        return null;
    }

    public static class Helpers {
        @Test
        public void testRev() {
            Assert.assertEquals("cba", rev("abc"));
        }
        @Test
        public void testPal() {
            Assert.assertEquals(true, isPalindrome("aba"));
            Assert.assertEquals(true, isPalindrome("a"));
            Assert.assertEquals(true, isPalindrome(""));
            Assert.assertEquals(true, isPalindrome("abba"));
            Assert.assertEquals(true, isPalindrome("abcba"));
            Assert.assertEquals(false, isPalindrome("ab"));
            Assert.assertEquals(false, isPalindrome("abcbca"));
        }
    }

    @RunWith(Parameterized.class)
    public static class Tests {
        static final String END = "END";
        @Parameterized.Parameters
        public static Iterable<String[][]> cases() {
            return Arrays.asList(new String[][][] {
                    {{"bat", "tab", "cat", END, "tabbat"}},
                    {{"bat", "cat", END, null}},
                    {{"b", "ab", END, "bab"}},
                    {{"b","cab",END,null}},
                    {{"abba", "cabba", END, "abbacabba"}},
            });
        }

        @Test
        public void tcase() {
            String actual = findPalindrome(dict);
            Assert.assertEquals(
                    "" + dict + " => " + expect,
                    expect, actual);
        }

        List<String> dict = new LinkedList<>();
        String expect;
        public Tests(String[] tcase) {
            boolean end = false;
            for(String t : tcase) {
                if (end) {
                    expect = t;
                    break;
                }
                if ( t == END ) {
                    end = true;
                    continue;
                }
                dict.add(t);
            }
        }
    }

}
