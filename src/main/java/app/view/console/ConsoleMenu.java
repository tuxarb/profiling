package app.view.console;


import app.utils.Log;
import app.utils.exceptions.ClientProcessException;
import org.slf4j.Logger;

import java.io.File;

import static app.utils.ConsoleWorker.*;

class ConsoleMenu {
    private ConsoleView view;
    private int second = 0;
    private static volatile boolean isExceptionOccurred;
    private static final Logger LOG = Log.createLog(ConsoleMenu.class);

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
        println("\t\t1. " + Log.START_TEST);
        println("\t\t2. " + Log.OPEN_PROPERTY_FILE);
        print("\n");
        println("+-------------------------------------------------+");

        while (true) {
            print("\n" + Log.ENTER);
            String input = readLine();
            if (input.startsWith("1")) {
                if (!view.getEventListener().isPropertiesFileExists()) {
                    println(Log.PROPERTIES_IS_NULL_LOG);
                    LOG.warn(Log.PROPERTIES_IS_NULL_LOG);
                } else {
                    findOutOS();
                    waitForEndProcessing();
                    LOG.info(Log.PROCESS_INFO_END);
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
        LOG.info(Log.PROCESS_INFO_START);
        while (true) {
            displayInfoAboutProcessing();
            if (isExceptionOccurred) {
                second = 0;
                print("\n");
                return;
            }
            if (view.getEventListener().isCompleted()) {
                break;
            }
        }
    }

    private void update() {
        print("\n");
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
            }
            try {
                view.getEventListener().findOutOS(view.getOperatingSystem());
            } catch (ClientProcessException ex) {
                isExceptionOccurred = true;
                print("\n");
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
            if ("".equals(path)) {
                println(Log.A_PROPERTY_FILE_WAS_NOT_SELECTED);
                LOG.warn(Log.A_PROPERTY_FILE_WAS_NOT_SELECTED);
                return;
            }
            try {
                view.getEventListener().readPropertyFile(new File(path));
                println(Log.PROPERTY_FILE_READ);
                break;
            } catch (Exception e) {
                println(Log.MENU_WRONG_PATH_TO_PROPERTY_FILE);
                print("\n");
            }
        }
    }

    private void displayInfoAboutProcessing() {
        System.out.print("\r" + Log.PROCESSING_CONSOLE + " " + (second++) + " sec.");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }
}
