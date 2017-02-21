package app.view.gui;

import app.model.PointsList;
import app.utils.Log;
import app.utils.Utils;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

import static app.utils.GraphicsConfig.CAPACITY_TIME_TYPE;

class GraphicsPainter extends JDialog {
    private final PointsList points;
    private final int TYPE;
    private static final int BORDER_GAP = 60;
    private static final Logger LOG = Log.createLog(GraphicsPainter.class);

    GraphicsPainter(GuiView view, final int TYPE) {
        this.TYPE = TYPE;
        this.points = view.getEventListener().getPoints();
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
        g.drawString("s", BORDER_GAP - 15, BORDER_GAP + 4);
        if (this.TYPE == CAPACITY_TIME_TYPE) {
            g.drawString("Mb", getWidth() - BORDER_GAP + 5, getHeight() - BORDER_GAP + 5);
        } else {
            g.drawString("Mb/s", getWidth() - BORDER_GAP + 5, getHeight() - BORDER_GAP + 5);
        }
        g.setColor(Color.WHITE);
        g.drawString("0", BORDER_GAP - 20, getHeight() - BORDER_GAP + 15);
    }

    private void createHatchMarksAndGrid(Graphics2D g) {
        int markSize = 5;
        int marksCount = 21;
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
        labelHatchMarks(g, marksCount, stepX, stepY);
    }

    private void labelHatchMarks(Graphics2D g, int marksCount, double stepX, double stepY) {
        PointsList.Point lastPoint = points.getLast();
        BigDecimal maxCapacityOrSpeed;
        if (this.TYPE == CAPACITY_TIME_TYPE) {
            maxCapacityOrSpeed = new BigDecimal(lastPoint.getCapacity());
        } else {
            maxCapacityOrSpeed = new BigDecimal(lastPoint.getSpeed());
        }
        long maxRuntime = lastPoint.getRuntime();

        BigDecimal stepByCapacityOrSpeed = maxCapacityOrSpeed.divide(
                BigDecimal.valueOf(marksCount * 1024), //1024 - convert to MB
                2,
                RoundingMode.HALF_EVEN
        );
        double stepByRuntime = (double) maxRuntime / marksCount;

        g.setColor(Color.WHITE);
        g.setFont(new Font("Courier New", 4, 11));
        for (int i = 1; i <= marksCount; i++) {
            g.drawString(
                    formatRuntime(Math.round(i * stepByRuntime)),
                    (float) BORDER_GAP * 0.15f,
                    (float) (getHeight() - BORDER_GAP - i * stepY + 5)
            );
            g.drawString(
                    stepByCapacityOrSpeed.multiply(
                            BigDecimal.valueOf(i))
                            .setScale(0, RoundingMode.CEILING)
                            .toString(),
                    (float) (BORDER_GAP + i * stepX - 10),
                    i % 2 == 1 ?
                            (float) (getHeight() - BORDER_GAP + 20) :
                            (float) (getHeight() - BORDER_GAP + 33)
            );
        }
    }

    private String formatRuntime(long runtime) {
        if (runtime >= 100_000) //longer than 100 s
            return Utils.getStringWithoutLastChars(Utils.formatNumber(runtime, Locale.GERMAN), 1);
        if (runtime >= 1_000_000) //longer than 1000 s
            return Utils.getStringWithoutLastChars(Utils.formatNumber(runtime, Locale.GERMAN), 2);
        if (runtime >= 10_000_000) //longer than 10000 s
            return String.valueOf(runtime / 1000);
        return runtime < 1000 ? "0." + runtime : Utils.formatNumber(runtime, Locale.GERMAN);
    }

    private String getMessageDependingOnType() {
        return this.TYPE == CAPACITY_TIME_TYPE ? Log.CAPACITY_TIME : Log.SPEED_TIME;
    }
}
