package rytim.kata;

import org.junit.Test;
import org.junit.runners.Suite;

import java.util.*;

public class MergeSort {

    Queue<Integer> merge(Queue<Integer> lq, Queue<Integer> rq) {
        Queue<Integer> out = new LinkedList<Integer>();

        while(!lq.isEmpty() && !rq.isEmpty()) {
            Integer l = lq.remove();
            Integer r = rq.remove();
            out.add( l <= r ? l : r );
        }
        out.addAll(lq);
        out.addAll(rq);
        return out;
    }

    Queue<Integer> sort(Queue<Integer> in) {
        if (in == null || in.isEmpty()) {
            return in;
        }
        int size = in.size();
        if ( size == 1 ) {
            return in;
        }

        int mid = size / 2;
        // TODO
//        List<Integer> left = sort(in.subList(0, mid));
//        List<Integer> right = sort(in.subList(size-mid, size));
//
//        return merge(left, right);
        return null;
    }

    public static class Tests {
        @Test
        public void main() {
            MergeSort m = new MergeSort();
            List<Integer> t = Arrays.asList(1, 3, 5, 0, 2, 1);
//        System.out.println(m.sort(t));
        }
    }


}
