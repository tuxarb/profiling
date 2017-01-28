package app.view.console;


import app.model.enums.DatabaseTypes;
import app.utils.Log;
import app.utils.Utils;
import app.utils.exceptions.WrongSelectedDatabaseException;
import org.slf4j.Logger;

import java.io.IOException;

import static app.utils.ConsoleWorker.*;

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
        LOG.info(Log.DATA_INIT_FOR_DISPLAY);
        this.capacity = view.getEventListener().getModel().getCharacteristic().getCapacity();
        this.runtime = view.getEventListener().getModel().getCharacteristic().getRuntime();
        this.speed = view.getEventListener().getModel().getCharacteristic().getSpeed();

        if (this.capacity == null ||
                this.runtime == null ||
                this.speed == null) {
            println(Log.DATA_DISPLAY_ERROR);
            returnToMenu();
        }
    }

    void init() {
        print("\n\n");
        println("+----------------------------------------+");
        println("|\t" + Log.RESULTS);
        println("+----------------------------------------+");
        println("| " + Log.RUNTIME + " \t" + runtime);
        println("| " + Log.CAPACITY + " \t" + capacity);
        println("| " + Log.SPEED + " \t" + speed);
        println("+---------------------------------------+");
        LOG.info(Log.DATA_DISPLAY_SUCCESS);
        L:
        while (true) {
            print("\n");
            println(Log.RESULT_PANEL_CHOICE);
            print("\n");
            println("\t1. " + Log.SAVE_TO_FILE);
            println("\t2. " + Log.SAVE_TO_DATABASE);
            println("\t3. " + Log.UPDATE_PROPERTY_FILE);
            println("\t4. " + Log.REPEAT_TEST);
            println("\t5. " + Log.EXIT);
            print("\n");

            print(Log.ENTER);
            String input = readLine();
            switch (input) {
                case "1":
                    writeToFile();
                    break;
                case "2":
                    DatabaseTypes type = readDatabaseType();
                    if (type == null) {
                        break;
                    }
                    writeToDatabase(type);
                    break;
                case "3":
                    if (isYes(Log.CONFIRMATION_OF_UPDATE_PROPERTY_FILE)) {
                        updatePropertyFile();
                    }
                    break;
                case "4":
                    if (isYes(Log.RETURNING_TO_MENU)) {
                        break L;
                    }
                    break;
                case "5":
                    exit();
                default:
                    println(Log.WRONG_ENTER);
            }
        }
        returnToMenu();
    }

    private boolean isYes(String message) {
        println(message + "(y|n)");
        String answer = readLine();
        return answer.startsWith("y") || answer.startsWith("у");
    }

    private void returnToMenu() {
        view.renderMenu();
    }

    private void updatePropertyFile() {
        try {
            view.getEventListener().updatePropertyFile();
            println(Log.PROPERTY_FILE_UPDATE);
        } catch (Exception e) {
            println(Log.PROPERTY_READ_ERROR);
        }
    }

    private void exit() {
        println(Log.CLOSING_APP);
        view.getEventListener().exit();
    }

    private void writeToFile() {
        try {
            view.getEventListener().writeToFile();
            println(Log.FILE_DATA_DISPLAY_SUCCESS);
        } catch (IOException e) {
            println(Log.FILE_DATA_DISPLAY_ERROR);
        }
    }

    private void writeToDatabase(DatabaseTypes type) {
        try {
            view.getEventListener().writeToDatabase(type);
            println(Log.WRITING_DATABASE_SUCCESS);
        } catch (IOException e) {
            println(Log.WRITING_DATABASE_ERROR);
        } catch (WrongSelectedDatabaseException e) {
            println(Log.WRONG_DATABASE_URL);
        }
    }

    private DatabaseTypes readDatabaseType() {
        while (true) {
            print("\n");
            println(Log.DATABASE_INPUT);
            print("\n");
            println("\t1." + Utils.POSTGRESQL);
            println("\t2." + Utils.MYSQL);
            println("\t3." + Utils.ORACLE);
            println("\t4." + Utils.SQL_SERVER);
            println("\t5." + Utils.OTHER_DBMS);
            println("------------");
            println("\t6." + Log.EXIT);
            print("\n");

            print(Log.ENTER);
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
                case "6":
                    println(Log.TYPE_DATABASE_IS_NOT_SELECTED);
                    return null;
                default:
                    println(Log.WRONG_ENTER);
            }
        }
    }
}
