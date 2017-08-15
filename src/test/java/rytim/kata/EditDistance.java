package rytim.kata;

import org.junit.Assert;
import org.junit.Test;

public class EditDistance {

    static int editDistance(String a, String b) {
        return editDistance(a.toCharArray(), b.toCharArray(), a.length(), b.length());
    }

    // TODO: memoize by storing return values of (aright,bright) pairs
    static int editDistance(char[] a, char[] b, int aright, int bright) {
        if (aright == 0 ) {
            // can only add all of b
            return bright;
        }
        if (bright == 0) {
            // can only add all of a
            return aright;
        }
        if (a[aright-1] == b[bright-1]) {
            // yay free edit
            return editDistance(a, b, aright-1, bright-1);
        }

        // dynamic programming

        int add = editDistance(a, b, aright, bright-1);
        int rem = editDistance(a, b, aright-1, bright);
        int rep = editDistance(a, b, aright-1, bright-1);

        return 1 // just did an add/rem/rep
                + Math.min(add, Math.min(rem, rep)); // cost of cheapest
    }

    public static class Tests {
        @Test
        public void testOne() {
            Assert.assertEquals(1, editDistance("a",    "aa"));
            Assert.assertEquals(0, editDistance("aa",   "aa"));
            Assert.assertEquals(1, editDistance("a",    "b"));
            Assert.assertEquals(3, editDistance("abc",  ""));
            Assert.assertEquals(3, editDistance("abc",  ""));
            Assert.assertEquals(4, editDistance("abc",  "defg"));
            Assert.assertEquals(2, editDistance("abc",  "cba")); // swap a->c, c->a
        }
    }

}
