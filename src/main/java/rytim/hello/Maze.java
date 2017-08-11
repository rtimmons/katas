package rytim.hello;

import java.util.Stack;

public class Maze {

    final int id;

    public Maze(int id) {
        this.id = id;
    }

    boolean exit;
    Maze up,
            down,
            left,
            right;

    enum Dir {
        ENTER,
        L, R, U, D,
        EXIT,
    }

    static class Builder {
        final Maze root;
        final Stack<Maze> path;
        public Builder() {
            this.root = new Maze(0);
            this.path = new Stack<Maze>();
            this.path.push(root);
        }

        Maze build() {
            return this.root;
        }

        int seed = 100;

        Builder exits() {
            path.peek().exit = true;
            return this;
        }
        Builder up() {
            Maze n = new Maze(seed++);
            path.peek().up = n;
            path.push(n);
            return this;
        }
        Builder down() {
            Maze n = new Maze(seed++);
            path.peek().down = n;
            path.push(n);
            return this;
        }
        Builder left() {
            Maze n = new Maze(seed++);
            path.peek().left = n;
            path.push(n);
            return this;
        }
        Builder right() {
            Maze n = new Maze(seed++);
            path.peek().right = n;
            path.push(n);
            return this;
        }
        Builder or() {
            path.pop();
            return this;
        }
    }

    static class Move {
        final Dir dir;
        final Maze maz;

        public Move(Dir dir, Maze maz) {
            this.dir = dir;
            this.maz = maz;
        }

        public String toString() {
            return dir.toString();
        }
    }

    Stack<Move> exit() {
        Stack<Move> s = new Stack<Move>();
        s.push(new Move(Dir.ENTER, this));
        if (exit(s)) {
            return s;
        } else {
            throw new IllegalStateException("Inescapable");
        }
    }

    static boolean explore(Maze into, Dir dir, Stack<Move> path) {
        if (into != null) {
            path.push(new Move(dir, into));
            if (exit(path)) {
                return true;
            }
            path.pop();
        }
        return false;
    }

    static boolean exit(Stack<Move> path) {
        Move move = path.peek();

        if (move.maz.exit) {
            path.push(new Move(Dir.EXIT,null));
            return true;
        }

        Maze head = move.maz;
        return explore(head.left, Dir.L, path) ||
                explore(head.right, Dir.R, path) ||
                explore(head.up, Dir.U, path) ||
                explore(head.down, Dir.D, path);
    }

    public static void main(String...args) {
        Maze m = new Maze.Builder()
                .down().left()
                       .or()
                       .right().right()
                               .or()
                               .left()
                               .or()
                        .or()

                .or()
                .up().down().left().right().left().right().up().exits()
                .build();
        Stack<Move> path = m.exit();
        while(!path.empty()) {
            System.out.println(path.pop());
        }
    }
}
