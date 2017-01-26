package app.view.console;


import app.controller.EventListener;
import app.model.enums.OperatingSystems;

import static app.utils.ConsoleWorker.print;

public class ConsoleView {
    private EventListener eventListener;
    private OperatingSystems operatingSystem;

    public ConsoleView() {
    }

    public void init() {
        ConsoleWelcome welcome = new ConsoleWelcome(this);
        welcome.init();
    }

    void renderMenu() {
        print("\n--------------------\n");
        ConsoleMenu menu = new ConsoleMenu(this);
        menu.init();
        getEventListener().setCompleted(false);
    }

    EventListener getEventListener() {
        return eventListener;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    OperatingSystems getOperatingSystem() {
        return operatingSystem;
    }

    void setOperatingSystem(OperatingSystems operatingSystem) {
        this.operatingSystem = operatingSystem;
    }
}
