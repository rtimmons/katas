package rytim.kata;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.Objects;

public class LList<T> implements Iterable<T> {

    public void add(T v) {
        if (this.delegate == null) {
            this.delegate = new Delegate<>(v);
            return;
        }
        Delegate curr = delegate;
        while(curr.next != null) {
            curr = curr.next;
        }
        curr.next = new Delegate<>(v);
    }

    public boolean contains(T needle) {
        for(Delegate<T> curr = delegate; curr != null; curr = curr.next) {
            if ( Objects.equals(needle, curr.data) ) {
                return true;
            }
        }
        return false;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Delegate curr = delegate;
            @Override
            public boolean hasNext() {
                return curr != null;
            }

            @Override
            public T next() {
                T out = (T)curr.data;
                curr = curr.next;
                return out;
            }
        };
    }

    public void remove(T needle) {
        // head
        if (Objects.equals(needle, this.delegate.data)) {
            this.delegate = this.delegate.next;
            return;
        }

        Delegate curr = delegate;

        while (curr != null) {
            if (curr.next != null) {
                if (Objects.equals(needle, curr.next.data)) {
                    curr.next = curr.next.next;
                    return;
                }
            }
            curr = curr.next;
        }
    }

    public String toString() {
        StringBuilder out = new StringBuilder("[");
        Delegate curr = delegate;
        while (curr != null) {
            out.append(String.valueOf(curr.data));
            if (curr.next != null) {
                out.append(",");
            }
            curr = curr.next;
        }
        return out.append("]").toString();
    }

    Delegate<T> delegate;

    public LList(T data) {
        this.delegate = new Delegate<>(data);
    }

    public static class Delegate<T> {

        final T data;
        Delegate<T> next;

        public Delegate(T data) {
            this.data = data;
        }

        boolean cycles() {
            return cycles(this);
        }

        static boolean cycles(Delegate<?> n) {
            Delegate slow = n;
            Delegate fast = n;

            // TODO: subtle bug here?
            while (slow != null) {
                slow = slow.next;

                if (fast.next != null) {
                    fast = fast.next;
                    if (fast == slow) {
                        return true;
                    }
                }
                if (fast.next != null) {
                    fast = fast.next;
                    if (fast == slow) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static class Tests {
        @Test
        public void testAddContainsRemove() {
            LList<Integer> l = new LList<>(1);
            l.add(2);
            l.add(3);
            Assert.assertEquals("[1,2,3]", l.toString());

            Assert.assertTrue(l.contains(1));
            Assert.assertTrue(l.contains(2));
            Assert.assertTrue(l.contains(3));
            Assert.assertFalse(l.contains(4));

            l.remove(2);
            Assert.assertEquals("[1,3]", l.toString());
            Assert.assertFalse(l.contains(2));

            l.remove(1);
            Assert.assertEquals("[3]", l.toString());
            Assert.assertFalse(l.contains(1));

            l.remove(4); // !contains(4)
            Assert.assertEquals("[3]", l.toString());
            Assert.assertFalse(l.contains(1));

            l.remove(3);
            Assert.assertEquals("[]", l.toString());
            Assert.assertFalse(l.contains(3));
        }
        @Test
        public void testCycles() {
            // TODO: don't expose the delegate! dirty refactor to support #remove()
            LList<Integer> h1 = new LList<>(1);
            LList<Integer> h2 = new LList<>(2);
            LList<Integer> h3 = new LList<>(3);

            h1.delegate.next = h2.delegate;
            h2.delegate.next = h3.delegate;
            Assert.assertEquals("[1,2,3]", h1.toString());

            // TODO: this should pass
//            Assert.assertFalse(h1.delegate.cycles());
            h3.delegate.next = h1.delegate;
            Assert.assertTrue(h1.delegate.cycles());
        }
    }
}


