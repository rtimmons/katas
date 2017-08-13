package rytim.kata;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

//
// Probably better-named as "graph" but has API resembling
// a rectangular grid.
//
public class Board<T> {

    // We know we want constant-access
    private final ArrayList<ArrayList<T>> rows;

    private final int rowCount;
    private final int colCount;

    public Board(int rowCount, int colCount, T defaultValue) {
        assert rowCount > 0;
        assert colCount > 0;
        this.rows =
                new ArrayList<>(IntStream.range(0, rowCount)
                        .mapToObj(rnum -> new ArrayList<>(IntStream.range(0, colCount)
                                .mapToObj(x -> defaultValue)
                                .collect(toList())))
                        .collect(toList()));
        this.rowCount = rowCount;
        this.colCount = colCount;
    }

//    public Board(int cols, T...vals) {
//        assert vals.length % cols == 0;
//
//        this.colCount = cols;
//        this.rowCount = vals.length / cols;
//
//        ArrayList<ArrayList<T>> rs = new ArrayList<>(rowCount);
//
//        ArrayList<T> col = new ArrayList<T>(colCount);
//        for(int ii=0; ii < vals.length; ii++) {
//            if ( ii > 0 && ii % colCount == 0 ) {
//                rs.add(cur);
//            }
//            cur = new ArrayList<T>;
//
//        }
//
//    }

    private T get(int row, int col) {
        assert valid(row, col);
        return this.rows.get(row).get(col);
    }

    private void set(int row, int col, T val) {
        assert valid(row, col);
        this.rows.get(row).set(col, val);
    }

    private boolean valid(int row, int col) {
        return row >= 0 && row < rowCount &&
                col >= 0 && col < colCount;
    }

    public Tile tile(int row, int col) {
        assert valid(row, col);
        return new Tile(row, col);
    }

    // TODO: use Row
    public Stream<Tile> inRow(int rowNum) {
        assert valid(rowNum, 0);
        return IntStream
                .rangeClosed(0, colCount - 1)
                .mapToObj(c -> new Tile(rowNum, c));
    }

    public Stream<Tile> inColumn(int colNum) {
        assert valid(0, colNum);
        return IntStream
                .rangeClosed(0, rowCount - 1)
                .mapToObj(r -> new Tile(r, colNum));
    }

//    public Stream<Tile> forwardDiagonal(int row, int col) {
//        // TODO
//    }

//    public Stream<Tile> backwardDiagonal(int row, int col) {
//        // TODO
//    }

    public Stream<Row> rows() {
        return IntStream
                .rangeClosed(0, rowCount - 1)
                .mapToObj(Row::new);
    }

    public Row row(int n) {
        return new Row(n);
    }

    public Column col(int n) {
        return new Column(n);
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        rows().forEach(r -> {
            final Consumer<Tile> append = (t) -> {
                out.append(t);
                out.append(" ");
            };
            r.tiles().forEach(append);
            out.append("\n");
        });
        return out.toString();
    }

    // Convenience classes

    public final class Row {
        private final int rowNum;

        private Row(int rowNum) {
            assert valid(rowNum, 0);
            this.rowNum = rowNum;
        }

        public Stream<Tile> tiles() {
            return Board.this.inRow(rowNum);
        }
    }


    public final class Column {
        private final int colNum;

        private Column(int colNum) {
            assert valid(0, colNum);
            this.colNum = colNum;
        }

        public Stream<Tile> tiles() {
            return Board.this.inColumn(colNum);
        }
    }

    public final class Path {
        final Tile head;
        final Path previous;

        public Path(Tile head, Path previous) {
            this.head = head;
            this.previous = previous;
        }

        boolean contains(Tile other) {
            return other.equals(head) ||
                    (previous != null && previous.contains(other));
        }

        Stream<Path> explore() {
            return head.neighbors()
                    .filter(n -> !this.contains(n))
                    .map(n -> new Path(n, this))
                    .flatMap(Path::explore);
        }

        public String toString() {
            return String.format("%s<-%s", head, previous);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Board.Path)) return false;

            Path path = (Path) o;

            if (!head.equals(path.head)) return false;
            return previous != null ? previous.equals(path.previous) : path.previous == null;
        }

        @Override
        public int hashCode() {
            int result = head.hashCode();
            result = 31 * result + (previous != null ? previous.hashCode() : 0);
            return result;
        }
    }

    public final class Tile {
        final int row;
        final int col;

        private Tile(int row, int col) {
            this.row = row;
            this.col = col;
        }

        boolean valid() {
            return Board.this.valid(row, col);
        }

        Row inRow() {
            return new Row(row);
        }

        Column inColumn() {
            return new Column(col);
        }

        void set(T val) {
            Board.this.set(row, col, val);
        }

        T get() {
            return Board.this.get(row, col);
        }

        Tile nw() {
            return new Tile(row + 1, col - 1);
        }

        Tile n() {
            return new Tile(row + 1, col);
        }

        Tile ne() {
            return new Tile(row + 1, col + 1);
        }

        Tile e() {
            return new Tile(row, col + 1);
        }

        Tile se() {
            return new Tile(row - 1, col + 1);
        }

        Tile s() {
            return new Tile(row + 1, col);
        }

        Tile sw() {
            return new Tile(row - 1, col - 1);
        }

        Tile w() {
            return new Tile(row, col - 1);
        }

        Path startPath() {
            return new Path(this, null);
        }

        Stream<Tile> neighbors() {
            return Stream.of(
                    this.nw(),
                    this.n(),
                    this.ne(),
                    this.e(),
                    this.se(),
                    this.s(),
                    this.sw(),
                    this.w()
            ).filter(Tile::valid);
        }

        public String toString() {
            return String.format("(%-2d,%-2d){%.3s}", col, row, String.valueOf(get()));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Board.Tile)) return false;

            Board<T>.Tile tile = (Board<T>.Tile) o;

            if (row != tile.row) return false;
            return col == tile.col;
        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + col;
            return result;
        }
    }

    //    @Test
    public void testAdjacent() {
        Board<Integer> b = new Board<>(2, 2, 0);
        Board.Tile a00 = b.tile(0, 0);
        a00.neighbors().forEach(System.out::println);
        System.out.println(a00.startPath().explore().collect(toList()));

//        System.out.println(a00);
    }
}

