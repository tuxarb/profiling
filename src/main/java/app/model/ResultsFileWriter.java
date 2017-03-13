package app.model;

import app.model.beans.Characteristic;
import app.utils.Log;
import app.utils.Utils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

import static app.utils.Utils.LF;

class ResultsFileWriter {
    private final Characteristic ch;
    private final File directory;
    private final int TESTS_NUMBER;
    private static final Logger LOG = Log.createLog(ResultsFileWriter.class);

    ResultsFileWriter(Model model, String pathToUserFolder) {
        this.ch = model.getCharacteristic();
        this.directory = createDir(pathToUserFolder);
        this.TESTS_NUMBER = model.isDetailedTest() ? model.getNumberTests() : 1;
    }

    void write() throws IOException {
        File file = new File(getPathToDir() + File.separator + "results.txt");
        LOG.info(Log.PREPARATION_FOR_WRITING_IN_THE_FILE);

        String taskName = ch.getTaskName();
        LOG.info(Log.WRITING_IN_THE_FILE);
        try (java.io.FileWriter fileWriter = new java.io.FileWriter(file, true)) {
            fileWriter.write("\t  The results program:");
            fileWriter.write(LF + LF);
            fileWriter.write("------------------------------------------" + LF);
            if (!taskName.isEmpty()) {
                fileWriter.write("\t\t" + taskName);
                fileWriter.write(LF);
            }
            fileWriter.write("------------------------------------------" + LF);
            fileWriter.write("******************************************" + LF);
            fileWriter.write("Runtime: \t" + ch.getRuntime());
            fileWriter.write(LF);
            fileWriter.write("Capacity:\t" + ch.getCapacity());
            fileWriter.write(LF);
            fileWriter.write("Speed:   \t" + ch.getSpeed());
            fileWriter.write(LF);
            fileWriter.write("******************************************" + LF);
            fileWriter.write("The number of tests:  " + TESTS_NUMBER + LF);
            fileWriter.write("------------------------------------------" + LF);
            fileWriter.write("\t\t" + Utils.getCurrentDate());
            fileWriter.write(LF);
            fileWriter.write("------------------------------------------");
            fileWriter.write(LF + LF);
            if (directory == null || directory.listFiles().length == 0) {
                throw new IOException(Log.CREATING_FILE_ERROR);
            }
            LOG.info(Log.WRITING_IN_THE_FILE_SUCCESS + "[ " + file.getAbsolutePath().trim() + " ]");
        } catch (IOException e) {
            LOG.error(Log.WRITING_IN_FILE_ERROR);
            LOG.error(Log.CAUSE + e.getMessage());
            throw e;
        }
    }

    private File createDir(String pathToUserFolder) {
        String pathToFolder;
        if (!pathToUserFolder.isEmpty()) {
            pathToFolder = pathToUserFolder;
        } else {
            pathToFolder = System.getProperty("user.home");
        }
        LOG.info(Log.CREATING_DIR_FOR_WRITING);
        File dir = new File(pathToFolder + File.separator + ".profiling_results");
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.exists() ? dir : null;
    }

    private String getPathToDir() throws IOException {
        if (directory == null) {
            LOG.error(Log.CREATING_DIR_ERROR);
            throw new IOException();
        }
        return directory.getCanonicalPath();
    }
}

