package rytim.kata;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class GridCutter {

    public static class Grid {
        final boolean[] points;
        final int size;
        public Grid(int size, boolean[] points) {
            this.size = size;
            this.points = points;
        }
        boolean at(int x, int y) {
            if (x < 0 || x >= size || y < 0 || y >= size) {
                throw new IllegalArgumentException("(x,y)=("+x+","+y+")");
            }
            return points[y * size + x];
        }

        Point begin;
        Point end;

        List<Point> points() {
            return IntStream.range(0, points.length)
                    .filter(coord -> points[coord])
                    .mapToObj(coord -> new Point(size, coord))
                    .collect(toList());
        }

        Line evenCut() {
            Line l = new Line();
            // can we do better than O(n^3)?
            for(Point a : points()) {
                l.a = a;
                for(Point b : points()) {
                    l.b = b;
                    long lhs = points().stream().filter(l::isLeftOf).count();
                    long rhs = points().stream().filter(l::isRightOf).count();
                    if (lhs == rhs) {
                        return l;
                    }
                }
            }
            throw new IllegalStateException("Impossible to even-cut");
        }

        public class Line {
            Point a;
            Point b;

            private double slope() {
                return (a.x - b.x) / (a.y - b.y);
            }
            private double b() {
                return a.y - slope()*a.x;
            }
            private double xAtY(int y) {
                return (y - b())/slope();
            }
            boolean isLeftOf(Point p) {
                return a != null && b != null && xAtY(p.y) <= p.x;
            }
            boolean isRightOf(Point p) {
                return a != null && b != null && xAtY(p.y) >= p.x;
            }
        }

        public class Point {
            final int x;
            final int y;
            public Point(int size, int coord) {
                this.y = coord / size;
                this.x = coord % size;
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
    }

    // TODO: tests
}
