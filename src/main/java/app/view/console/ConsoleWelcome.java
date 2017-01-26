package app.view.console;

import app.model.enums.OperatingSystems;
import app.utils.Log;
import app.utils.Utils;

import static app.utils.ConsoleWorker.*;

class ConsoleWelcome {
    private ConsoleView view;

    ConsoleWelcome(ConsoleView view) {
        this.view = view;
    }

    void init() {
        println(Log.WELCOME);
        println("--------------------");
        println(Log.SELECT_OS + ":\n");

        for (int i = 0; i < OperatingSystems.values().length; i++) {
            println(i + 1 + ". " + OperatingSystems.values()[i]);
        }

        while (true) {
            print("\n" + Log.ENTER);
            switch (readLine().toLowerCase()) {
                case "1":
                case "w":
                case "windows":
                case "win":
                    if (!Utils.isWindows()) {
                        println(Log.NOT_WINDOWS_OS);
                        break;
                    }
                    view.setOperatingSystem(OperatingSystems.WINDOWS);
                    break;
                case "2":
                case "linux":
                case "lin":
                case "l":
                    if (!Utils.isLinux()) {
                        println(Log.NOT_LINUX_OS);
                        break;
                    }
                    view.setOperatingSystem(OperatingSystems.LINUX);
                    break;
                case "3":
                case "m":
                case "mac":
                    if (!Utils.isMac()) {
                        println(Log.NOT_MAC_OS);
                        break;
                    }
                    view.setOperatingSystem(OperatingSystems.MAC);
                    break;
                default:
                    println(Log.WRONG_ENTER);
            }
            if (view.getOperatingSystem() != null) {
                break;
            }
        }
        println(Log.VALID_OS);
        update();
    }

    private void update() {
        view.renderMenu();
    }
}
