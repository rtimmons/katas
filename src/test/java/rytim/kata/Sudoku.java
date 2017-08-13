package rytim.kata;

import org.junit.Assert;
import org.junit.Test;

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
        if (!explore()) {
            throw new IllegalArgumentException("Unsolvable board");
        }
    }

    boolean explore() {
        for(int y = 0; y < board.size*board.size; y++) {
            for(int x = 0; x < board.size*board.size; x++) {
                for(int guess = 1; guess <= board.size*board.size; guess++) {
                    if(board.get(x,y) == Q) {
                        board.set(x, y, guess);
                        if (!explore()) {
                            board.clear(x, y);
                        }
                    }
                }
            }
        }
        return board.valid();
    }

    private static class Board {
        final int[] cells;
        final int size;
        public Board(int size, int[] board) {
            this.size = size;
            if (board == null || board.length != size * size * size * size) {
                throw new IllegalArgumentException("Invalid board");
            }
            this.cells = board;
        }
        int get(int x, int y) {
            return cells[y*size*size + x];
        }
        Board set(int x, int y, int val) {
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
