package rytim.kata;

public class LList<T> {


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

    // TODO: don't expose the delegate! dirty refactor to support #remove()
    public static void main(String... arg) {
        LList<Integer> h1 = new LList<>(1);
        LList<Integer> h2 = new LList<>(2);
        LList<Integer> h3 = new LList<>(3);
        h3.delegate.next = h1.delegate;

        h1.delegate.next = h2.delegate;
        h2.delegate.next = h3.delegate;

        System.out.println(h1.delegate.cycles());
    }
}


