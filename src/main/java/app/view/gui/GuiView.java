package app.view.gui;

import app.controller.EventListener;
import app.model.enums.OperatingSystems;
import app.utils.Log;
import app.utils.Utils;
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
import java.io.InputStream;
import java.net.URL;

public class GuiView extends JFrame {
    private Panel panel;
    private EventListener eventListener;
    private Image backgroundImage;
    private BufferedImage buttonImage;
    private OperatingSystems operatingSystem;
    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    public GuiView() {
        super(Log.PROFILING);
    }

    public void initFrame() {
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setFocusable(true);
        JFrame.setDefaultLookAndFeelDecorated(true);
        setLookAndFeel();
        setIconImage();
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

    EventListener getEventListener() {
        return eventListener;
    }

    public Panel getPanel() {
        return panel;
    }

    void setBackgroundImage(String path, final Logger LOG) {
        try {
            InputStream fileAsStream = getStreamOfResourceFile(path);
            backgroundImage = ImageIO.read(fileAsStream);
        } catch (Exception e) {
            LOG.error(Log.SETTING_BACKGROUND_ERROR);
            getEventListener().exit();
        }
    }

    void setButtonImage(String path, final Logger LOG) {
        try {
            InputStream fileAsStream = getStreamOfResourceFile(path);
            buttonImage = ImageIO.read(fileAsStream);
        } catch (Exception e) {
            LOG.error(Log.SETTING_BUTTON_IMAGE_ERROR);
            getEventListener().exit();
        }
    }

    private void setIconImage() {
        URL resource = getClass().getResource(Utils.ICON_IMAGE);
        if (resource != null) {
            this.setIconImage(new ImageIcon(resource).getImage());
        }
    }

    Image getBackgroundImage() {
        return backgroundImage;
    }

    Image getButtonImage() {
        return buttonImage;
    }

    private void setLookAndFeel() {
        UIManager.put("OptionPane.background", new Color(15, 5, 5));
        UIManager.put("Panel.background", new Color(15, 5, 5));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("OptionPane.messageFont", new Font("Modern No. 20", 4, 12));
        UIManager.put("Button.background", new Color(190, 190, 190));
    }

    File getSelectedPropertyFile() throws FileNotFoundException {
        JFileChooser fileOpen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.properties", "properties");
        fileOpen.setAcceptAllFileFilterUsed(false);
        fileOpen.setFileFilter(filter);
        fileOpen.setDialogTitle(Log.OPEN_PROPERTY_FILE);
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
