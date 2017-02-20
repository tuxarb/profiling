package app.model;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PointsList {
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

    public int size() {
        return points.size();
    }

    public Point get(int index) {
        return points.get(index);
    }

    public Point getLast() {
        return points.get(size() - 1);
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

    public class Point {
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

        public BigInteger getCapacity() {
            return capacity;
        }

        public BigInteger getSpeed() {
            return speed;
        }

        public long getRuntime() {
            return runtime;
        }
    }
}
