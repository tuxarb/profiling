package app.view.console;


import app.utils.Log;
import app.utils.exceptions.ClientProcessException;

import java.io.File;

import static app.utils.ConsoleWorker.*;

class ConsoleMenu {
    private ConsoleView view;
    private static volatile boolean isExceptionOccurred;

    ConsoleMenu(ConsoleView view) {
        this.view = view;
    }

    public void init() {
        print("\n\n+-------------------------------------------------+\n");
        println("\t\t   " + Log.MENU_CONSOLE + "\t");
        println("+-------------------------------------------------+");
        print("\n");
        println("    " + Log.MENU_CHOICE);
        print("\n");
        println("\t\t1. " + Log.START_TEST_BUTTON_MESSAGE);
        println("\t\t2. " + Log.OPEN_PROPERTY_FILE);
        print("\n");
        println("+-------------------------------------------------+");
        while (true) {
            print("\n" + Log.ENTER);
            String input = readLine();
            if (input.startsWith("1")) {
                if (!view.getEventListener().isPropertiesFileExists()) {
                    println(Log.PROPERTIES_IS_NULL);
                } else {
                    findOutOS();
                    waitForEndProcessing();
                    if (!isExceptionOccurred) {
                        break;
                    }
                }
            } else if (input.startsWith("2")) {
                readPropertyFile();
            } else {
                println(Log.WRONG_ENTER);
            }
        }
        update();
    }

    private void waitForEndProcessing() {
        println("+------------------------------------------------+");
        println("\t\t" + Log.PROCESSING);
        println("+------------------------------------------------+");
        while (true) {
            if (isExceptionOccurred) {
                return;
            }
            if (view.getEventListener().isCompleted()) {
                break;
            }
        }
    }

    private void update() {
        println(Log.PROCESSING_WAS_FINISHED);
        ConsoleResult consoleResult = new ConsoleResult(view);
        consoleResult.init();
    }

    private void findOutOS() {
        isExceptionOccurred = false;
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                view.getEventListener().findOutOS(view.getOperatingSystem());
            } catch (ClientProcessException ex) {
                isExceptionOccurred = true;
                if (ex.getLocalizedMessage() != null && !ex.getLocalizedMessage().isEmpty()) {
                    println(ex.getLocalizedMessage());
                } else
                    println(Log.CLIENT_PROCESS_ERROR);
            }
        }).start();
    }

    private void readPropertyFile() {
        while (true) {
            print(Log.MENU_PATH_TO_PROPERTY_FILE);
            String path = readLine().trim();
            if ("cancel".equalsIgnoreCase(path)) {
                println(Log.CANCELLING_PROPERTY_FILE);
                return;
            }
            try {
                if (!isPropertiesFile(path)) {
                    throw new Exception();
                }
                view.getEventListener().readPropertyFile(new File(path));
                println(Log.PROPERTY_FILE_READ);
                break;
            } catch (Exception e) {
                println(Log.MENU_WRONG_PATH_TO_PROPERTY_FILE);
                print("\n");
            }
        }
    }

    private boolean isPropertiesFile(String path) {
        return path.toLowerCase().endsWith(".properties");
    }
}
