package app.model;

import app.model.beans.Characteristic;
import app.utils.Log;
import app.utils.Utils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

class FileWriter {
    private Characteristic ch;
    private File directory;
    private static final Logger LOG = Log.createLog(FileWriter.class);

    FileWriter(Characteristic characteristic, String pathToUserFolder) {
        this.ch = characteristic;
        this.directory = createDir(pathToUserFolder);
    }

    void write() throws IOException {
        LOG.info(Log.PREPARATION_FOR_WRITING_IN_THE_FILE);
        File file = new File(getPathToDir() + File.separator + "results.txt");

        String taskName = ch.getTaskName();
        LOG.info(Log.WRITING_IN_THE_FILE);
        try (java.io.FileWriter fileWriter = new java.io.FileWriter(file, true)) {
            fileWriter.write("\t  Results for your program:\n\n");
            fileWriter.write("------------------------------------------\n");
            if (!taskName.isEmpty())
                fileWriter.write("\t\t\t" + taskName + "\n");
            fileWriter.write("------------------------------------------\n");
            fileWriter.write("Runtime:\t" + ch.getRuntime() + "\n");
            fileWriter.write("Capacity:\t" + ch.getCapacity() + "\n");
            fileWriter.write("Speed:\t\t" + ch.getSpeed() + "\n");
            fileWriter.write("------------------------------------------\n");
            fileWriter.write("\t\t\t" + Utils.getCurrentDate() + "\n");
            fileWriter.write("------------------------------------------\n\n");
            if (directory == null || directory.listFiles().length == 0) {
                LOG.error(Log.CREATING_FILE_ERROR);
                throw new IOException();
            }
            LOG.info(Log.WRITING_IN_THE_FILE_SUCCESS + "[ " + file.getAbsolutePath().trim() + " ]");
        } catch (IOException e) {
            LOG.error(Log.WRITING_IN_FILE_ERROR);
            throw e;
        }
    }

    private File createDir(String pathToUserFolder) {
        String pathToFolder;
        if (!pathToUserFolder.isEmpty()) {
            pathToFolder = pathToUserFolder;
        } else {
            pathToFolder = System.getProperty("user.dir");
        }
        LOG.info(Log.CREATING_DIR_FOR_WRITING);
        File dir = new File(pathToFolder + File.separator + "result");
        dir.mkdir();
        return dir;
    }

    private String getPathToDir() throws IOException {
        return directory.getCanonicalPath();
    }
}

