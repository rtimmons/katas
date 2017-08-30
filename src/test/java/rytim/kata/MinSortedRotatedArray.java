package rytim.kata;


import org.junit.Assert;
import org.junit.Test;

/*
A sorted array has been rotated so that the elements might
appear in the order 3 4 5 6 7 1 2.
How would you find the minimum element?
*/
public class MinSortedRotatedArray {

    static int min(int[] ns) {
        return ns == null || ns.length <= 0 ? 0 : min(ns, 0, ns.length - 1);
    }

    static int min(int[] ns, int low, int hi) {
        if (low == hi)
            return ns[low];
        if (low == hi - 1)
            return Math.min(ns[low], ns[hi]);

        int mid = low + (hi - low) / 2;

        return ns[mid] < ns[hi]
        ?   mid > 0 && ns[mid-1] > ns[mid]
            ?   ns[mid]
            :   min(ns, low, mid-1)
        :   min(ns, mid, hi);
    }

    public static class Tests {
        @Test
        public void test1() {
            Assert.assertEquals(1, min(new int[]{4, 5, 1, 2, 3}));
            Assert.assertEquals(1, min(new int[]{5, 1, 2, 3, 4}));
            Assert.assertEquals(1, min(new int[]{1, 2, 3, 4, 5}));
            Assert.assertEquals(1, min(new int[]{2, 3, 4, 5, 1}));
            Assert.assertEquals(1, min(new int[]{3, 4, 5, 1, 2}));
        }

        @Test
        public void test2() {
            Assert.assertEquals(1, min(new int[]{1}));
            Assert.assertEquals(1, min(new int[]{1, 2}));
            Assert.assertEquals(1, min(new int[]{1, 1, 1, 1, 2}));
            Assert.assertEquals(-1, min(new int[]{-1, 0, 1, 2, 3}));
        }
    }

}
