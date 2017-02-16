package app.model;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

class PointsList {
    private List<Point> points = new ArrayList<>(1000) {{
        add(new Point(
                BigInteger.valueOf(0),
                0,
                BigInteger.valueOf(0))
        );
    }};

    void add(Point point) {
        points.add(point);
    }

    int size() {
        return points.size();
    }

    void computeSpeedForAllPoints() {
        for (Point point : points) {
            if (point.speed == null) {
                point.speed = point.capacity
                        .multiply(BigInteger.valueOf(1000))
                        .divide(BigInteger.valueOf(point.runtime));
            }
        }
    }

    class Point {
        private BigInteger capacity;
        private BigInteger speed;
        private long runtime;

        Point(BigInteger capacity, long runtime) {
            this.capacity = capacity;
            this.runtime = runtime;
        }

        Point(BigInteger capacity, long runtime, BigInteger speed) {
            this.capacity = capacity;
            this.runtime = runtime;
            this.speed = speed;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "capacity=" + capacity +
                    ", speed=" + speed +
                    ", runtime=" + runtime +
                    '}';
        }
    }
}
