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
        int hi  = haystack.length - 1;
        while(low < hi) {
            int mid = (hi - low) / 2;
            if(haystack[mid] < needle) {
                hi = mid + 1;
            }
            else if (haystack[mid] > needle) {
                low = mid - 1;
            }
            else {
                return mid;
            }
        }
        return -(low - 1);
    }

    public static class Tests {
        @Test
        public void tests() {
            Assert.assertEquals(0, indexOf(1, new int[]{ 1 }));
            Assert.assertEquals(1, indexOf(1, new int[]{ 0, 1 }));
            Assert.assertEquals(0, indexOf(0, new int[]{ 0, 1 }));
        }
    }
}
