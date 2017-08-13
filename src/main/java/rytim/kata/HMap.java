package rytim.kata;

import java.util.*;

// Hash-map. For Kata purposes,
// - keys are Strings
// - values ints
// - resize only when full
// - resize recreates
class HMap {

    // TODO

    Delegate delegate;
    HMap() {
        this.delegate = new Delegate(20);
    }


    private static class Delegate {
        final ArrayList<LinkedList<Integer>> buckets;
        Delegate(int bucketCount) {
            this.buckets = new ArrayList<>(bucketCount);
        }
    }

}
