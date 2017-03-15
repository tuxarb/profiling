package app.model;


import app.utils.Log;
import app.utils.Utils;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.Locale;

import static app.utils.Utils.LF;

public class PointsFileWriter {
    private final PointsList points;
    private final File userPath;
    private static final Locale LOC = Locale.CANADA_FRENCH;
    private static final String DIR_NAME = "profiling_points";
    private static final String FILE_NAME_MAIN = "main.txt";
    private static final String FILE_NAME_RUNTIME_CAPACITY = "runtime_capacity.csv";
    private static final String FILE_NAME_RUNTIME_SPEED = "runtime_speed.csv";
    private static final String FILE_NAME_INC_RUNTIME_CAPACITY = "increment_runtime_capacity.csv";
    private static final String FILE_NAME_INC_RUNTIME_SPEED = "increment_runtime_speed.csv";
    private static final String MS = "ms";
    private static final String KB = "KB";
    private static final String KB_S = KB + "/s";
    private static final String INCREMENT_MS = "inc_ms";
    private static final String INCREMENT_KB = "inc_KB";
    private static final String INCREMENT_KB_S = "inc_" + KB + "/s";
    private static final Logger LOG = Log.createLog(PointsFileWriter.class);


    public PointsFileWriter(PointsList points, File userPath) {
        this.points = points;
        this.userPath = userPath;
    }

    public void write() throws Exception {
        File newDir = createDir();
        if (newDir == null) {
            throw new Exception(Log.CREATING_DIR_ERROR);
        }
        createAndWriteMainFile(newDir);
        createAndWriteRuntimeCapacityFile(newDir);
        createAndWriteRuntimeSpeedFile(newDir);
        createAndWriteIncrementRuntimeCapacityFile(newDir);
        createAndWriteIncrementRuntimeSpeedFile(newDir);
    }

    private File createDir() {
        File newDir = new File(userPath + File.separator + DIR_NAME);
        if (!newDir.exists()) {
            if (newDir.mkdir()) {
                LOG.debug(Log.DIR_WAS_CREATED, newDir.toString());
            }
        }
        return newDir.exists() ? newDir : null;
    }

    private void createAndWriteMainFile(File dir) throws Exception {
        File newFile = new File(dir + File.separator + FILE_NAME_MAIN);
        try (FileWriter writer = new FileWriter(newFile)) {
            String maxRuntime = Utils.formatNumber(points.getLast().getRuntime(), LOC);
            String maxCapacity = Utils.formatNumber(points.getLast().getCapacity(), LOC);
            String maxSpeed = Utils.formatNumber(points.getMaxSpeed(), LOC);
            String maxIncrementRuntime = Utils.formatNumber(points.getMaxIncrementRuntime(), LOC);
            String maxIncrementCapacity = Utils.formatNumber(points.getMaxIncrementCapacity(), LOC);
            String maxIncrementSpeed = Utils.formatNumber(points.getMaxIncrementSpeed(), LOC);
            int blockLength = (
                    getWhitespaces("", maxRuntime) + getWhitespaces("", maxCapacity) +
                            getWhitespaces("", maxSpeed) + getWhitespaces("", maxIncrementRuntime) +
                            getWhitespaces("", maxIncrementCapacity) + getWhitespaces("", maxIncrementSpeed)
            ).length();
            writer.write(getBounds(Log.POINTS_FILE_WRITER_MESSAGE.length(), "*"));
            writer.write(LF);
            writer.write("<-----" + Log.POINTS_FILE_WRITER_MESSAGE + "----->");
            writer.write(LF);
            writer.write(Log.FILE_OUTPUT_INFO);
            writer.write(LF);
            writer.write(getBounds(Log.POINTS_FILE_WRITER_MESSAGE.length(), "*"));
            writer.write(LF + LF);
            String bounds = getBounds(blockLength, "-");
            writer.write(bounds + "#" + LF);
            writer.write(
                    MS + getWhitespaces(MS, maxRuntime) +
                            KB + getWhitespaces(KB, maxCapacity) +
                            KB_S + getWhitespaces(KB_S, maxSpeed) +
                            INCREMENT_MS + getWhitespaces(INCREMENT_MS, maxIncrementRuntime) +
                            INCREMENT_KB + getWhitespaces(INCREMENT_KB, maxIncrementCapacity) +
                            INCREMENT_KB_S + getWhitespaces(INCREMENT_KB_S, maxIncrementSpeed) + "|"
            );
            writer.write(LF);
            writer.write(bounds + "|" + LF);
            long prevRuntime = points.get(0).getRuntime();
            BigInteger prevCapacity = points.get(0).getCapacity();
            BigInteger prevSpeed = points.get(0).getSpeed();
            for (int i = 0; i < points.size(); i++) {
                long curRuntime = points.get(i).getRuntime();
                BigInteger curCapacity = points.get(i).getCapacity();
                BigInteger curSpeed = points.get(i).getSpeed();
                String curRuntimeAsStr = Utils.formatNumber(curRuntime, LOC);
                String curCapacityAsStr = Utils.formatNumber(curCapacity, LOC);
                String curSpeedAsStr = Utils.formatNumber(curSpeed, LOC);
                writer.write(curRuntimeAsStr + getWhitespaces(curRuntimeAsStr, maxRuntime));
                writer.write(curCapacityAsStr + getWhitespaces(curCapacityAsStr, maxCapacity));
                writer.write(curSpeedAsStr + getWhitespaces(curSpeedAsStr, maxSpeed));
                long incrementRuntime = curRuntime - prevRuntime;
                long incrementCapacity = curCapacity.subtract(prevCapacity).longValue();
                long incrementSpeed = curSpeed.subtract(prevSpeed).longValue();
                String incrementRuntimeTimeAsStr = Utils.formatNumber(incrementRuntime, LOC);
                String incrementCapacityAsStr = Utils.formatNumber(incrementCapacity, LOC);
                String incrementSpeedAsStr = Utils.formatNumber(incrementSpeed, LOC);
                writer.write(
                        incrementRuntimeTimeAsStr + getWhitespaces(incrementRuntimeTimeAsStr, maxIncrementRuntime)
                );
                writer.write(incrementCapacityAsStr + getWhitespaces(incrementCapacityAsStr, maxIncrementCapacity));
                writer.write(incrementSpeedAsStr + getWhitespaces(incrementSpeedAsStr, maxIncrementSpeed));
                writer.write("|");
                writer.write(LF);
                if (i != points.size() - 1) {
                    writer.write(LF);
                }
                prevRuntime = curRuntime;
                prevCapacity = curCapacity;
                prevSpeed = curSpeed;
            }
            writer.write(bounds + "#");
        }
        logFileCreation(newFile);
    }

    private void createAndWriteRuntimeCapacityFile(File dir) throws Exception {
        File newFile = new File(dir + File.separator + FILE_NAME_RUNTIME_CAPACITY);
        try (FileWriter writer = new FileWriter(newFile)) {
            for (int i = 0; i < points.size(); i++) {
                writer.write(String.valueOf(points.get(i).getRuntime()));
                writer.write(";");
                writer.write(String.valueOf(points.get(i).getCapacity()));
                if (i != points.size() - 1) {
                    writer.write(LF);
                }
            }
        }
        logFileCreation(newFile);
    }

    private void createAndWriteRuntimeSpeedFile(File dir) throws Exception {
        File newFile = new File(dir + File.separator + FILE_NAME_RUNTIME_SPEED);
        try (FileWriter writer = new FileWriter(newFile)) {
            for (int i = 0; i < points.size(); i++) {
                writer.write(String.valueOf(points.get(i).getRuntime()));
                writer.write(";");
                writer.write(String.valueOf(points.get(i).getSpeed()));
                if (i != points.size() - 1) {
                    writer.write(LF);
                }
            }
        }
        logFileCreation(newFile);
    }

    private void createAndWriteIncrementRuntimeCapacityFile(File dir) throws Exception {
        File newFile = new File(dir + File.separator + FILE_NAME_INC_RUNTIME_CAPACITY);
        try (FileWriter writer = new FileWriter(newFile)) {
            long prevRuntime = points.get(0).getRuntime();
            BigInteger prevCapacity = points.get(0).getCapacity();
            for (int i = 0; i < points.size(); i++) {
                long curRuntime = points.get(i).getRuntime();
                BigInteger curCapacity = points.get(i).getCapacity();
                writer.write(String.valueOf(curRuntime - prevRuntime));
                writer.write(";");
                writer.write(String.valueOf(curCapacity.subtract(prevCapacity)));
                if (i != points.size() - 1) {
                    writer.write(LF);
                }
                prevRuntime = curRuntime;
                prevCapacity = curCapacity;
            }
        }
        logFileCreation(newFile);
    }

    private void createAndWriteIncrementRuntimeSpeedFile(File dir) throws Exception {
        File newFile = new File(dir + File.separator + FILE_NAME_INC_RUNTIME_SPEED);
        try (FileWriter writer = new FileWriter(newFile)) {
            long prevRuntime = points.get(0).getRuntime();
            BigInteger prevSpeed = points.get(0).getSpeed();
            for (int i = 0; i < points.size(); i++) {
                long curRuntime = points.get(i).getRuntime();
                BigInteger curSpeed = points.get(i).getSpeed();
                writer.write(String.valueOf(curRuntime - prevRuntime));
                writer.write(";");
                writer.write(String.valueOf(curSpeed.subtract(prevSpeed)));
                if (i != points.size() - 1) {
                    writer.write(LF);
                }
                prevRuntime = curRuntime;
                prevSpeed = curSpeed;
            }
        }
        logFileCreation(newFile);
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

    private void logFileCreation(File path) {
        LOG.debug(Log.FILE_WAS_CREATED, path.toString());
    }
}
