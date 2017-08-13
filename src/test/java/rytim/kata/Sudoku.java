package rytim.kata;

import org.junit.Test;

public class Sudoku {
    static final int SIZE = 3;
    static final int Q    = 0;

    final Board board;

    public Sudoku(int[] board) {
        this.board = new Board(board);
    }

    public String toString() {
        return this.board.toString();
    }

    private static class Board {
        final int[] cells;
        public Board(int[] board) {
            if (board == null || board.length != SIZE * SIZE * SIZE * SIZE) {
                throw new IllegalArgumentException("Invalid board");
            }
            this.cells = board;
        }
        int get(int x, int y) {
            return cells[y*SIZE*SIZE + x];
        }
        Board set(int x, int y, int val) {
            cells[y*SIZE*SIZE + x] = val;
            return this;
        }
        Board clear(int x, int y) {
            return set(x,y,Q);
        }

        public String toString() {
            StringBuilder out = new StringBuilder();
            for(int ix = 1; ix <= this.cells.length; ix++) {
                out.append(this.cells[ix-1]);
                out.append(" ");

                if ( ix % (SIZE*SIZE) == 0 ) {
                    out.append("\n");
                    if (ix % (SIZE*SIZE*SIZE) == 0) {
                        out.append("\n");
                    }
                } else if ( ix % SIZE == 0) {
                    out.append("   ");
                }
            }
            return out.toString();
        }
    }


    public static class Tests {
        // http://www.dailysudoku.com/sudoku/examples/classic.pdf
        @Test
        public void testOne() {
            Sudoku s = new Sudoku(new int[] {
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
            System.out.println(s);
        }

    }

}
