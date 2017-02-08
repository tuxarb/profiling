package app.model;

import app.model.beans.Characteristic;
import app.model.enums.DatabaseTypes;
import app.utils.Log;
import app.utils.Utils;
import app.utils.exceptions.ClientProcessException;
import app.utils.exceptions.WrongSelectedDatabaseException;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import static app.utils.Utils.getUserCommandInfo;

public class Model {
    private final Characteristic characteristic;
    private long processId;
    private ProcessHandle task;
    private volatile boolean isCompleted = false;
    private static final PropertyRepository PROPERTY_REPOSITORY = PropertyRepository.getInstance();
    private static final Logger LOG = Log.createLog(Model.class);

    public Model(final Characteristic characteristic) {
        this.characteristic = characteristic;
    }

    public void startTestForWindows() throws IOException, ClientProcessException {
        long capacity = 0;
        long countIterations = 0;
        long startTime = startTestAndGetStartTime();

        Process process;
        Scanner scanner;
        while (task.isAlive()) {
            process = execute("tasklist /v /fo list /fi \"PID eq \"" + processId);
            scanner = new Scanner(process.getInputStream(), "cp866");

            while (scanner.hasNextLine()) {
                String newLine = scanner.nextLine();
                if (Utils.isMemoryLine(newLine.toLowerCase())) {
                    capacity += Utils.getNumberFromString(newLine);
                    countIterations++;
                    break;
                }
            }
            waitSomeTime(20);
        }
        checkCountOfIterationsOnZero(countIterations);

        long time = getTestTime(startTime);
        capacity /= countIterations;
        long speed = 1000 * capacity / time;

        setResultData(capacity, speed, time);
        LOG.info(Log.END_READING_PROCESS);
    }

    public void startTestForLinux() throws IOException, ClientProcessException {
        long capacity = 0;
        long countIterations = 0;
        long startTime = startTestAndGetStartTime();

        Process process;
        Scanner scanner;
        while (task.isAlive()) {
            process = execute("ps -p " + processId + " -o rss");
            scanner = new Scanner(process.getInputStream());
            while (scanner.hasNext()) {
                String newLine = scanner.nextLine();
                if ((!newLine.matches("^\\D*"))) {
                    capacity += Utils.getNumberFromString(newLine);
                    countIterations++;
                    break;
                }
            }
            waitSomeTime(20);
        }
        checkCountOfIterationsOnZero(countIterations);

        long time = getTestTime(startTime);
        capacity /= countIterations;
        long speed = 1000 * capacity / time;

        setResultData(capacity, speed, time);
        LOG.info(Log.END_READING_PROCESS);
    }

    public void startTestForMac() throws IOException, ClientProcessException {
        long capacity = 0;
        long countIterations = 0;
        long startTime = startTestAndGetStartTime();

        Process process;
        Scanner scanner;
        while (task.isAlive()) {
            process = execute("top -ncols 8" + " -pid " + processId);
            scanner = new Scanner(process.getInputStream());
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.contains("java")) {
                    String cap = line.substring(41);
                    capacity += 1024 * Utils.getNumberFromString(cap);
                    countIterations++;
                    process.destroy();
                }
            }
            waitSomeTime(20);
        }
        checkCountOfIterationsOnZero(countIterations);

        long time = getTestTime(startTime);
        capacity /= countIterations;
        long speed = 1000 * capacity / time;

        setResultData(capacity, speed, time);
        LOG.info(Log.END_READING_PROCESS);
    }

    private void waitSomeTime(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    private long startTestAndGetStartTime() throws ClientProcessException {
        boolean isScriptFile = false;
        String pathToProgram = PROPERTY_REPOSITORY.getProperties().getProperty("program_path");
        if (pathToProgram.isEmpty()) {
            pathToProgram = PROPERTY_REPOSITORY.getProperties().getProperty("script_file_path");
            if (pathToProgram.isEmpty()) {
                LOG.error(Log.EMPTY_PATH);
                throw new ClientProcessException(Log.EMPTY_PATH_MESSAGE);
            }
            isScriptFile = true;
        }

        long startTime = System.currentTimeMillis();
        createAndRunUserProcess(pathToProgram, isScriptFile);

        return startTime;
    }

    private void createAndRunUserProcess(String pathToProgram, boolean isScriptFile) throws ClientProcessException {
        LOG.info(Log.START_READING_PROCESS);
        try {
            execute(pathToProgram);

            if (isScriptFile) {
                long startTime = System.currentTimeMillis();
                while (true) {
                    if (ProcessHandle.current().descendants().count() == 2) {
                        processId = ProcessHandle.current()
                                .children()
                                .findFirst()
                                .orElseThrow(() -> new Exception(Log.SMALL_PROGRAM_ERROR))
                                .children()
                                .findFirst()
                                .orElseThrow(() -> new Exception(Log.SMALL_PROGRAM_ERROR))
                                .getPid();
                        break;
                    }
                    if (System.currentTimeMillis() - startTime > 999) {
                        throw new Exception(Log.ERROR_WHEN_CREATING_USER_PROCESS_FROM_SCRIPT_FILE);
                    }
                }
            } else {
                processId = ProcessHandle.current()
                        .children()
                        .findFirst()
                        .orElseThrow(() -> new Exception(Log.ERROR_WHEN_CREATING_USER_PROCESS))
                        .getPid();
            }
            task = ProcessHandle.of(processId)
                    .orElseThrow(() -> new Exception(Log.PATH_TO_PROGRAM_INCORRECT));
        } catch (Exception e) {
            killAllChildrenProcesses();
            LOG.error(e.getMessage());
            throw new ClientProcessException();
        }
        LOG.info(Log.START_RUNNING_CODE + getUserCommandInfo(task));
    }

    private Process execute(String command) throws IOException {
        return Runtime.getRuntime().exec(command);
    }

    private long getTestTime(long startTime) {
        long endTime = System.currentTimeMillis();
        LOG.info(Log.END_RUNNING_CODE);

        return endTime - startTime;
    }

    private void setResultData(long capacity, long speed, long runtime) {
        characteristic.setCapacity(Utils.formatNumber(capacity) + " kB");
        characteristic.setSpeed(Utils.formatNumber(speed) + " kB/s");
        String timeAsString = Utils.formatNumber(runtime);
        characteristic.setRuntime((runtime < 1000 ? "0," + timeAsString : timeAsString) + " s");
    }

    private void checkCountOfIterationsOnZero(long countIterations) throws ClientProcessException {
        if (countIterations == 0) {
            LOG.error(Log.SMALL_PROGRAM_ERROR);
            throw new ClientProcessException(Log.SMALL_PROGRAM_ERROR);
        }
    }

    public void exit() {
        LOG.debug(Log.CLOSING_APP);
        killAllChildrenProcesses();
        System.exit(0);
    }

    private void killAllChildrenProcesses() {
        ProcessHandle.current()
                .descendants()
                .forEach(ProcessHandle::destroyForcibly);
    }

    public void writeToFile() throws IOException {
        Properties properties = PROPERTY_REPOSITORY.getProperties();
        characteristic.setTaskName(properties.getProperty("program_name"));
        String pathToUserFolderForSaveResult = properties.getProperty("result_file_path");

        app.model.FileWriter fileWriter = new app.model.FileWriter(characteristic, pathToUserFolderForSaveResult);
        fileWriter.write();
    }

    public void writeToDatabase(DatabaseTypes type) throws IOException, WrongSelectedDatabaseException {
        DatabaseWriter dw = new DatabaseWriter(type);
        Properties properties = PROPERTY_REPOSITORY.getProperties();

        dw.setUrl(properties.getProperty("url"));
        dw.setUsername(properties.getProperty("user"));
        dw.setPassword(properties.getProperty("password"));
        String programName = properties.getProperty("program_name");
        if (!programName.isEmpty()) {
            characteristic.setTaskName(programName);
        }
        dw.initProperties();
        dw.setSessionFactory();
        try {
            dw.write(characteristic);
        } catch (Exception e) {
            LOG.error(Log.HIBERNATE_ERROR);
            throw new IOException(e);
        }
    }

    public void readPropertyFile(File file) throws Exception {
        PROPERTY_REPOSITORY.setPropertyFile(file);
    }

    public void completed() {
        this.isCompleted = true;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Characteristic getCharacteristic() {
        return characteristic;
    }

    public PropertyRepository getPropertyRepository() {
        return PROPERTY_REPOSITORY;
    }
}