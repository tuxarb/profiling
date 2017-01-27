package app.view.gui;

import app.utils.exceptions.WrongSelectedDatabaseException;
import app.model.enums.DatabaseTypes;
import app.utils.Log;
import app.utils.Utils;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

class ResultPanelImpl extends JPanel implements Panel {
    private View view;
    private String capacity;
    private String runtime;
    private String speed;
    private static final Logger LOG = Log.createLog(ResultPanelImpl.class);

    ResultPanelImpl(View view) {
        this.view = view;
        initResultData();
    }

    private void initResultData() {
        LOG.info(Log.DATA_INIT_FOR_DISPLAY);
        this.capacity = view.getEventListener().getModel().getCharacteristic().getCapacity();
        this.runtime = view.getEventListener().getModel().getCharacteristic().getRuntime();
        this.speed = view.getEventListener().getModel().getCharacteristic().getSpeed();

        if (this.capacity == null ||
                this.runtime == null ||
                this.speed == null) {
            LOG.error(Log.DATA_DISPLAY_ERROR);
            view.getEventListener().update();
        }
    }

    @Override
    public void init() {
        view.setSize(View.WIDTH / 2 - 50, View.WIDTH / 2 + 60);
        view.setLocationRelativeTo(null);
        view.getContentPane().add(this);
        view.setColorOptionPane();

        setLayout(new FlowLayout(FlowLayout.CENTER, 35, 17));
        setBackground(Color.BLACK);
        setSize(view.getSize());

        paint();
        setVisible(true);
    }

    private void paint() {
        JLabel labelAction = createLabel(Log.RESULTS);
        Font font = new Font("Arial", Font.LAYOUT_NO_LIMIT_CONTEXT, 24);
        labelAction.setFont(font);
        labelAction.setForeground(Color.WHITE);

        JTextArea textArea = new JTextArea();
        textArea.setBackground(Color.ORANGE);
        textArea.setFont(new Font("Arial", Font.PLAIN, 20));
        textArea.setLayout(new BorderLayout());
        textArea.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        textArea.setEditable(false);

        textArea.append(Log.RUNTIME + " \t" + runtime + "\n");
        textArea.append(Log.CAPACITY + " \t" + capacity + "\n");
        textArea.append(Log.SPEED + " \t" + speed);
        add(textArea);
        LOG.info(Log.DATA_DISPLAY_SUCCESS);

        JButton print = createButton(Log.SAVE_TO_FILE, Log.SAVE_FILE_BUTTON_MESSAGE);
        print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    try {
                        view.getEventListener().writeToFile();
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(view, Log.FILE_DATA_DISPLAY_SUCCESS, Log.INFORMATION,
                                        JOptionPane.INFORMATION_MESSAGE)
                        );
                    } catch (IOException e1) {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(view, Log.FILE_DATA_DISPLAY_ERROR, Log.ERROR,
                                JOptionPane.ERROR_MESSAGE)
                        );
                    }
                }).start();
            }
        });

        JButton addToDatabase = createButton(Log.SAVE_TO_DB, Log.SAVE_DB_BUTTON_MESSAGE);
        addToDatabase.addActionListener(new ActionListener() {
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
                menu.show(addToDatabase, addToDatabase.getWidth() / 2, addToDatabase.getHeight() / 2);
            }
        });

        JButton updatePropertyFile = createButton(Log.UPDATE_FILE, Log.UPDATE_FILE_BUTTON_MESSAGE);
        updatePropertyFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    try {
                        view.getEventListener().updatePropertyFile();
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(view, Log.PROPERTY_UPDATE, Log.INFORMATION,
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

        JButton returningOnMenu = createButton(Log.REPEAT_TEST, Log.REPEAT_TEST_BUTTON_MESSAGE);
        returningOnMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(view, Log.RETURNING_TO_MENU, Log.QUESTION,
                        JOptionPane.YES_NO_OPTION);
                if (result != JOptionPane.YES_OPTION) {
                    return;
                }
                view.getEventListener().update();
            }
        });

        JButton exit = createButton(Log.EXIT, Log.EXIT_BUTTON_MESSAGE);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() ->
                        view.getEventListener().exit()
                ).start();
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
        jButton.setFont(new Font("Apple Symbols", Font.TRUETYPE_FONT, 12));
        jButton.setForeground(Color.WHITE);
        jButton.setToolTipText(message);
        jButton.setBorder(new RoundedBorder(10));
        add(jButton, BorderLayout.WEST);
        revalidate();
        return jButton;
    }

    @Override
    public void update() {
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(view.getBackgroundImage(), 0, 0, this);
    }
}
