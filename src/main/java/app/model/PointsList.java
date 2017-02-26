package app.model;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PointsList {
    private List<Point> points = new ArrayList<>(1000);

    void add(Point point) {
        points.add(point);
    }

    void clear() {
        points.clear();
        add(new Point(
                BigInteger.valueOf(0),
                0,
                BigInteger.valueOf(0))
        );
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

    public int size() {
        return points.size();
    }

    public Point get(int index) {
        return points.get(index);
    }

    public Point getLast() {
        return points.get(size() - 1);
    }

    public Point getPointWithMaxSpeed() {
        return points.stream()
                .max((p1, p2) -> p1.getSpeed().compareTo(p2.getSpeed()))
                .get();
    }

    public BigInteger getMaxSpeed() {
        return getPointWithMaxSpeed().getSpeed();
    }

    public long getAverageCapacityForOneMs() {
        Point last = getLast();
        return last.capacity.divide(
                BigInteger.valueOf(last.runtime)
        ).longValue();
    }

    public long getMaxIncrementRuntime() {
        long maxIncrement = 0;
        for (int i = 0; i < size() - 1; i++) {
            long currentIncrement = get(i + 1).getRuntime() - get(i).getRuntime();
            if (currentIncrement > maxIncrement) {
                maxIncrement = currentIncrement;
            }
        }
        return maxIncrement;
    }

    public long getMaxIncrementCapacity() {
        long maxIncrement = 0;
        for (int i = 0; i < size() - 1; i++) {
            long currentIncrement = get(i + 1).getCapacity().subtract(get(i).getCapacity()).longValue();
            if (currentIncrement > maxIncrement) {
                maxIncrement = currentIncrement;
            }
        }
        return maxIncrement;
    }

    public long getMaxIncrementSpeed() {
        long maxIncrement = Long.MIN_VALUE;
        for (int i = 0; i < size() - 1; i++) {
            long currentIncrement = Math.abs(get(i + 1).getSpeed().subtract(get(i).getSpeed()).longValue());
            if (currentIncrement > maxIncrement) {
                maxIncrement = currentIncrement;
            }
        }
        return maxIncrement;
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
