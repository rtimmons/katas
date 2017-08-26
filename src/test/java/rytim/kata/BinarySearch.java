package rytim.kata;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author rtimmons@
 * @since 8/23/17
 */
public class BinarySearch {
    static int indexOf(int needle, int[] haystack) {
        int low = 0;
        int high = haystack.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            long midVal = haystack[mid];

            if (midVal < needle) {
                low = mid + 1;
            }
            else if (midVal > needle) {
                high = mid - 1;
            }
            else {
                return mid;
            }
        }
        return -1;
    }

    public static class Tests {
        @Test
        public void tests() {
            Assert.assertEquals("Single element", 0, indexOf(1, new int[]{ 1 }));
            Assert.assertEquals("last", 1, indexOf(1, new int[]{ 0, 1 }));
            Assert.assertEquals("first", 0, indexOf(0, new int[]{ 0, 1 }));
            Assert.assertEquals("middle", 1, indexOf(1, new int[]{ 0, 1, 2 }));
            Assert.assertEquals("last3", 2, indexOf(2, new int[]{ 0, 1, 2 }));
            Assert.assertEquals("lastrev", 2, indexOf(1, new int[]{ -1, 0, 1 }));
            Assert.assertEquals("notfound", -1, indexOf(1, new int[]{ 2,2,2 }));
        }
    }
}
