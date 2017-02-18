package app.view.gui;


import app.utils.Log;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.*;

class GraphicsPainter {
    private JFrame view;
    private final JDialog dialog = new JDialog(view, Log.GRAPHICS, false);
    private final int TYPE;
    private static final Logger LOG = Log.createLog(GraphicsPainter.class);

    GraphicsPainter(JFrame view, final int TYPE) {
        this.view = view;
        this.TYPE = TYPE;
    }

    void init() {
        dialog.setSize(
                Toolkit.getDefaultToolkit().getScreenSize().width - 100,
                Toolkit.getDefaultToolkit().getScreenSize().height - 100
        );
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        dialog.setVisible(true);
        paint();
    }

    private void paint() {
        Graphics g = dialog.getGraphics();
        g.drawRect(100, 100, 300, 300);
    }
}
