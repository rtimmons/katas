package rytim.kata;

import org.junit.Test;

import java.util.*;

public class Permutations {

    static <T> Iterator<List<T>> permute(List<T>...ls) {
        return new PIter<T>(Arrays.asList(ls));
    }

    static class PIter<T> implements Iterator<List<T>> {

        final List<List<T>> ls;

        int ix = 0;
        final int[] sizes;

        public PIter(List<List<T>> ls) {
            this.ls = ls;
            sizes = new int[ls.size() + 1];
            sizes[ls.size()] = 1;

            for(int i=ls.size() - 1; i >= 0; i--) {
                sizes[i] = sizes[i+1] * ls.get(i).size();
            }
        }

        @Override
        public String toString() {
            return "PIter{" +
                    "sizes=" + Arrays.toString(sizes) +
                    '}';
        }

        @Override
        public boolean hasNext() {
            return ix <= sizes[0];
        }

        @Override
        public List<T> next() {

            IXList<T> out = new IXList(ix, ls, sizes);
            ix++;
            return out;
        }

        static class IXList<T> extends AbstractList<T> {
            final int ix;
            final List<List<T>> into;
            final int[] sizes;

            public IXList(int ix, List<List<T>> into, int[] sizes) {
                this.ix = ix;
                this.into = into;
                this.sizes = sizes;
            }

            @Override
            public T get(int axis) {
                int p = (ix/sizes[axis+1]) % into.get(axis).size();
                return into.get(axis).get(p);
            }

            @Override
            public int size() {
                return into.size();
            }
        }
    }

    public static class Tests {
        @Test
        public void main() {
            Iterator<List<Integer>> permute = permute(
                    Arrays.asList(1, 2, 3),
                    Arrays.asList(11, 12),
                    Arrays.asList(21, 22, 23, 24)
            );
            while(permute.hasNext()) {
                System.out.println(permute.next());
            }
        }
    }

}
