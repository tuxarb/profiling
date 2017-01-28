package app.view.gui;


import app.utils.Log;
import app.utils.exceptions.ClientProcessException;
import org.slf4j.Logger;

import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.util.*;

class MenuPanelImpl extends JPanel implements Panel {
    private View view;
    private List<JButton> buttons = new ArrayList<>();
    private static volatile boolean isExceptionOccurred;
    private static final Logger LOG = Log.createLog(MenuPanelImpl.class);

    MenuPanelImpl(View view) {
        this.view = view;
    }

    @Override
    public void init() {
        view.setSize(View.HEIGHT / 2, View.WIDTH / 2);
        view.setLocationRelativeTo(null);
        view.getContentPane().add(this);
        view.setColorOptionPane();

        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 30));
        setBackground(Color.BLACK);
        setSize(view.getSize());

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
            if (!view.getEventListener().isPropertiesFileExists()) {
                LOG.error(Log.PROPERTIES_IS_NULL);
                JOptionPane.showMessageDialog(view, Log.PROPERTIES_IS_NULL, Log.ERROR, JOptionPane.ERROR_MESSAGE);
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
                    view.getEventListener().readPropertyFile(view.getSelectedPropertyFile());
                } catch (Exception e1) {
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
                e.printStackTrace();
            }
            try {
                view.getEventListener().findOutOS(view.getOperatingSystem());
            } catch (ClientProcessException ex) {
                isExceptionOccurred = true;
                if (ex.getLocalizedMessage() != null && !ex.getLocalizedMessage().isEmpty()) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(view, ex.getLocalizedMessage(), Log.ERROR, JOptionPane.ERROR_MESSAGE)
                    );
                } else
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(view, Log.CLIENT_PROCESS_ERROR, Log.ERROR, JOptionPane.ERROR_MESSAGE)
                    );
            }
            changeButtonState(true);
        }).start();
    }

    private JButton createButton(String name, String message) {
        JButton jButton = new JButton(name, new ImageIcon(view.getButtonImage()));
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
        new Thread(() -> view.getEventListener().update())
                .start();
    }

    @Override
    public void update() {
        ProgressDialog progressDialog = new ProgressDialog(view);

        while (true) {
            if (isExceptionOccurred) {
                progressDialog.closeDialog();
                return;
            }
            if (view.getEventListener().isCompleted()) {
                progressDialog.closeDialog();
                break;
            }
        }
        view.getContentPane().remove(this);
        view.getContentPane().revalidate();
        ResultPanelImpl resultPanelImpl = new ResultPanelImpl(view);
        view.setPanel(resultPanelImpl);
        resultPanelImpl.init();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(view.getBackgroundImage(), 0, 0, this);
    }
}
