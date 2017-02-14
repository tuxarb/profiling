package app.view.console;


import app.controller.EventListener;
import app.model.enums.OperatingSystems;

public class ConsoleView {
    private EventListener eventListener;
    private OperatingSystems operatingSystem;

    public void init() {
        ConsoleWelcome welcome = new ConsoleWelcome(this);
        welcome.init();
    }

    void renderMenu() {
        getEventListener().setCompleted(false);
        ConsoleMenu menu = new ConsoleMenu(this);
        menu.init();
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
