package app.view.gui;

import app.model.beans.Characteristic;
import app.model.enums.DatabaseTypes;
import app.utils.GraphicsConfig;
import app.utils.Log;
import app.utils.Utils;
import app.utils.exceptions.WrongSelectedDatabaseException;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

class ResultPanelImpl extends JPanel implements Panel {
    private GuiView view;
    private String capacity;
    private String runtime;
    private String speed;
    private static final Logger LOG = Log.createLog(ResultPanelImpl.class);

    ResultPanelImpl(GuiView view) {
        this.view = view;
        initResultData();
    }

    private void initResultData() {
        LOG.info(Log.DATA_INIT_FOR_DISPLAYING);
        Characteristic characteristic = view.getEventListener().getModel().getCharacteristic();
        this.capacity = characteristic.getCapacity();
        this.runtime = characteristic.getRuntime();
        this.speed = characteristic.getSpeed();

        if (this.capacity == null ||
                this.runtime == null ||
                this.speed == null) {
            LOG.error(Log.DATA_DISPLAYING_ERROR);
            view.getEventListener().update();
        }
    }

    @Override
    public void init() {
        view.setSize(GuiView.WIDTH / 2 - 50, GuiView.WIDTH / 2 + 60);
        view.setLocationRelativeTo(null);
        view.getContentPane().add(this);

        setLayout(new FlowLayout(FlowLayout.CENTER, 35, 17));
        setBackground(Color.BLACK);
        setPreferredSize(view.getSize());

        paint();
        setVisible(true);
    }

    private void paint() {
        JLabel labelAction = createLabel(Log.RESULTS_GUI);
        Font font = new Font("Courier New", 2, 24);
        labelAction.setFont(font);
        labelAction.setForeground(Color.WHITE);

        JTextArea textArea = new JTextArea();
        textArea.setBackground(new Color(250, 212, 9));
        textArea.setFont(new Font("Arial", 3, 20));
        textArea.setLayout(new BorderLayout());
        textArea.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        textArea.setEditable(false);

        textArea.append(Log.RUNTIME + "   " + runtime + "\n");
        textArea.append(Log.CAPACITY + "  " + capacity + "\n");
        textArea.append(Log.SPEED + "      " + speed);
        add(textArea);
        LOG.info(Log.DATA_DISPLAYING_SUCCESS);

        JButton saveToFileButton = createButton(Log.SAVE_TO_FILE, Log.SAVE_FILE_BUTTON_MESSAGE);
        saveToFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    try {
                        view.getEventListener().writeToFile();
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(view, Log.WRITING_FILE_DATA_SUCCESS, Log.INFORMATION,
                                        JOptionPane.INFORMATION_MESSAGE)
                        );
                    } catch (IOException e1) {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(view, Log.FILE_DATA_DISPLAYING_ERROR, Log.ERROR,
                                JOptionPane.ERROR_MESSAGE)
                        );
                    }
                }).start();
            }
        });

        JButton addToDatabaseButton = createButton(Log.SAVE_TO_DB, Log.SAVE_DB_BUTTON_MESSAGE);
        addToDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu menu = new JPopupMenu();

                JMenuItem postgreItem = new JMenuItem(Utils.POSTGRESQL);
                postgreItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        writeToDatabase(DatabaseTypes.POSTGRESQL);
                    }
                });
                menu.add(postgreItem);

                JMenuItem mysqlItem = new JMenuItem(Utils.MYSQL);
                mysqlItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        writeToDatabase(DatabaseTypes.MYSQL);
                    }
                });
                menu.add(mysqlItem);

                JMenuItem oracleItem = new JMenuItem(Utils.ORACLE);
                oracleItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        writeToDatabase(DatabaseTypes.ORACLE);
                    }
                });
                menu.add(oracleItem);

                JMenuItem sqlServerItem = new JMenuItem(Utils.SQL_SERVER);
                sqlServerItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        writeToDatabase(DatabaseTypes.SQLSERVER);
                    }
                });
                menu.add(sqlServerItem);

                JMenuItem otherDbItem = new JMenuItem(Utils.OTHER_DBMS);
                otherDbItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        writeToDatabase(DatabaseTypes.OTHER);
                    }
                });
                otherDbItem.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                otherDbItem.setBackground(Color.YELLOW);
                menu.add(otherDbItem);

                menu.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                menu.setBorderPainted(true);
                menu.show(addToDatabaseButton, addToDatabaseButton.getWidth() / 2, addToDatabaseButton.getHeight() / 2);
            }
        });

        JButton updatePropertyFileButton = createButton(Log.UPDATE_FILE, Log.UPDATE_FILE_BUTTON_MESSAGE);
        updatePropertyFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    try {
                        int result = JOptionPane.showConfirmDialog(view, Log.CONFIRMATION_OF_UPDATE_PROPERTY_FILE, Log.QUESTION,
                                JOptionPane.YES_NO_OPTION);
                        if (result != JOptionPane.YES_OPTION) {
                            LOG.debug(Log.NO_OPTION_WHEN_UPDATE_THE_PROPERTY_FILE);
                            return;
                        }
                        view.getEventListener().updatePropertyFile();
                        LOG.info(Log.PROPERTY_FILE_UPDATED);
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(view, Log.PROPERTY_FILE_UPDATED, Log.INFORMATION,
                                        JOptionPane.INFORMATION_MESSAGE)
                        );
                    } catch (Exception e1) {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(view, Log.PROPERTY_READ_ERROR, Log.ERROR,
                                JOptionPane.ERROR_MESSAGE)
                        );
                    }
                }).start();
            }
        });

        JButton returnToMenuButton = createButton(Log.REPEAT_TEST, Log.REPEAT_TEST_BUTTON_MESSAGE);
        returnToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(view, Log.CONFIRMATION_OF_RETURNING_TO_MENU, Log.QUESTION,
                        JOptionPane.YES_NO_OPTION);
                if (result != JOptionPane.YES_OPTION) {
                    LOG.debug(Log.NO_OPTION_WHEN_RETURNING_TO_THE_MENU);
                    return;
                }
                view.getEventListener().update();
            }
        });

        JButton showGraphicsButton = createButton(Log.SHOW_GRAPHICS, Log.SHOW_GRAPHICS_BUTTON_MESSAGE);
        showGraphicsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!view.getEventListener().areGraphicsAvailableToPaint()) {
                    JOptionPane.showMessageDialog(view, "  " + Log.NUMBER_OF_POINTS_IS_NOT_ENOUGH, Log.ERROR,
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JPopupMenu menu = new JPopupMenu();
                JMenuItem capacityTimeItem = new JMenuItem(Log.CAPACITY_TIME);
                capacityTimeItem.addActionListener(
                        e1 -> buildGraphic(GraphicsConfig.CAPACITY_TIME_TYPE)
                );
                menu.add(capacityTimeItem);
                menu.addSeparator();

                JMenuItem speedTimeItem = new JMenuItem(Log.SPEED_TIME);
                speedTimeItem.addActionListener(
                        e1 -> buildGraphic(GraphicsConfig.SPEED_TIME_TYPE)
                );
                menu.add(speedTimeItem);

                menu.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                menu.setBorderPainted(true);
                menu.show(
                        showGraphicsButton,
                        showGraphicsButton.getWidth() / 2,
                        showGraphicsButton.getHeight() / 2
                );
            }
        });
    }

    private JLabel createLabel(String name) {
        JLabel topLabel = new JLabel(name);
        add(topLabel, BorderLayout.LINE_START);
        return topLabel;
    }

    private JButton createButton(String name, String message) {
        JButton jButton = new JButton(name, new ImageIcon(view.getButtonImage()));
        jButton.setPreferredSize(new Dimension(115, 65));
        jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton.setHorizontalTextPosition(JButton.CENTER);
        jButton.setFont(new Font("Futura", 2, 12));
        jButton.setForeground(Color.WHITE);
        jButton.setToolTipText(message);
        jButton.setBorder(new RoundedBorder(10));
        add(jButton, BorderLayout.WEST);
        revalidate();
        return jButton;
    }

    @Override
    public void update() {
        LOG.info(Log.RETURNING_TO_THE_MENU_SUCCESS);
        view.renderMenu(this);
    }

    private void writeToDatabase(DatabaseTypes type) {
        new Thread(() -> {
            try {
                view.getEventListener().writeToDatabase(type);
                JOptionPane.showMessageDialog(view, Log.WRITING_DATABASE_SUCCESS, Log.INFORMATION,
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e1) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(view, Log.WRITING_DATABASE_ERROR, Log.ERROR,
                                JOptionPane.ERROR_MESSAGE));
            } catch (WrongSelectedDatabaseException e) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(view, Log.WRONG_DATABASE_URL, Log.ERROR,
                        JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void buildGraphic(int type) {
        GraphicsPainter painter = new GraphicsPainter(view, type);
        painter.init();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(view.getBackgroundImage(), 0, 0, this);
    }
}
