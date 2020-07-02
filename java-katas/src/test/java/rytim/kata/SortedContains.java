package rytim.kata;

/*
You have k lists of sorted integers. Find the smallest range that includes at least one number from each of the k lists.

For example,
List 1: [4, 10, 15, 24, 26]
List 2: [0, 9, 12, 20]
List 3: [5, 18, 22, 30]

The smallest range here would be [20, 24] as it contains 24 from list 1, 20 from list 2, and 22 from list 3.


*/

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SortedContains {


    static class MinMax {
        final int min;
        final int max;

        public MinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MinMax minMax = (MinMax) o;

            if (min != minMax.min) return false;
            return max == minMax.max;
        }

        @Override
        public String toString() {
            return "MinMax{" +
                    "min=" + min +
                    ", max=" + max +
                    '}';
        }
    }
    static class RangeKeeper {
        final int[] sorted;
        int rinclusive;
        int linclusive;
        public RangeKeeper(int[] sorted) {
            this.sorted = sorted;
            this.rinclusive = sorted.length - 1;
            this.linclusive = 0;
        }
        boolean contains(int n) {
            return  sorted[linclusive] <= n &&
                    sorted[rinclusive] >= n;
        }

        int min() {
            return sorted[linclusive];
        }
        int max() {
            return sorted[rinclusive];
        }
    }

    static MinMax smallestRange(int[][] sets) {
        // TODO
        return null;
    }

    public static class Tests {
        @Test @Ignore // TODO
        public void testExample() {
            MinMax actual = smallestRange(new int[][] {
                    {4, 10, 15, 24, 26},
                    {0, 9, 12, 20},
                    {5, 18, 22, 30},
            });
            MinMax expect = new MinMax(20, 24);
            Assert.assertEquals(expect, actual);
        }
    }
}
