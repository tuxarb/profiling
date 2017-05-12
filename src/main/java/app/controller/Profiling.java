package app.controller;

import app.model.DetailedTestResultsCalculator;
import app.model.Model;
import app.model.PointsList;
import app.model.beans.Characteristic;
import app.model.enums.DatabaseTypes;
import app.model.enums.OperatingSystems;
import app.utils.GraphicsConfig;
import app.utils.Log;
import app.utils.Utils;
import app.utils.exceptions.ClientProcessException;
import app.utils.exceptions.WrongSelectedDatabaseException;
import app.view.console.ConsoleView;
import app.view.gui.GuiView;
import app.view.gui.WelcomePanelImpl;
import org.slf4j.Logger;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class Profiling implements EventListener {
    private GuiView guiView;
    private Model model;
    private static final Logger LOG = Log.createLog(Profiling.class);

    private Profiling() {
        LOG.debug(Log.APP_IS_BEING_INITIALIZED);

        model = new Model(new Characteristic());
        if (isGUI()) {
            SwingUtilities.invokeLater(() -> {
                guiView = new GuiView();
                guiView.setEventListener(Profiling.this);
                guiView.initFrame();
                guiView.setPanel(new WelcomePanelImpl(guiView));
                guiView.getPanel().init();
            });
        } else if (isNonInteractiveConsoleUI()) {
            startTestInNonInteractiveMode();
        } else {
            ConsoleView consoleView = new ConsoleView();
            consoleView.setEventListener(this);
            consoleView.init();
        }
    }

    private void startTestInNonInteractiveMode() {
        setDetailedTest(true);
        OperatingSystems os = null;
        if (Utils.isWindows()) {
            os = OperatingSystems.WINDOWS;
        } else if (Utils.isLinux()) {
            os = OperatingSystems.LINUX;
        } else if (Utils.isMac()) {
            os = OperatingSystems.MAC;
        } else {
            LOG.error(Log.NOT_SUPPORTED_OS);
            printErrorMessageAndExit();
        }
        String filePath = getPropertiesFilePath();
        if (filePath.isEmpty()) {
            LOG.error(Log.PROPERTIES_PATH_WAS_NOT_SPECIFIED);
            printErrorMessageAndExit();
        }
        try {
            model.readPropertyFile(new File(filePath));
            findOutOS(os);
            model.writeAllToFile();
        } catch (Exception e) {
            if (model.getPropertyRepository().getPropertyFile() == null) {
                LOG.error(Log.ERROR + ". " + Log.THE_FILE_IS_NOT_PROPERTIES);
            }
            printErrorMessageAndExit();
        }
    }

    private void printErrorMessageAndExit() {
        System.out.println(Log.ERROR_OCCURRED + " " + Utils.getPathToLogs());
        exit(1);
    }

    @Override
    public void findOutOS(OperatingSystems os) throws ClientProcessException {
        try {
            if (os.name().equals(OperatingSystems.WINDOWS.name())) {
                if (model.isDetailedTest()) {
                    startDetailedTest(true);
                } else {
                    model.startTestForWindows();
                }
            } else if (os.name().equals(OperatingSystems.LINUX.name()) ||
                    os.name().equals(OperatingSystems.MAC.name())) {
                if (model.isDetailedTest()) {
                    startDetailedTest(false);
                } else {
                    model.startTestForLinuxOrMac();
                }
            }
            model.getCharacteristic().setTestsNumber(
                    model.isDetailedTest() ? model.getNumberTests() : 1
            );
        } catch (IOException e) {
            waitSomeTime(40);
            LOG.error(Log.INTERNAL_APPLICATION_ERROR);
            throw new ClientProcessException();
        } catch (ClientProcessException e) {
            throw e;
        } catch (Throwable e) {
            waitSomeTime(40);
            LOG.error(Log.UNEXPECTED_ERROR + e.toString());
            e.printStackTrace();
            throw new ClientProcessException();
        }
        model.completed();
    }

    private void startDetailedTest(boolean isWindows) throws ClientProcessException, IOException {
        DetailedTestResultsCalculator calculator = new DetailedTestResultsCalculator(model);
        int maxAttemptsNumberForStartTheFirstTest = 3;
        int maxAttemptsNumberForStartTheFollowingTests = 15;
        int curAttemptsNumberForStart = 0;
        model.setNumberTests();
        LOG.info(Log.DETAILED_TEST_STARTED);
        for (int i = 1; i <= model.getNumberTests(); i++) {
            LOG.debug(Log.TEST_NUMBER + i + Log.STARTED);
            try {
                model.killAllChildrenProcesses();
                if (isWindows) {
                    model.startTestForWindows();
                } else {
                    model.startTestForLinuxOrMac();
                }
                calculator.saveResultForTest(model.getCharacteristic());
                curAttemptsNumberForStart = 0;
                LOG.debug(Log.TEST_NUMBER + i + Log.COMPLETED);
            } catch (Exception e) {
                waitSomeTime(40);
                if (i == 1) {
                    int rest = maxAttemptsNumberForStartTheFirstTest - curAttemptsNumberForStart;
                    if (rest > 0) {
                        LOG.warn(Log.DETAILED_TEST_FIRST_START_ERROR, rest);
                    } else {
                        LOG.error(Log.DETAILED_TEST_ENDED_WITH_ERROR);
                        throw e;
                    }
                } else {
                    if (curAttemptsNumberForStart == maxAttemptsNumberForStartTheFollowingTests) {
                        LOG.error(Log.DETAILED_TEST_NUMBER_ATTEMPTS_EXCEEDED,
                                maxAttemptsNumberForStartTheFollowingTests
                        );
                        LOG.error(Log.DETAILED_TEST_ENDED_WITH_ERROR);
                        throw e;
                    }
                }
                LOG.debug(Log.TEST_NUMBER + i + Log.A_TEST_ENDED_WITH_ERROR);
                i--;
                curAttemptsNumberForStart++;
            }
        }
        LOG.debug(Log.DETAILED_TEST_COMPUTING_RESULT_STARTED);
        try {
            calculator.computeAndSaveAverageResult();
        } catch (Exception e) {
            LOG.error(Log.DETAILED_TEST_RESULT_COMPUTING_ERROR);
            if (e.getMessage() != null && !e.getMessage().isEmpty()) {
                LOG.error(e.toString());
            }
            throw new ClientProcessException();
        }
        LOG.debug(Log.DETAILED_TEST_COMPUTING_RESULT_ENDED);
        LOG.info(Log.DETAILED_TEST_ENDED);
    }

    private boolean isGUI() {
        String mode = System.getProperty("mode");
        return "gui".equalsIgnoreCase(mode);
    }

    private boolean isNonInteractiveConsoleUI() {
        String mode = System.getProperty("mode");
        return "non_interactive".equalsIgnoreCase(mode);
    }

    private String getPropertiesFilePath() {
        String path = System.getProperty("path");
        return path != null ? path.trim() : "";
    }

    private void waitSomeTime(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public boolean isCompleted() {
        return model.isTaskCompleted();
    }

    @Override
    public void update() {
        guiView.update();
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public void writeToFile() throws IOException {
        model.writeToFile();
    }

    @Override
    public void writeToDatabase(DatabaseTypes type) throws IOException, WrongSelectedDatabaseException {
        model.writeToDatabase(type);
    }

    @Override
    public void readPropertyFile(File file) throws Exception {
        model.readPropertyFile(file);
    }

    @Override
    public void updatePropertyFile() throws Exception {
        model.readPropertyFile(model.getPropertyRepository().getPropertyFile());
    }

    @Override
    public boolean isPropertiesFileExists() {
        File file = model.getPropertyRepository().getPropertyFile();
        return file != null && file.exists();
    }

    @Override
    public boolean areGraphicsAvailableToPaint() {
        if (model.getPoints().size() < GraphicsConfig.MIN_NUMBER_POINTS_TO_PAINT) {
            LOG.error(Log.GRAPHICS_ARE_NOT_AVAILABLE);
            return false;
        }
        return true;
    }

    @Override
    public PointsList getPoints() {
        return model.getPoints();
    }

    @Override
    public void setCompleted(boolean isCompleted) {
        model.setTaskCompleted(isCompleted);
    }

    @Override
    public void setDetailedTest(boolean isDetailedTest) {
        model.setDetailedTest(isDetailedTest);
    }

    @Override
    public void exit(int digit) {
        model.exit(digit);
    }

    public static void main(String[] args) {
        checkJavaVersion();
        new Profiling();
    }

    private static void checkJavaVersion() {
        int version = Integer.valueOf(
                ManagementFactory.getRuntimeMXBean().getSpecVersion().substring(0, 1)
        );
        if (version != 9) {
            System.out.println(Log.UNSUPPORTED_JAVA_VERSION);
            LOG.error(Log.UNSUPPORTED_JAVA_VERSION);
            System.exit(1);
        }
    }
}
