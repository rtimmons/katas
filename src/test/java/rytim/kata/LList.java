package rytim.kata;

public class LList<T> {

    final T data;
    LList<T> next;

    public LList(T data) {
        this.data = data;
    }

    boolean cycles() {
        return cycles(this);
    }

    static boolean cycles(LList<?> n) {
        LList slow = n;
        LList fast = n;

        while(slow != null) {
            slow = slow.next;

            if ( fast.next != null ) {
                fast = fast.next;
                if (fast == slow) {
                    return true;
                }
            }
            if ( fast.next != null ) {
                fast = fast.next;
                if ( fast == slow ) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String...arg) {
        LList<Integer> h1 = new LList<>(1);
        LList<Integer> h2 = new LList<>(2);
        LList<Integer> h3 = new LList<>(3);
        h3.next = h1;

        h1.next = h2;
        h2.next = h3;

        System.out.println(h1.cycles());
    }
}
