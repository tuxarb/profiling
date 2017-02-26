package app.view.console;


import app.model.PointsList;
import app.utils.Log;
import app.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.Locale;

class PointsFileWriter {
    private final PointsList points;
    private final File dir;
    private File newFile;
    private static final String FILE_NAME = "profiling_points.txt";
    private static final String MS = "ms";
    private static final String KB = "KB";
    private static final String KB_S = KB + "/s";
    private static final String INCREMENT_MS = "inc_ms";
    private static final String INCREMENT_KB = "inc_KB";
    private static final String INCREMENT_KB_S = "inc_" + KB + "/s";

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
            String maxIncrementRuntime = Utils.formatNumber(points.getMaxIncrementRuntime(), loc);
            String maxIncrementCapacity = Utils.formatNumber(points.getMaxIncrementCapacity(), loc);
            String maxIncrementSpeed = Utils.formatNumber(points.getMaxIncrementSpeed(), loc);
            int blockLength = (
                    getWhitespaces("", maxRuntime) + getWhitespaces("", maxCapacity) +
                            getWhitespaces("", maxSpeed) + getWhitespaces("", maxIncrementRuntime) +
                            getWhitespaces("", maxIncrementCapacity) + getWhitespaces("", maxIncrementSpeed)
            ).length();
            writer.write(getBounds(Log.POINTS_FILE_WRITER_MESSAGE.length(), "*"));
            writer.write("\n");
            writer.write("<-----" + Log.POINTS_FILE_WRITER_MESSAGE + "----->\n");
            writer.write(getBounds(Log.POINTS_FILE_WRITER_MESSAGE.length(), "*"));
            writer.write("\n");
            String bounds = getBounds(blockLength, "-");
            writer.write(bounds + "#" + "\n");
            writer.write(
                    MS + getWhitespaces(MS, maxRuntime) +
                            KB + getWhitespaces(KB, maxCapacity) +
                            KB_S + getWhitespaces(KB_S, maxSpeed) +
                            INCREMENT_MS + getWhitespaces(INCREMENT_MS, maxIncrementRuntime) +
                            INCREMENT_KB + getWhitespaces(INCREMENT_KB, maxIncrementCapacity) +
                            INCREMENT_KB_S + getWhitespaces(INCREMENT_KB_S, maxIncrementSpeed) + "|" + "\n"
            );
            writer.write(bounds + "|" + "\n");
            long prevRuntime = points.get(0).getRuntime();
            BigInteger prevCapacity = points.get(0).getCapacity();
            BigInteger prevSpeed = points.get(0).getSpeed();
            for (int i = 0; i < points.size(); i++) {
                long curRuntime = points.get(i).getRuntime();
                BigInteger curCapacity = points.get(i).getCapacity();
                BigInteger curSpeed = points.get(i).getSpeed();
                String curRuntimeAsStr = Utils.formatNumber(curRuntime, loc);
                String curCapacityAsStr = Utils.formatNumber(curCapacity, loc);
                String curSpeedAsStr = Utils.formatNumber(curSpeed, loc);
                writer.write(curRuntimeAsStr + getWhitespaces(curRuntimeAsStr, maxRuntime));
                writer.write(curCapacityAsStr + getWhitespaces(curCapacityAsStr, maxCapacity));
                writer.write(curSpeedAsStr + getWhitespaces(curSpeedAsStr, maxSpeed));
                long incrementRuntime = curRuntime - prevRuntime;
                long incrementCapacity = curCapacity.subtract(prevCapacity).longValue();
                long incrementSpeed = curSpeed.subtract(prevSpeed).longValue();
                String incrementRuntimeTimeAsStr = Utils.formatNumber(incrementRuntime, loc);
                String incrementCapacityAsStr = Utils.formatNumber(incrementCapacity, loc);
                String incrementSpeedAsStr = Utils.formatNumber(incrementSpeed, loc);
                writer.write(
                        incrementRuntimeTimeAsStr + getWhitespaces(incrementRuntimeTimeAsStr, maxIncrementRuntime)
                );
                writer.write(incrementCapacityAsStr + getWhitespaces(incrementCapacityAsStr, maxIncrementCapacity));
                writer.write(incrementSpeedAsStr + getWhitespaces(incrementSpeedAsStr, maxIncrementSpeed));
                writer.write("|");
                writer.write("\n");
                prevRuntime = curRuntime;
                prevCapacity = curCapacity;
                prevSpeed = curSpeed;
            }
            writer.write(bounds + "#");
        }
    }

    private String getWhitespaces(String s, String maxNumberAsString) {
        int defaultSpacesCount = 5;
        int spacesCount = maxNumberAsString.length() - s.length() + defaultSpacesCount;
        StringBuilder stringWithSpaces = new StringBuilder();
        for (int i = 0; i < spacesCount; i++) {
            stringWithSpaces.append(" ");
        }
        return stringWithSpaces.toString();
    }

    private String getBounds(int length, String symb) {
        StringBuilder s = new StringBuilder();
        if ("*".equals(symb)) {
            for (int i = 0; i < 6; i++) {
                s.append(" ");
            }
        }
        for (int i = 0; i < length; i++) {
            s.append(symb);
        }
        return s.toString();
    }

    File getNewFile() {
        return newFile;
    }
}
