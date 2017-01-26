package app.controller;

import app.model.Model;
import app.model.beans.Characteristic;
import app.model.enums.DatabaseTypes;
import app.model.enums.OperatingSystems;
import app.utils.exceptions.ClientProcessException;
import app.utils.exceptions.WrongSelectedDatabaseException;
import app.view.console.ConsoleView;
import app.view.gui.WelcomePanelImpl;
import app.view.gui.View;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Profiling implements EventListener {
    private View view;
    private ConsoleView consoleView;
    private Model model;

    public Profiling() {
        model = new Model(new Characteristic());

        boolean flag = true;
        if (!flag) {
            SwingUtilities.invokeLater(() -> {
                view = new View();
                view.setEventListener(Profiling.this);
                view.initFrame();
                view.setPanel(new WelcomePanelImpl(view));
                view.getPanel().init();
            });
        } else {
            consoleView = new ConsoleView();
            consoleView.setEventListener(Profiling.this);
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
                startTestForLinux();

            if (os.name().equals(OperatingSystems.MAC.toString()))
                startTestForMac();
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.completed();
    }

    private void startTestForWindows() throws IOException, ClientProcessException {
        model.startTestForWindows();
    }

    private void startTestForLinux() throws IOException, ClientProcessException {
        model.startTestForLinux();
    }

    private void startTestForMac() throws IOException, ClientProcessException {
        model.startTestForMac();
    }

    public boolean isCompleted() {
        return model.isCompleted();
    }

    public void update() {
        view.update();
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

    public static void main(String[] args) {
        new Profiling();
    }
}
