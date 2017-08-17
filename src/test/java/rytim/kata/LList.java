package rytim.kata;

import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

public class LList<T> {


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


