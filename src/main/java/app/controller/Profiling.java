package app.controller;

import app.model.Model;
import app.model.PointsList;
import app.model.beans.Characteristic;
import app.model.enums.DatabaseTypes;
import app.model.enums.OperatingSystems;
import app.utils.GraphicsConfig;
import app.utils.Log;
import app.utils.exceptions.ClientProcessException;
import app.utils.exceptions.WrongSelectedDatabaseException;
import app.view.console.ConsoleView;
import app.view.gui.GuiView;
import app.view.gui.WelcomePanelImpl;
import org.slf4j.Logger;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Profiling implements EventListener {
    private GuiView guiView;
    private ConsoleView consoleView;
    private Model model;
    private static final Logger LOG = Log.createLog(Profiling.class);

    public Profiling() {
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
        } else {
            consoleView = new ConsoleView();
            consoleView.setEventListener(this);
            consoleView.init();
        }
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
        } catch (IOException e) {
            waitSomeTime(40);
            LOG.error(Log.INTERNAL_APPLICATION_ERROR);
            throw new ClientProcessException();
        }
        model.completed();
    }

    private void startDetailedTest(boolean isWindows) throws ClientProcessException, IOException {
        model.setNumberTests();
        LOG.info(Log.DETAILED_TEST_STARTED);
        for (int i = 1; i <= model.getNumberTests(); i++) {
            LOG.debug(Log.TEST_NUMBER + i + Log.STARTED);
            try {
                if (isWindows) {
                    model.startTestForWindows();
                } else {
                    model.startTestForLinuxOrMac();
                }
                LOG.debug(Log.TEST_NUMBER + i + Log.COMPLETED);
            } catch (Exception e) {
                waitSomeTime(40);
                if (i == 1) {
                    LOG.error(Log.DETAILED_TEST_ENDED_WITH_ERROR);
                    throw e;
                } else {
                    LOG.debug(Log.TEST_NUMBER + i + Log.A_TEST_ENDED_WITH_ERROR);
                    i--;
                }
            }
        }
        LOG.info(Log.DETAILED_TEST_ENDED);
    }

    private boolean isGUI() {
        String mode = System.getProperty("mode");
        return "gui".equalsIgnoreCase(mode);
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
    public void exit() {
        model.exit();
    }

    public static void main(String[] args) {
        new Profiling();
    }
}
