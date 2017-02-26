package app.view.console;


import app.model.enums.DatabaseTypes;
import app.utils.Log;
import app.utils.Utils;
import app.utils.exceptions.WrongSelectedDatabaseException;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

import static app.utils.ConsoleWorker.*;
import static app.utils.Utils.getPathToLogs;

class ConsoleResult {
    private ConsoleView view;
    private String capacity;
    private String runtime;
    private String speed;
    private static final Logger LOG = Log.createLog(ConsoleResult.class);

    ConsoleResult(ConsoleView view) {
        this.view = view;
        initResultData();
    }

    private void initResultData() {
        LOG.info(Log.DATA_INIT_FOR_DISPLAYING);
        this.capacity = view.getEventListener().getModel().getCharacteristic().getCapacity();
        this.runtime = view.getEventListener().getModel().getCharacteristic().getRuntime();
        this.speed = view.getEventListener().getModel().getCharacteristic().getSpeed();

        if (this.capacity == null ||
                this.runtime == null ||
                this.speed == null) {
            println(Log.DATA_DISPLAYING_ERROR + "\n" + getPathToLogs());
            returnToMenu();
        }
    }

    void init() {
        displayResultPanel();
        LOG.info(Log.DATA_DISPLAYING_SUCCESS);
        L:
        while (true) {
            print("\n" + Log.ENTER);
            String input = readLine();
            switch (input) {
                case "1":
                    writeToFile();
                    break;
                case "2":
                    displayDatabaseList();
                    while (true) {
                        DatabaseTypes type = readDatabaseType();
                        if (type == null || writeToDatabase(type)) {
                            displayResultPanel();
                            break;
                        }
                    }
                    break;
                case "3":
                    if (!isAnswerYes(Log.CONFIRMATION_OF_UPDATE_PROPERTY_FILE)) {
                        LOG.debug(Log.NO_OPTION_WHEN_UPDATE_THE_PROPERTY_FILE);
                        break;
                    }
                    updatePropertyFile();
                    break;
                case "4":
                    if (!isAnswerYes(Log.CONFIRMATION_OF_RETURNING_TO_MENU)) {
                        LOG.debug(Log.NO_OPTION_WHEN_RETURNING_TO_THE_MENU);
                        break;
                    }
                    break L;
                case "5":
                    writePointsToFile();
                    break;
                case "0":
                    exit();
                default:
                    println(Log.WRONG_ENTER);
            }
        }
        returnToMenu();
    }

    private boolean isAnswerYes(String message) {
        println(message + " (y | n)");
        print(">");
        String answer = readLine().toLowerCase();
        return answer.startsWith("y");
    }

    private void returnToMenu() {
        LOG.info(Log.RETURNING_TO_THE_MENU_SUCCESS);
        view.renderMenu();
    }

    private void updatePropertyFile() {
        try {
            view.getEventListener().updatePropertyFile();
            println(Log.PROPERTY_FILE_UPDATED);
            LOG.info(Log.PROPERTY_FILE_UPDATED);
        } catch (Exception e) {
            println(Log.PROPERTY_READ_ERROR + " " + getPathToLogs());
        }
    }

    private void exit() {
        println(Log.CLOSING_APP);
        view.getEventListener().exit();
    }

    private void writePointsToFile() {
        print(Log.RESULT_PATH_TO_FILE_TO_SAVE_POINTS);
        while (true) {
            print("\n>");
            String path = readLine().trim();
            if ("".equals(path)) {
                println(Log.FILE_WAS_NOT_SELECTED);
                LOG.warn(Log.FILE_WAS_NOT_SELECTED);
                return;
            }
            File dir = new File(path);
            if (dir.exists()) {
                PointsFileWriter writer = new PointsFileWriter(view.getEventListener().getPoints(), dir);
                try {
                    writer.write();
                    if (!writer.getNewFile().exists()) {
                        throw new Exception(Log.CREATING_FILE_ERROR);
                    }
                    println(Log.WRITING_FILE_POINTS_SUCCESS);
                    LOG.info(Log.WRITING_FILE_POINTS_SUCCESS);
                    break;
                } catch (Exception e) {
                    print(Log.CREATING_FILE_ERROR);
                    LOG.error(e.toString());
                }
            } else {
                print(Log.WRONG_PATH_TO_FILE);
            }
        }
    }

    private void writeToFile() {
        try {
            view.getEventListener().writeToFile();
            println(Log.WRITING_FILE_DATA_SUCCESS);
        } catch (IOException e) {
            println(Log.FILE_DATA_DISPLAYING_ERROR + " " + getPathToLogs());
        }
    }

    private boolean writeToDatabase(DatabaseTypes type) {
        try {
            view.getEventListener().writeToDatabase(type);
            println(Log.WRITING_DATABASE_SUCCESS);
        } catch (IOException e) {
            println(Log.WRITING_DATABASE_ERROR + " " + getPathToLogs());
            return false;
        } catch (WrongSelectedDatabaseException e) {
            println(Log.WRONG_DATABASE_URL);
            return false;
        }
        return true;
    }

    private DatabaseTypes readDatabaseType() {
        while (true) {
            print("\n>");
            String type = readLine();
            switch (type) {
                case "1":
                    return DatabaseTypes.POSTGRESQL;
                case "2":
                    return DatabaseTypes.MYSQL;
                case "3":
                    return DatabaseTypes.ORACLE;
                case "4":
                    return DatabaseTypes.SQLSERVER;
                case "5":
                    return DatabaseTypes.OTHER;
                case "0":
                    println(Log.TYPE_DATABASE_IS_NOT_SELECTED);
                    return null;
                default:
                    println(Log.WRONG_ENTER);
            }
        }
    }

    private void displayDatabaseList() {
        print("\n");
        println("+---------------------------------------------------------------+");
        print("\n");
        println("    " + Log.DATABASE_INPUT);
        print("\n");
        println("\t\t1." + Utils.POSTGRESQL);
        println("\t\t2." + Utils.MYSQL);
        println("\t\t3." + Utils.ORACLE);
        println("\t\t4." + Utils.SQL_SERVER);
        println("\t\t5." + Utils.OTHER_DBMS);
        println("\t\t-----------");
        println("\t\t0." + Log.BACK);
        print("\n");
        println("+---------------------------------------------------------------+");
        print("\n");
    }

    private void displayResultPanel() {
        print("\n\n");
        println("+---------------------------------------------------+");
        println("\t     " + Log.RESULTS_CONSOLE);
        println("+---------------------------------------------------+");
        println("\t\t" + Log.RUNTIME + "   " + runtime);
        println("\t\t" + Log.CAPACITY + "  " + capacity);
        println("\t\t" + Log.SPEED + "     " + speed);
        println("+---------------------------------------------------+");
        print("\n");
        println("\t" + Log.RESULT_PANEL_CHOICE);
        print("\n");
        println("\t\t1. " + Log.SAVE_TO_FILE);
        println("\t\t2. " + Log.SAVE_TO_DATABASE);
        println("\t\t3. " + Log.UPDATE_PROPERTY_FILE);
        println("\t\t4. " + Log.REPEAT_TEST);
        println("\t\t5. " + Log.SAVE_POINTS_TO_FILE);
        println("\t\t0. " + Log.EXIT);
        print("\n");
        println("+---------------------------------------------------+");
    }
}
