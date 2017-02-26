package app.view.console;


import app.model.PointsList;
import app.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.util.Locale;

class PointsFileWriter {
    private final PointsList points;
    private final File dir;
    private File newFile;
    private static final String FILE_NAME = "profiling_points.txt";
    private static final String MS = "ms";
    private static final String KB = "KB";
    private static final String KB_S = KB + "/s";

    PointsFileWriter(PointsList points, File dir) {
        this.points = points;
        this.dir = dir;
    }

    void write() throws Exception {
        Locale loc = Locale.CANADA_FRENCH;
        this.newFile = new File(dir.toString() + File.separator + FILE_NAME);
        try (FileWriter writer = new FileWriter(newFile)) {
            String maxRuntime = Utils.formatNumber(points.getLast().getRuntime(), loc);
            String maxCapacity = Utils.formatNumber(points.getLast().getCapacity(), loc);
            String maxSpeed = Utils.formatNumber(points.getMaxSpeed(), loc);
            int blockLength = (
                    getWhitespaces("", maxRuntime) + getWhitespaces("", maxCapacity) + getWhitespaces("", maxSpeed)
            ).length();
            String bounds = getBounds(blockLength);
            writer.write(bounds + "|" + "\n");
            writer.write(MS + getWhitespaces(MS, maxRuntime) +
                    KB + getWhitespaces(KB, maxCapacity) +
                    KB_S + getWhitespaces(KB_S, maxSpeed) + "|" + "\n");
            writer.write(bounds + "|" + "\n");
            for (int i = 0; i < points.size(); i++) {
                String runtime = Utils.formatNumber(points.get(i).getRuntime(), loc);
                String capacity = Utils.formatNumber(points.get(i).getCapacity(), loc);
                String speed = Utils.formatNumber(points.get(i).getSpeed(), loc);
                writer.write(runtime + getWhitespaces(runtime, maxRuntime));
                writer.write(capacity + getWhitespaces(capacity, maxCapacity));
                writer.write(speed + getWhitespaces(speed, maxSpeed));
                writer.write("|");
                writer.write("\n");
            }
            writer.write(bounds + "#");
        }
    }

    private String getWhitespaces(String s, String maxNumberAsString) {
        int defaultSpacesCount = 4;
        int spacesCount = maxNumberAsString.length() - s.length() + defaultSpacesCount;
        StringBuilder stringWithSpaces = new StringBuilder();
        for (int i = 0; i < spacesCount; i++) {
            stringWithSpaces.append(" ");
        }
        return stringWithSpaces.toString();
    }

    private String getBounds(int length) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++) {
            s.append("-");
        }
        return s.toString();
    }

    File getNewFile() {
        return newFile;
    }
}
