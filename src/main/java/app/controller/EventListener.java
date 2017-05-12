package app.controller;

import app.model.Model;
import app.model.PointsList;
import app.model.enums.DatabaseTypes;
import app.model.enums.OperatingSystems;
import app.utils.exceptions.WrongSelectedDatabaseException;

import java.io.File;
import java.io.IOException;

public interface EventListener {
    void findOutOS(OperatingSystems os);
    void update();
    boolean isCompleted();
    Model getModel();
    void exit(int digit);
    void writeToFile() throws IOException;
    void writeToDatabase(DatabaseTypes type) throws IOException, WrongSelectedDatabaseException;
    void readPropertyFile(File file) throws Exception;
    void updatePropertyFile() throws Exception;
    boolean isPropertiesFileExists();
    boolean areGraphicsAvailableToPaint();
    PointsList getPoints();
    void setCompleted(boolean isCompleted);
    void setDetailedTest(boolean isDetailedTest);
}
