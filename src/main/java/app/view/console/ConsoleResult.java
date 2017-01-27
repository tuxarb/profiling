package app.view.console;


import app.utils.Log;

import java.io.IOException;

import static app.utils.ConsoleWorker.*;

class ConsoleResult {
    private ConsoleView view;
    private String capacity;
    private String runtime;
    private String speed;

    ConsoleResult(ConsoleView view) {
        this.view = view;
        initResultData();
    }

    private void initResultData() {
        //println(Log.DATA_INIT_FOR_DISPLAY);
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
        println("\n  " + Log.RESULTS);
        println("+-------------------------------+");
        println("| " + Log.RUNTIME + " \t\t" + runtime + " \t\t|");
        println("| " + Log.CAPACITY + " \t" + capacity + "  \t\t|");
        println("| " + Log.SPEED + " \t\t" + speed + "   \t|");
        println("+-------------------------------+");
        //println(Log.DATA_DISPLAY_SUCCESS);
        L:
        while (true) {
            print("\n");
            println(Log.RESULT_PANEL_CHOICE);
            print("\n");
            println("1. " + Log.SAVE_TO_FILE);
            println("2. " + Log.SAVE_TO_DATABASE);
            println("3. " + Log.UPDATE_PROPERTY_FILE);
            println("4. " + Log.REPEAT_TEST);
            println("5. " + Log.EXIT);
            print("\n");

            print(Log.ENTER);
            String input = readLine();
            switch (input) {
                case "1":
                    writeToFile();
                    break;
                case "2":
                    break;
                case "3":
                    updatePropertyFile();
                    break;
                case "4":
                    println(Log.RETURNING_TO_MENU + "(y|n)");
                    String action = readLine();
                    if (action.startsWith("y")) {
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

    private void returnToMenu() {
        view.renderMenu();
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

    private void updatePropertyFile() {
        try {
            view.getEventListener().updatePropertyFile();
            println(Log.PROPERTY_UPDATE);
        } catch (Exception e) {
            println(Log.PROPERTY_READ_ERROR);
        }
    }
}
