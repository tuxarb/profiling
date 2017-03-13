package app.model;

import app.model.beans.Characteristic;
import app.utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DetailedTestResultsCalculator {
    private final List<TempResultKeeper> results;
    private Model model;

    public DetailedTestResultsCalculator(Model model) {
        this.model = model;
        this.results = new ArrayList<>(model.getNumberTests());
    }

    public void saveResultForTest(Characteristic ch) {
        long runtime = Utils.getNumberFromString(ch.getRuntime());
        long capacity = Utils.getNumberFromString(ch.getCapacity());
        results.add(new TempResultKeeper(runtime, capacity));
    }

    public void computeAndSaveAverageResult() {
        discardAnomalousResults();
        saveNewResultingData();
    }

    private void discardAnomalousResults() {
        int size = getIterationsNumber();
        if (size < 0) {
            return;
        }
        for (int i = 0; i <= size; i++) {
            long maxRuntime = results.stream()
                    .map(TempResultKeeper::getRuntime)
                    .max(Comparator.comparing(Long::valueOf))
                    .get();
            long minRuntime = results.stream()
                    .map(TempResultKeeper::getRuntime)
                    .min(Comparator.comparing(Long::valueOf))
                    .get();
            results.remove(
                    results.stream()
                            .filter(o -> o.getRuntime() == maxRuntime)
                            .findFirst()
                            .get()
            );
            results.remove(
                    results.stream()
                            .filter(o -> o.getRuntime() == minRuntime)
                            .findFirst()
                            .get()
            );
        }
    }

    private int getIterationsNumber() {
        int size = results.size();
        if (size == 2) {
            return -1;
        } else if (size >= 3 && size <= 7) {
            return 0;
        }
        int divider = 8;
        int i = (int) ((double) (size / divider) + 0.05);
        if (size % divider > (int) ((double) (divider / 2) + 0.05)) {
            i++;
        }
        return i;
    }

    private void saveNewResultingData() {
        long runtime = results.stream()
                .map(TempResultKeeper::getRuntime)
                .collect(Collectors.averagingLong(r -> r))
                .longValue();
        double capacity = results.stream()
                .map(TempResultKeeper::getCapacity)
                .collect(Collectors.averagingDouble(c -> c));
        long speed = (long) (1000 * capacity / runtime);
        model.saveResultingData((long) capacity, speed, runtime);
        saveNewPoints(runtime, capacity);
    }

    private void saveNewPoints(long averageRuntime, double averageCapacity) {
        TempResultKeeper optimalResult;
        int increment = 10;
        int delta = 0;
        int curIterationsNumber = 0;
        int maxIterationsNumber = 25;
        L:
        while (true) {
            delta += increment;
            for (TempResultKeeper curResultKeeper : results) {
                long runtime = curResultKeeper.runtime;
                long capacity = curResultKeeper.capacity;
                if (capacity >= (long) (1.1 * averageCapacity) ||
                        capacity <= (long) (0.9 * averageCapacity)) {
                    continue;
                }
                if ((runtime <= averageRuntime && runtime >= averageRuntime - delta) ||
                        (runtime > averageRuntime && runtime <= averageRuntime + delta)) {
                    optimalResult = curResultKeeper;
                    break L;
                }
            }
            if (curIterationsNumber == maxIterationsNumber ||
                    (averageRuntime + delta >= 1.1 * averageRuntime && averageRuntime > 600)) {
                optimalResult = results.stream()
                        .sorted(Comparator.comparing(TempResultKeeper::getRuntime))
                        .collect(Collectors.toList())
                        .get(results.size() / 2);
                break;
            }
            curIterationsNumber++;
        }
        model.setPoints(optimalResult.points);
    }

    private class TempResultKeeper {
        private final long runtime;
        private final long capacity;
        private final PointsList points;

        private TempResultKeeper(long runtime, long capacity) {
            this.runtime = runtime;
            this.capacity = capacity;
            this.points = new PointsList(model.getPoints());
        }

        private long getRuntime() {
            return runtime;
        }

        private long getCapacity() {
            return capacity;
        }
    }
}