package app.controller;

import app.model.Model;
import app.model.beans.Characteristic;
import app.model.enums.DatabaseTypes;
import app.model.enums.OperatingSystems;
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
    public void exit() {
        model.exit();
    }

    @Override
    public void findOutOS(OperatingSystems os) throws ClientProcessException {
        try {
            if (os.name().equals(OperatingSystems.WINDOWS.toString()))
                startTestForWindows();

            if (os.name().equals(OperatingSystems.LINUX.toString()))
                startTestForLinuxOrMac();

            if (os.name().equals(OperatingSystems.MAC.toString()))
                startTestForLinuxOrMac();
        } catch (IOException e) {
            LOG.error(Log.INTERNAL_APPLICATION_ERROR);
            throw new ClientProcessException();
        }
        model.completed();
    }

    private void startTestForWindows() throws ClientProcessException, IOException {
        if (model.isDetailedTest()) {
            setNumberTestsForDetailedTest();
            LOG.info(Log.DETAILED_TEST_STARTED);
            for (int i = 1; i <= model.getNumberTests(); i++) {
                LOG.debug(Log.TEST_NUMBER + i + Log.STARTED);
                try {
                    model.startTestForWindows();
                } catch (Exception e) {
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException ignored) {
                    }
                    if (i == 1) {
                        LOG.error(Log.DETAILED_TEST_ENDED_WITH_ERROR);
                        throw e;
                    } else {
                        LOG.error(Log.TEST_NUMBER + i + Log.A_TEST_ENDED_WITH_ERROR);
                        i--;
                        continue;
                    }
                }
                LOG.debug(Log.TEST_NUMBER + i + Log.COMPLETED);
            }
            LOG.info(Log.DETAILED_TEST_ENDED);
        } else {
            model.startTestForWindows();
        }
    }

    private void startTestForLinuxOrMac() throws ClientProcessException, IOException {
        if (model.isDetailedTest()) {
            setNumberTestsForDetailedTest();
        }
        model.startTestForLinuxOrMac();
    }

    private void setNumberTestsForDetailedTest() {
        model.setNumberTests();
    }

    public boolean isCompleted() {
        return model.isCompleted();
    }

    public void update() {
        guiView.update();
    }

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
    public void setCompleted(boolean isCompleted) {
        model.setCompleted(isCompleted);
    }

    @Override
    public void setDetailedTest(boolean isDetailedTest) {
        model.setDetailedTest(isDetailedTest);
    }

    private boolean isGUI() {
        String mode = System.getProperty("mode");
        return "gui".equalsIgnoreCase(mode);
    }

    public static void main(String[] args) {
        new Profiling();
    }
}
