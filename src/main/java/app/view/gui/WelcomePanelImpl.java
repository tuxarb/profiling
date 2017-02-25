package app.view.gui;

import app.model.enums.OperatingSystems;
import app.utils.Log;
import app.utils.Utils;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class WelcomePanelImpl extends JPanel implements Panel {
    private GuiView view;
    private static final Logger LOG = Log.createLog(WelcomePanelImpl.class);

    public WelcomePanelImpl(GuiView view) {
        this.view = view;
        view.setBackgroundImage(Utils.BACKGROUND_IMAGE, LOG);
        view.setButtonImage(Utils.BUTTON_IMAGE, LOG);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(view.getBackgroundImage(), 0, 0, this);
    }

    @Override
    public void init() {
        setSize(view.getSize());
        setLayout(new FlowLayout(FlowLayout.CENTER, 500, 40));
        view.getContentPane().add(this);
        paint();
        setVisible(true);
        LOG.debug(Log.APP_IS_READY);
    }

    private void paint() {
        JLabel labWelcome = createLabel(Log.WELCOME_GUI);
        Font font = new Font("Hiragino Kaku Gothic Pro", Font.ITALIC, 36);
        labWelcome.setFont(font);
        labWelcome.setForeground(Color.YELLOW);

        JLabel labelAction = createLabel(Log.CHOICE_OS);
        font = new Font("Courier New", 4, 24);
        labelAction.setFont(font);
        labelAction.setForeground(Color.WHITE);


        JButton windowsButton = createButton("WINDOWS");
        windowsButton.addActionListener(new ActionHandler());
        windowsButton.setActionCommand("W");

        JButton linuxButton = createButton("LINUX");
        linuxButton.addActionListener(new ActionHandler());
        linuxButton.setActionCommand("L");

        JButton macButton = createButton("MAC OS");
        macButton.addActionListener(new ActionHandler());
        macButton.setActionCommand("M");
    }

    private JButton createButton(String name) {
        JButton jButton = new JButton(name, new ImageIcon(view.getButtonImage()));
        jButton.setPreferredSize(new Dimension(100, 70));
        jButton.setHorizontalTextPosition(JButton.CENTER);
        jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton.setFont(new Font("Eurostile", 4, 12));
        jButton.setForeground(Color.WHITE);
        jButton.setBorder(new RoundedBorder(5));
        add(jButton);
        revalidate();
        return jButton;
    }

    private JLabel createLabel(String name) {
        JLabel topLabel = new JLabel(name);
        add(topLabel);
        return topLabel;
    }

    @Override
    public void update() {
        view.renderMenu(this);
    }

    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            switch (command) {
                case "W":
                    if (!Utils.isWindows()) {
                        showMessageAboutError(Log.NOT_WINDOWS_OS);
                        return;
                    }
                    updateView(OperatingSystems.WINDOWS);
                    break;
                case "L":
                    if (!Utils.isLinux()) {
                        showMessageAboutError(Log.NOT_LINUX_OS);
                        return;
                    }
                    updateView(OperatingSystems.LINUX);
                    break;
                case "M":
                    if (!Utils.isMac()) {
                        showMessageAboutError(Log.NOT_MAC_OS);
                        return;
                    }
                    updateView(OperatingSystems.MAC);
                    break;
            }
        }

        private void updateView(OperatingSystems operatingSystem) {
            LOG.debug(Log.VALID_OS);
            view.setOperatingSystem(operatingSystem);
            view.getEventListener().update();
        }

        private void showMessageAboutError(String s) {
            LOG.warn(Log.WRONG_OS);
            JOptionPane.showMessageDialog(view, s, Log.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}
