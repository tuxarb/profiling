package app.controller;

import app.model.Model;
import app.model.enums.DatabaseTypes;
import app.model.enums.OperatingSystems;
import app.utils.exceptions.WrongSelectedDatabaseException;

import java.io.File;
import java.io.IOException;

public interface EventListener{
    void findOutOS(OperatingSystems os);
    void update();
    boolean isCompleted();
    Model getModel();
    void exit();
    void writeToFile() throws IOException;
    void writeToDatabase(DatabaseTypes type) throws IOException, WrongSelectedDatabaseException;
    void readPropertyFile(File file) throws Exception;
    void updatePropertyFile() throws Exception;
    boolean isPropertiesFileExists();
    void setCompleted(boolean isCompleted);
}
