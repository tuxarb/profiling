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
        println(Log.MENU);
        println("--------------------");

        while (true) {
            print("\n");
            println(Log.MENU_CHOICE);
            print("\n");
            println("1. " + Log.START_TEST_BUTTON_MESSAGE);
            println("2. " + Log.OPEN_PROPERTY_FILE);
            print("\n");
            print(Log.ENTER);
            String input = readLine();
            if (input.startsWith("1")) {
                if (!view.getEventListener().isPropertiesFileExists()) {
                    println(Log.PROPERTIES_IS_NULL);
                } else {
                    findOutOS();
                    update();
                    if (!isExceptionOccurred)
                        break;
                }
            } else if (input.startsWith("2")) {
                try {
                    view.getEventListener().readPropertyFile(getFile());
                    println(Log.PROPERTY_READ);
                } catch (Exception e1) {
                    println(Log.MENU_WRONG_PATH_TO_PROPERTY_FILE);
                }
            } else {
                println(Log.WRONG_ENTER);
            }
        }
    }

    public void update() {
        println("+--------------+");
        println("| " + Log.PROCESSING + "|");
        println("+--------------+");
        while (true) {
            if (isExceptionOccurred) {
                return;
            }
            if (view.getEventListener().isCompleted()) {
                break;
            }
        }
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

    private File getFile() {
        print(Log.MENU_PATH_TO_PROPERTY_FILE);
        return new File(readLine());
    }
}
