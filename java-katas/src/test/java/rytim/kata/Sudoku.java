package rytim.kata;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.fail;

public class Sudoku {
    static final int Q = 0;

    final Board board;
    public Sudoku(int size, int[] board) {
        this.board = new Board(size, board);
    }

    public String toString() {
        return this.board.toString();
    }

    void solve() {
        List<Board.Square> blanks = board.blanks().collect(toList());
        if (!explore(blanks)) {
            System.out.println(board);
            throw new IllegalArgumentException("Unsolvable");
        }
    }

    boolean explore(List<Board.Square> blanks) {
        if (blanks.isEmpty()) {
            return board.valid();
        }
        Board.Square first = blanks.get(0);
        List<Board.Square> rest = blanks.subList(1, blanks.size());

        for(int i=1; i <= board.size * board.size; i++) {
            first.set(i);
            if (explore(rest)) {
                return true;
            }
            first.clear();
        }
        return false;
    }

    private static class Board {
        final int[] cells;
        final int size;
        final int sq;
        final int expectSum;
        int sum;

        public Board(int size, int[] board) {
            this.size = size;
            if (board == null || board.length != size * size * size * size) {
                throw new IllegalArgumentException("Invalid board");
            }
            this.cells = board;
            this.sq = size * size;

            int groupSum = sq * (sq + 1)/2;
            this.expectSum = sq * groupSum;

            sum = Arrays.stream(board).sum();
        }
        int get(int x, int y) {
            return cells[y*size*size + x];
        }
        Board set(int x, int y, int val) {
            sum -= get(x,y);
            sum += val;

            cells[y*size*size + x] = val;
            return this;
        }
        Board clear(int x, int y) {
            return set(x,y,Q);
        }

        boolean validRow(int row, Tally t) {
            if (row >= size*size) {
                throw new IllegalArgumentException("Invalid row " + row);
            }
            t.clear();
            for(int col = 0; col < size*size; col++) {
                t.mark(get(col,row));
            }
            return t.okay();
        }

        boolean validColumn(int column, Tally t) {
            if (column >= size*size) {
                throw new IllegalArgumentException("Invalid column " + column);
            }
            t.clear();
            for(int row = 0; row < size*size; row++) {
                t.mark(get(column,row));
            }
            return t.okay();
        }

        boolean validBox(int box, Tally t) {
            if (box >= size*size) {
                throw new IllegalArgumentException("Invalid box " + box);
            }
            int rowOffset = size * (box / size);
            int colOffset = size * (box % size);
            t.clear();
            for(int row = 0; row < size; row++) {
                for(int col = 0; col < size; col++) {
                    t.mark(get(colOffset + col, rowOffset + row));
                }
            }
            return t.okay();
        }


        boolean valid() {
            if (sum != expectSum) {
                return false;
            }
            Tally t = new Tally(size);
            for(int i=0; i < size*size; i++) {
                if (!validRow(i, t)) {
                    return false;
                }
                if (!validColumn(i, t)) {
                    return false;
                }
                if (!validBox(i, t)) {
                    return false;
                }
            }
            return true;
        }

        static class Tally {
            final boolean[] seen;
            final int size;
            boolean failed = false;
            Tally(int size) {
                this.seen = new boolean[size * size];
                this.size = size;
            }
            Tally mark(int i) {
                if ( i == Q ) {
                    failed = true;
                    return this;
                }
                if ( i <= 0 || i > size*size ) {
                    throw new IllegalArgumentException("Invalid tally " + i);
                }
                if(seen[i-1]) {
                    failed = true;
                }
                seen[i-1] = true;
                return this;
            }
            boolean okay() {
                if(failed) { return false; }
                for(boolean s : seen) {
                    if (!s) {
                        return false;
                    }
                }
                return true;
            }
            Tally clear() {
                for(int i=0; i < seen.length; i++) {
                    seen[i] = false;
                }
                this.failed = false;
                return this;
            }
        }

        Stream<Square> blanks() {
            return IntStream.range(0, size*size*size*size)
                    .mapToObj(Square::new)
                    .filter(Square::blank);
        }

        private class Square {
            final int col; final int row;
            public Square(int number) {
                this.row = number / (size*size);
                this.col = number % (size*size);
            }

            boolean blank() {
                return get() == Q;
            }
            int get() {
                return Board.this.get(this.col, this.row);
            }
            boolean set(int val) {
                Board.this.set(this.col, this.row, val);
                return Board.this.valid();
            }
            void clear() {
                Board.this.clear(this.col, this.row);
            }
        }

        public String toString() {
            StringBuilder out = new StringBuilder();
            for(int ix = 1; ix <= this.cells.length; ix++) {
                out.append(this.cells[ix-1]);
                out.append(" ");

                if ( ix % (size*size) == 0 ) {
                    out.append("\n");
                    if (ix % (size*size*size) == 0) {
                        out.append("\n");
                    }
                } else if ( ix % size == 0) {
                    out.append("   ");
                }
            }
            return out.toString();
        }
    }


    public static class Tests {

        @Test
        public void testValid2by2() {
            Sudoku s = new Sudoku(2, new int[]{
                    1, 2,   4, 3,
                    3, 4,   2, 1,

                    2, 3,   1, 4,
                    4, 1,   3, 2,
            });
            Board.Tally t = new Board.Tally(2);
            Assert.assertEquals(true, s.board.validBox(0, t));
            Assert.assertEquals(true, s.board.validBox(1, t));
            Assert.assertEquals(true, s.board.validBox(2, t));
            Assert.assertEquals(true, s.board.validBox(3, t));

            Assert.assertEquals(true, s.board.validColumn(0, t));
            Assert.assertEquals(true, s.board.validColumn(1, t));
            Assert.assertEquals(true, s.board.validColumn(2, t));
            Assert.assertEquals(true, s.board.validColumn(3, t));

            Assert.assertEquals(true, s.board.validRow(0, t));
            Assert.assertEquals(true, s.board.validRow(1, t));
            Assert.assertEquals(true, s.board.validRow(2, t));
            Assert.assertEquals(true, s.board.validRow(3, t));

            Assert.assertEquals(true, s.board.valid());
        }

        @Test
        public void testSolves2by2() {
            Sudoku s = new Sudoku(2, new int[]{
                    Q, 2,   4, 3,
                    3, Q,   2, 1,

                    2, 3,   Q, 4,
                    4, 1,   3, Q,
            });
            s.solve();
            System.out.println(s.board);
            Assert.assertEquals(true, s.board.valid());
        }

        @Test
        public void testSolvesHarder2by2() {
            Sudoku s = new Sudoku(2, new int[]{
                    2, Q,   Q, 4,
                    Q, Q,   1, Q,

                    Q, 3,   Q, Q,
                    Q, Q,   Q, Q,
            });
            s.solve();
            System.out.println(s.board);
            Assert.assertEquals(true, s.board.valid());
        }
        @Test
        public void testInvalid2by2() {
            Sudoku s = new Sudoku(2, new int[]{
                    1, 2,   4, 3,
                    3, 4,   2, 1,

                    2, 3,   4, 4,
                    4, 1,   3, 2,
            });
            Board.Tally t = new Board.Tally(2);
            Assert.assertEquals(true, s.board.validBox(0, t));
            Assert.assertEquals(true, s.board.validBox(1, t));
            Assert.assertEquals(true, s.board.validBox(2, t));
            Assert.assertEquals(false, s.board.validBox(3, t));

            Assert.assertEquals(true, s.board.validColumn(0, t));
            Assert.assertEquals(true, s.board.validColumn(1, t));
            Assert.assertEquals(false, s.board.validColumn(2, t));
            Assert.assertEquals(true, s.board.validColumn(3, t));

            Assert.assertEquals(true, s.board.validRow(0, t));
            Assert.assertEquals(true, s.board.validRow(1, t));
            Assert.assertEquals(false, s.board.validRow(2, t));
            Assert.assertEquals(true, s.board.validRow(3, t));

            Assert.assertEquals(false, s.board.valid());
            try {
                s.solve();
                fail("expected exception");
            }
            catch(IllegalArgumentException expected) {}
        }

        // http://www.dailysudoku.com/sudoku/examples/classic.pdf
        @Test
        public void realTestOne() {
            Sudoku s = new Sudoku(3, new int[] {
                    Q,Q,2,  Q,9,Q,  Q,6,Q,
                    Q,4,Q,  Q,Q,1,  Q,Q,8,
                    Q,7,Q,  4,2,Q,  Q,Q,3,

                    5,Q,Q,  Q,Q,Q,  3,Q,Q,
                    Q,Q,1,  Q,6,Q,  5,Q,Q,
                    Q,Q,3,  Q,Q,Q,  Q,Q,6,

                    1,Q,Q,  Q,5,7,  Q,4,Q,
                    6,Q,Q,  9,Q,Q,  Q,2,Q,
                    Q,2,Q,  Q,8,Q,  1,Q,Q,
            });
//            s.solve();
        }

    }

}
