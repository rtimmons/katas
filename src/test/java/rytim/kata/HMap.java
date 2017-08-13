package rytim.kata;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

// Hash-map. For Kata purposes,
// - keys are Strings
// - values ints
// - no resizing (yet)
class HMap {

    Delegate delegate;

    HMap() {
        this.delegate = new Delegate(5);
    }

    private void put(String key, int val) {
        // TODO: resize
        delegate.put(key, val);
    }

    public int get(String key) {
        return delegate.get(key);
    }

    private static class Delegate {
        final ArrayList<LinkedList<Entry>> buckets;
        final int bucketCount;

        Delegate(int bucketCount) {
            this.buckets = new ArrayList<>(bucketCount);
            for(int i=0; i < bucketCount; i++) {
                this.buckets.add(new LinkedList<>());
            }
            this.bucketCount = bucketCount;
        }

        public void put(String key, int val) {
            remove(key).add(new Entry(key, val));
        }

        private LinkedList<Entry> bucketFor(String key) {
            int bucketIndex = Math.abs(key.hashCode() % this.bucketCount);
            return buckets.get(bucketIndex);
        }

        public int get(String key) {
            return bucketFor(key).stream()
                    .filter(e -> e.hasKey(key))
                    .map(e -> e.value)
                    .findFirst()
                    .orElse(0);
        }

        public int size() {
            return buckets.stream().collect(Collectors.summingInt(List::size));
        }

        public boolean containsKey(String key) {
            return bucketFor(key).stream()
                    .anyMatch(e -> e.hasKey(key));
        }

        // returns bucket for key
        public LinkedList<Entry> remove(String key) {
            LinkedList<Entry> bucket = bucketFor(key);
            bucket.removeIf(e -> e.hasKey(key));
            return bucket;
        }

        public static class Entry {
            final String key;
            final int value;

            public Entry(String key, int value) {
                this.key = key;
                this.value = value;
            }
            boolean hasKey(String key) {
                return this.key.equals(key);
            }
        }
    }

    public static class Tests {
        HMap h = new HMap();
        @Test
        public void getUnknownKeyReturnsZero() {
            Assert.assertEquals(0, h.get("unknown"));
        }
        @Test
        public void testSize() {
            Assert.assertEquals(0, h.size());
            h.put("foo", 1);
            Assert.assertEquals(1, h.size());
            h.put("foo", 2);
            Assert.assertEquals(1, h.size());
            h.put("newFoo", 3);
            Assert.assertEquals(2, h.size());
        }
        @Test
        public void testHasKey() {
            Assert.assertEquals(false, h.containsKey("foo"));
            h.put("foo", 1);
            Assert.assertEquals(true, h.containsKey("foo"));
        }
        @Test
        public void testRemove() {
            h.put("foo", 1);
            Assert.assertEquals(true, h.containsKey("foo"));
            h.remove("foo");
            Assert.assertEquals(false, h.containsKey("foo"));
            Assert.assertEquals(0, h.size());
        }
        @Test
        public void putThenGet() {
            h.put("foo", 1);
            Assert.assertEquals(1, h.get("foo"));
        }
        @Test
        public void putThenRePutThenGet() {
            h.put("foo", 1);
            h.put("foo", 2);
            Assert.assertEquals(2, h.get("foo"));
        }
        @Test
        public void putTwice() {
            h.put("foo", 1);
            h.put("bar", 2);
            Assert.assertEquals(1, h.get("foo"));
            Assert.assertEquals(2, h.get("bar"));
        }
    }

    private void remove(String key) {
        delegate.remove(key);
    }

    private boolean containsKey(String foo) {
        return delegate.containsKey(foo);
    }

    public int size() {
        return delegate.size();
    }
}
