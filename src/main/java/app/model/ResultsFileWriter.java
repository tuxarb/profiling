package app.model;

import app.model.beans.Characteristic;
import app.utils.Log;
import app.utils.Utils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

class ResultsFileWriter {
    private Characteristic ch;
    private File directory;
    private static final Logger LOG = Log.createLog(ResultsFileWriter.class);

    ResultsFileWriter(Characteristic characteristic, String pathToUserFolder) {
        this.ch = characteristic;
        this.directory = createDir(pathToUserFolder);
    }

    void write() throws IOException {
        File file = new File(getPathToDir() + File.separator + "results.txt");
        LOG.info(Log.PREPARATION_FOR_WRITING_IN_THE_FILE);

        String taskName = ch.getTaskName();
        LOG.info(Log.WRITING_IN_THE_FILE);
        try (java.io.FileWriter fileWriter = new java.io.FileWriter(file, true)) {
            fileWriter.write("\t  The results program:\n\n");
            fileWriter.write("------------------------------------------\n");
            if (!taskName.isEmpty())
                fileWriter.write("\t\t" + taskName + "\n");
            fileWriter.write("------------------------------------------\n");
            fileWriter.write("Runtime: \t" + ch.getRuntime() + "\n");
            fileWriter.write("Capacity:\t" + ch.getCapacity() + "\n");
            fileWriter.write("Speed:   \t" + ch.getSpeed() + "\n");
            fileWriter.write("------------------------------------------\n");
            fileWriter.write("\t\t" + Utils.getCurrentDate() + "\n");
            fileWriter.write("------------------------------------------\n\n");
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

