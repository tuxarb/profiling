package app.view.gui;


import app.utils.Log;
import app.utils.exceptions.ClientProcessException;
import app.utils.exceptions.TheFileIsNotPropertiesException;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class MenuPanelImpl extends JPanel implements Panel {
    private GuiView guiView;
    private List<JButton> buttons = new ArrayList<>();
    private static volatile boolean isExceptionOccurred;
    private static final Logger LOG = Log.createLog(MenuPanelImpl.class);

    MenuPanelImpl(GuiView guiView) {
        this.guiView = guiView;
    }

    @Override
    public void init() {
        guiView.setSize(GuiView.HEIGHT / 2, GuiView.WIDTH / 2);
        guiView.setLocationRelativeTo(null);
        guiView.getContentPane().add(this);
        guiView.setColorOptionPane();

        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 30));
        setBackground(Color.BLACK);
        setSize(guiView.getSize());

        paint();
        setVisible(true);
    }

    private void paint() {
        Label menu = new Label(Log.MENU_GUI);
        Font font = new Font("Hiragino Kaku Gothic Pro", Font.ITALIC, 26);
        menu.setFont(font);
        menu.setForeground(Color.YELLOW);
        add(menu);

        JButton startingTest = createButton(Log.START_TEST, Log.START_TEST_BUTTON_MESSAGE);
        startingTest.addActionListener(e -> {
            if (!guiView.getEventListener().isPropertiesFileExists()) {
                LOG.error(Log.PROPERTIES_IS_NULL_LOG);
                JOptionPane.showMessageDialog(guiView, Log.PROPERTIES_IS_NULL_DIALOG, Log.ERROR, JOptionPane.ERROR_MESSAGE);
                return;
            }
            changeButtonState(false);
            updatePanel();
            findOutOS();
        });
        buttons.add(startingTest);

        JButton openingPropertyFile = createButton(Log.OPEN_PROPERTY_FILE, Log.OPEN_PROPERTY_BUTTON_MESSAGE);
        openingPropertyFile.addActionListener(e -> {
            new Thread(() -> {
                try {
                    guiView.getEventListener().readPropertyFile(guiView.getSelectedPropertyFile());
                } catch (TheFileIsNotPropertiesException e1) {
                    LOG.error(Log.PROPERTY_READ_ERROR + " " + Log.THE_FILE_IS_NOT_PROPERTIES);
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(guiView, Log.THE_FILE_IS_NOT_PROPERTIES, Log.ERROR, JOptionPane.ERROR_MESSAGE)
                    );
                } catch (Exception e2) {
                    LOG.warn(Log.CANCELLING_PROPERTY_FILE);
                }
            }).start();
        });
        buttons.add(openingPropertyFile);
    }

    private void changeButtonState(boolean isEnabled) {
        for (JButton jButton : buttons) {
            jButton.setEnabled(isEnabled);
        }
    }

    private void findOutOS() {
        new Thread(() -> {
            isExceptionOccurred = false;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            try {
                guiView.getEventListener().findOutOS(guiView.getOperatingSystem());
            } catch (ClientProcessException ex) {
                isExceptionOccurred = true;
                if (ex.getLocalizedMessage() != null && !ex.getLocalizedMessage().isEmpty()) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(guiView, ex.getLocalizedMessage(), Log.ERROR, JOptionPane.ERROR_MESSAGE)
                    );
                } else
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(guiView, Log.CLIENT_PROCESS_ERROR, Log.ERROR, JOptionPane.ERROR_MESSAGE)
                    );
            }
            changeButtonState(true);
        }).start();
    }

    private JButton createButton(String name, String message) {
        JButton jButton = new JButton(name, new ImageIcon(guiView.getButtonImage()));
        jButton.setPreferredSize(new Dimension(145, 65));
        jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton.setHorizontalTextPosition(JButton.CENTER);
        jButton.setFont(new Font("Apple Symbols", Font.TRUETYPE_FONT, 14));
        jButton.setForeground(Color.WHITE);
        jButton.setBorder(new RoundedBorder(10));
        jButton.setToolTipText(message);
        add(jButton, BorderLayout.CENTER);
        revalidate();
        return jButton;
    }

    private void updatePanel() {
        new Thread(() -> guiView.getEventListener().update())
                .start();
    }

    @Override
    public void update() {
        ProgressDialog progressDialog = new ProgressDialog(guiView);

        while (true) {
            if (isExceptionOccurred) {
                progressDialog.closeDialog();
                return;
            }
            if (guiView.getEventListener().isCompleted()) {
                progressDialog.closeDialog();
                break;
            }
        }
        guiView.getContentPane().remove(this);
        guiView.getContentPane().revalidate();
        ResultPanelImpl resultPanelImpl = new ResultPanelImpl(guiView);
        guiView.setPanel(resultPanelImpl);
        resultPanelImpl.init();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(guiView.getBackgroundImage(), 0, 0, this);
    }
}
