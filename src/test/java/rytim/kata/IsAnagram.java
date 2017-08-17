package rytim.kata;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rtimmons@
 * @since 8/17/17
 */
// Write a method to decide if two strings are anagrams or not

public class IsAnagram {
    static boolean isAnagram(String a, String b) {
        // TODO nullcheck
        return isAnagram(a.toCharArray(), b.toCharArray());
    }
    static boolean isAnagram(char[] a, char[] b){

        Map<Character,Integer> acount = count(a);
        Map<Character,Integer> bcount = count(a);
        return acount.equals(bcount);
    }
    static Map<Character,Integer> count(char[] a) {
        Map<Character,Integer> counts = new HashMap<>();
        for(Character c : a) {
            if(counts.containsKey(c)) {
                counts.put(c, counts.get(c) + 1);
            }
            counts.put(c,0);
        }
        return counts;
    }


    public static class Tests {
        @Test
        public void foo() {
            Assert.assertTrue(isAnagram("a", "a"));
            Assert.assertTrue(isAnagram("ab", "ab"));
            Assert.assertTrue(isAnagram("ab", "ba"));
            Assert.assertTrue(isAnagram("redcar", "redcar"));
            Assert.assertTrue(isAnagram("foobar", "fboaor"));
        }
    }
}
