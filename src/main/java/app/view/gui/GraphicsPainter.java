package app.view.gui;

import app.utils.Log;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import static app.utils.GraphicsConfig.*;

class GraphicsPainter extends JDialog {
    private final int TYPE;
    private static final int BORDER_GAP = 40;
    private static final Logger LOG = Log.createLog(GraphicsPainter.class);

    GraphicsPainter(final int TYPE) {
        this.TYPE = TYPE;
    }

    void init() {
        LOG.debug(Log.GRAPHIC_PAINTER_STARTED, getMessageDependingOnType());
        setTitle(String.format(Log.GRAPHICS + " <%s>", getMessageDependingOnType()));
        setSize(
                Toolkit.getDefaultToolkit().getScreenSize().width - 100,
                Toolkit.getDefaultToolkit().getScreenSize().height - 100
        );
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawAxes((Graphics2D) g);
    }

    private void drawAxes(Graphics2D g) {
        Color axesColor = Color.WHITE;
        g.setColor(axesColor);
        g.setStroke(new BasicStroke(2));
        g.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
        g.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);
        drawArrows(g);
        labelSignatures(g);
        createHatchMarksAndGrid(g);
    }

    private void drawArrows(Graphics2D g) {
        int arrowSize = 5;
        g.drawLine(BORDER_GAP - arrowSize, BORDER_GAP + arrowSize, BORDER_GAP, BORDER_GAP);
        g.drawLine(BORDER_GAP, BORDER_GAP, BORDER_GAP + arrowSize, BORDER_GAP + arrowSize);
        g.drawLine(getWidth() - BORDER_GAP, getHeight() - BORDER_GAP,
                getWidth() - BORDER_GAP - arrowSize, getHeight() - BORDER_GAP - arrowSize);
        g.drawLine(getWidth() - BORDER_GAP, getHeight() - BORDER_GAP,
                getWidth() - BORDER_GAP - arrowSize, getHeight() - BORDER_GAP + arrowSize);
    }

    private void labelSignatures(Graphics2D g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Courier New", 2, 16));
        g.drawString("s", BORDER_GAP - 15, BORDER_GAP + 8);
        if (this.TYPE == CAPACITY_TIME_TYPE) {
            g.drawString("kB", getWidth() - BORDER_GAP - 10, getHeight() - BORDER_GAP + 15);
        } else {
            g.drawString("kB/s", getWidth() - BORDER_GAP - 15, getHeight() - BORDER_GAP + 15);
        }
        g.setColor(Color.WHITE);
        g.drawString("0", BORDER_GAP - 12, getHeight() - BORDER_GAP + 12);
    }

    private void createHatchMarksAndGrid(Graphics2D g) {
        int markSize = 5;
        int marksCount = 20;
        int extra = 15;
        int graphicWidth = getWidth() - BORDER_GAP * 2 - extra;
        int graphicHeight = getHeight() - BORDER_GAP * 2 - extra;
        double stepX = graphicWidth / marksCount;
        double stepY = graphicHeight / marksCount;

        Color markColor = Color.BLUE;
        g.setColor(markColor);
        for (int i = 1; i <= marksCount; i++) {
            g.draw(new Line2D.Double(
                    BORDER_GAP - markSize, getHeight() - BORDER_GAP - i * stepY,
                    BORDER_GAP + markSize, getHeight() - BORDER_GAP - i * stepY)
            );
            g.draw(new Line2D.Double(
                    BORDER_GAP + i * stepX, getHeight() - BORDER_GAP - markSize,
                    BORDER_GAP + i * stepX, getHeight() - BORDER_GAP + markSize)
            );
        }

        Color gridAreaColor = new Color(60, 60, 60);
        g.setColor(gridAreaColor);
        g.fill(new Rectangle2D.Double(
                BORDER_GAP, getHeight() - BORDER_GAP - marksCount * stepY,
                marksCount * stepX, marksCount * stepY)
        );

        Color gridColor = new Color(50, 50, 50);
        g.setColor(gridColor);
        g.setStroke(new BasicStroke(1));
        for (int i = 1; i <= marksCount; i++) {
            g.draw(new Line2D.Double(
                    BORDER_GAP + i * stepX, getHeight() - BORDER_GAP,
                    BORDER_GAP + i * stepX, getHeight() - BORDER_GAP - marksCount * stepY)
            );
            g.draw(new Line2D.Double(
                    BORDER_GAP, getHeight() - BORDER_GAP - i * stepY,
                    BORDER_GAP + marksCount * stepX, getHeight() - BORDER_GAP - i * stepY)
            );
        }
    }

    private String getMessageDependingOnType() {
        return this.TYPE == CAPACITY_TIME_TYPE ? Log.CAPACITY_TIME : Log.SPEED_TIME;
    }
}
