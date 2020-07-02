package rytim.kata;

/*
Determine a valid location to put N queens on an
NxN chess-board such that none of the queens can
kill any of the other queens.

Queens can move at 90 and 45 degrees.

Some values of N have no solution (1,2,3,4(?)) but
others have multiple.

Simple problem: find one solution
Slightly harder: find all solutions
*/

import org.junit.Test;

public class NQueens {

    public static class Board {
        final boolean[] board; // true => has queen
        final int n;
        final int n2;
        Board(int n) {
            this.n = n;
            this.n2 = n*n; // convenience
            this.board = new boolean[n2];
        }
        // static can be optimized
        private static int ix(int n, int row, int col) {
            return row*n + col;
        }
        boolean has(int row, int col) {
            return board[ix(n,row,col)];
        }
        void place(int row, int col) {
            board[ix(n,row,col)] = true;
        }
        void remove(int row, int col) {
            board[ix(n,row,col)] = false;
        }

        private boolean validRow(final int row) {
            boolean seen = false;
            for(int col=0; col<n; col++) {
                boolean q = has(row, col);
                if (q) {
                    if (seen) {
                        return false;
                    }
                    seen = true;
                }
            }
            return true;
        }
        // refactor with above?
        private boolean validColumn(final int col) {
            boolean seen = false;
            for(int row=0; row<n; row++) {
                boolean q = has(row, col);
                if (q) {
                    if (seen) {
                        return false;
                    }
                    seen = true;
                }
            }
            return true;
        }
        // TODO: refactor above to use this
        // TODO: there's a subtle bug here - not finding
        //       queens at {(x,y)}={(2,1),(3,2)}
        private boolean validDirection(int topX, int topY, int dx, int dy) {
            boolean seen = false;
            for(int row=topY; row<n && row >= 0; row+=dy) {
                int col = topX+(topY-row)*dx;
                if (col < 0 || col >= n) {
                    continue;
                }
//                if (col < 0 || col >= n) {
//                    throw new IllegalStateException(String.format(
//                            "Invalid col=%s on row=%s for topX=%s topY=%s dx=%s dy=%s",
//                            col, row, topX, topY, dx, dy
//                    ));
//                }
                boolean q = has(row,col);
                if (q) {
                    if (seen) {
                        return false;
                    }
                    seen = true;
                }
            }
            return true;
        }
        boolean valid(int row, int col) {
            boolean rowV = validRow(row);
            boolean colV = validColumn(col);
            boolean upright     = validDirection(row, col,  1, -1);
            boolean upleft      = validDirection(row, col, -1, -1);
            boolean downright   = validDirection(row, col,  1,  1);
            boolean downleft    = validDirection(row, col, -1,  1);
            return  rowV &&
                    colV &&
                    upright &&
                    upleft &&
                    downright &&
                    downleft;
        }

        public String toString() {
            StringBuilder out = new StringBuilder();
            for(int row=0; row<n; row++) {
                for(int col=0; col<n; col++) {
                    boolean q = has(row,col);
                    out.append(q ? "♕" : "🀆");
                }
                out.append("\n");
            }
            return out.toString();
        }
    }

    void solve() {
        if(!explore(n)) {
            throw new IllegalArgumentException("No solution found");
        }
    }

    final Board board;
    final int n;
    public NQueens(int n) {
        this.n = n;
        this.board = new Board(n);
    }

    boolean explore(int remain) {
        if (remain <= 0) {
            return true;
        }
        for(int row=0; row<n; row++) {
            for(int col=0; col<n; col++) {
                // could probably clean up
                // has+place+valid to be
                // single operation or something
                if (board.has(row,col)) {
                    continue;
                }
                board.place(row, col);
                if (board.valid(row, col) && explore(remain-1)) {
                    return true;
                }
                board.remove(row, col);
            }
        }
        return false;
    }

    public static class Tests {
        @Test
        public void test4x4() {
            NQueens nq = new NQueens(4);
            nq.solve();
            System.out.println(nq.board);
        }
    }

}
