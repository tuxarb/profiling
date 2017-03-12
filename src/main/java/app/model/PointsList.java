package app.model;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PointsList {
    private List<Point> points;

    PointsList(PointsList points) {
        this.points = new ArrayList<>(points.get());
    }

    PointsList() {
        points = new ArrayList<>(500);
    }

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

    private List<Point> get() {
        return points;
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
                .max(Comparator.comparing(Point::getSpeed))
                .get();
    }

    public BigInteger getMaxSpeed() {
        return getPointWithMaxSpeed().getSpeed();
    }

    public double getAverageCapacityForOneMs() {
        double capacitySumForOneMs = 0;
        for (int i = 0; i < size() - 1; i++) {
            long incrementCapacity = get(i + 1).getCapacity().subtract(get(i).getCapacity()).longValue();
            long incrementRuntime = get(i + 1).getRuntime() - get(i).getRuntime();
            capacitySumForOneMs += (double) (incrementCapacity / incrementRuntime);
        }
        return capacitySumForOneMs / (size() - 1);
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
