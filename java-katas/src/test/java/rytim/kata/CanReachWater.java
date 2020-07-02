package rytim.kata;


import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;

/*
An area map is a WxH grid of int values that represent height above sea-level.
Each square is represented by an X,Y point value.
Beyond the edges of the grid lies an ocean (area with height=0).
Water flows down hill (from higher values to lower) between adjacent squares.

    (
    Two versions here - one where it can flow diagonal and one where it can't.
    Both get to same algo but diagonal is slightly harder
    )

Write a method to return the X,Y point values from where water could find the ocean.

Example 1:

    1   1   1
    1   0   1
    1   1   1

Returns only the edge points


Example 2:

    1   1   1
    1   5   1
    1   1   1

Returns all points


Example 3:

    1   1   1
    1   0   1
    1   5   1
    1   5   1

Returns all points except the "0" in the middle

*/
public class CanReachWater {

    final int[][] rows;

    final int maxY;
    final int maxX;

    // TODO: edge cases
    //   empty
    //   null
    //   array of empty arrays
    //   array of nulls
    //   different length inner rows
    public CanReachWater(int[][] rows) {
        this.rows = rows;
        this.maxY = rows.length - 1;
        this.maxX = rows[0].length - 1;
    }

    Stream<Point> canReach() {
        return IntStream.rangeClosed(0, maxY).mapToObj(Integer::new)
                .flatMap(row -> IntStream.rangeClosed(0, maxY)
                        .mapToObj(col -> new Tile(col, row)))
                .filter(Tile::canReachWater)
                .map(Tile::asPoint);
    }



    boolean valid(int x, int y) {
        return x >= 0 && x <= maxX &&
                y >= 0 && y <= maxY;
    }

    public static class Point {
        final int x;
        final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public String toString() {
            return "(" + this.x + "," + this.y + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (x != point.x) return false;
            return y == point.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    class Tile {
        final int x;
        final int y;

        int height() {
            return rows[y][x];
        }

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }

        boolean valid() {
            return CanReachWater.this.valid(this.x, this.y);
        }

        Point asPoint() {
            return new Point(this.x, this.y);
        }

        Tile n() {
            return new Tile(x, y - 1);
        }

        Tile s() {
            return new Tile(x, y + 1);
        }

        Tile e() {
            return new Tile(x + 1, y);
        }

        Tile w() {
            return new Tile(x - 1, y);
        }

        Stream<Tile> neighbors() {
            return Stream.of(
                    n(), s(), e(), w()
            ).filter(Tile::valid);
        }

        boolean isEdge() {
            return x == 0 || y == 0 ||
                    x == maxX || y == maxY;
        }

        Status status = Status.UNKNOWN;

        boolean canReachWater() {
            if (status == Status.UNKNOWN) {
                this.status = explore() ? Status.CAN_REACH : Status.CANNOT_REACH;
            }
            return status == Status.CAN_REACH;
        }

        private boolean explore() {
            return isEdge() ||
                    neighbors()
                            .filter(t -> t.height() <= this.height())
                            .filter(t -> t.canReachWater())
                            .anyMatch(t -> true);
        }
    }

    private enum Status {
        UNKNOWN,
        CAN_REACH,
        CANNOT_REACH
    }


    public static class Tests {
        @Test
        public void example1() {
            CanReachWater w = new CanReachWater(new int[][] {
                    { 1, 1, 1 },
                    { 1, 0, 1 },
                    { 1, 1, 1 }
            });
            List<Point> r = w.canReach().collect(toList());
            Assert.assertThat(r, hasItem(new Point(0,0)));
            Assert.assertThat(r, not(hasItem(new Point(1,1))));
        }
    }
}
