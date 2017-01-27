package app.view.gui;

import app.controller.EventListener;
import app.model.enums.OperatingSystems;
import app.utils.Log;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class View extends JFrame {
    private Panel panel;
    private EventListener eventListener;
    private Image backgroundImage;
    private BufferedImage buttonImage;
    private OperatingSystems operatingSystem;
    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    public View() {
        super(Log.PROFILING);
    }

    public void initFrame() {
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setFocusable(true);
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                getEventListener().exit();
            }
        });
        setVisible(true);
    }

    public void update() {
        panel.update();
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    synchronized EventListener getEventListener() {
        return eventListener;
    }

    public Panel getPanel() {
        return panel;
    }

    void setBackgroundImage(String path, final Logger LOG) {
        try {
            InputStream fileAsStream = getStreamOfResourceFile(path);
            backgroundImage = ImageIO.read(fileAsStream);
        } catch (IOException e) {
            LOG.warn(Log.SETTING_BACKGROUND_ERROR);
            e.printStackTrace();
        }
    }

    void setButtonImage(String path, final Logger LOG) {
        try {
            InputStream fileAsStream = getStreamOfResourceFile(path);
            buttonImage = ImageIO.read(fileAsStream);
        } catch (IOException e) {
            LOG.warn(Log.SETTING_BUTTON_IMAGE_ERROR);
            e.printStackTrace();
        }
    }

    Image getBackgroundImage() {
        return backgroundImage;
    }

    Image getButtonImage() {
        return buttonImage;
    }

    void setColorOptionPane() {
        UIManager.put("OptionPane.background", new Color(10, 5, 5));
        UIManager.put("Panel.background", new Color(10, 5, 5));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
    }

    File getSelectedPropertyFile() throws FileNotFoundException {
        JFileChooser fileOpen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.properties", "properties");
        fileOpen.setAcceptAllFileFilterUsed(false);
        fileOpen.setFileFilter(filter);
        fileOpen.setDialogTitle("Open file");
        int result = fileOpen.showOpenDialog(this);
        if (result == JFileChooser.CANCEL_OPTION)
            throw new FileNotFoundException();
        return fileOpen.getSelectedFile();
    }

    InputStream getStreamOfResourceFile(String path) {
        return getClass().getResourceAsStream(path);
    }

    void renderMenu(JPanel jPanel) {
        if (operatingSystem == null) {
            return;
        }
        getEventListener().setCompleted(false);
        getContentPane().remove(jPanel);
        getContentPane().revalidate();
        MenuPanelImpl menuPanelImpl = new MenuPanelImpl(this);
        setPanel(menuPanelImpl);
        menuPanelImpl.init();
    }

    OperatingSystems getOperatingSystem() {
        return operatingSystem;
    }

    void setOperatingSystem(OperatingSystems operatingSystem) {
        this.operatingSystem = operatingSystem;
    }
}
