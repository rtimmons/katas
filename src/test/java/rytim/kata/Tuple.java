package rytim.kata;

public class Tuple<A,B> {
    final A a;
    final B b;

    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }
    static <A,B> Tuple<A,B> of(A a, B b) {
        return new Tuple<>(a, b);
    }
    private static final Tuple<?,?> empty = new Tuple<>(null,null);
    static <A,B> Tuple<A,B> empty() {
        return (Tuple<A, B>) empty;
    }

    @Override
    public String toString() {
        return "("+String.valueOf(a)+","+String.valueOf(b)+")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple<?, ?> tuple = (Tuple<?, ?>) o;

        if (a != null ? !a.equals(tuple.a) : tuple.a != null) return false;
        return b != null ? b.equals(tuple.b) : tuple.b == null;
    }

    @Override
    public int hashCode() {
        int result = a != null ? a.hashCode() : 0;
        result = 31 * result + (b != null ? b.hashCode() : 0);
        return result;
    }
}
