package rytim.kata;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Function;

// https://leetcode.com/problems/judge-route-circle/description/
public class RobotCircle {

    static class Position {
        final int x;
        final int y;
        public Position() {
            this(0,0);
        }
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        boolean isOrigin() {
            return this.x == 0 && this.y == 0;
        }
        Position up() { return new Position(x,      y+1); }
        Position dn() { return new Position(x,      y-1); }
        Position lf() { return new Position(x-1,    y); }
        Position rt() { return new Position(x+1,    y); }
    }
    enum Dir {
        UP('U', Position::up),
        DOWN('D', Position::dn),
        LEFT('L', Position::lf),
        RIGHT('R', Position::rt);
        final int rep;
        final Function<Position,Position> mod;
        Dir(int rep, Function<Position,Position> mod) {
            this.rep = rep;
            this.mod = mod;
        }

        static Dir fromInt(int in) {
            for(Dir d : Dir.values()) {
                if (d.rep == in) {
                    return d;
                }
            }
            throw new IllegalArgumentException("" + (char) in);
        }
    }



    public boolean judgeCircle(String m) {
        final Position[] start = {new Position()};
        m.chars()
            .mapToObj(Dir::fromInt)
            // hmm..more functional way to do this?
            .forEach(d -> start[0] = d.mod.apply(start[0]));
        return start[0].isOrigin();
    }

    public static class Tests {
        RobotCircle rc = new RobotCircle();
        @Test
        public void judge() {
            Assert.assertEquals(true, rc.judgeCircle("UD"));
            Assert.assertEquals(true, rc.judgeCircle("DU"));
            Assert.assertEquals(true, rc.judgeCircle("DULR"));
            Assert.assertEquals(true, rc.judgeCircle("LRDU"));
            Assert.assertEquals(false, rc.judgeCircle("D"));
            Assert.assertEquals(false, rc.judgeCircle("DD"));
            Assert.assertEquals(false, rc.judgeCircle("DULRD"));
        }
    }
}
