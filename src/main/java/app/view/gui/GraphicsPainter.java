package app.view.gui;

import app.model.PointsList;
import app.utils.Log;
import app.utils.Utils;
import org.slf4j.Logger;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

import static app.utils.GraphicsConfig.CAPACITY_TIME_TYPE;

class GraphicsPainter extends JDialog {
    private double gridX;
    private double gridY;
    private final PointsList points;
    private final CoordinatesPainter painter;
    private final int TYPE;
    private static final int BORDER_GAP = 60;
    private static final Logger LOG = Log.createLog(GraphicsPainter.class);

    GraphicsPainter(GuiView view, final int TYPE) {
        this.TYPE = TYPE;
        this.points = view.getEventListener().getPoints();
        this.painter = new CoordinatesPainter();
    }

    void init() {
        LOG.debug(Log.GRAPHIC_PAINTER_STARTED, getMessageDependingOnType());
        setTitle(String.format(Log.GRAPHICS + " <%s>", getMessageDependingOnType()));
        setSize(getScreenWidth(), getScreenHeight());
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    painter.clear();
                    return;
                }
                double pointX = e.getPoint().getX();
                double pointY = e.getPoint().getY();
                if (isPointOutOfBounds(pointX, pointY)) {
                    painter.clear();
                    return;
                }
                painter.setX((pointX - BORDER_GAP) * gridX);
                painter.setY((getHeight() - BORDER_GAP - pointY) * gridY);
                painter.paint();
            }
        });
        setVisible(true);
    }

    private boolean isPointOutOfBounds(double x, double y) {
        boolean isCapacityTimeType = TYPE == CAPACITY_TIME_TYPE;
        return x > computeX(isCapacityTimeType ? points.getLast() : points.getPointWithMaxSpeed()) ||
                y < computeY(points.getLast()) ||
                x < BORDER_GAP ||
                y > getHeight() - BORDER_GAP;
    }

    public void paint(Graphics g) {
        super.paint(g);
        painter.setGraphics((Graphics2D) g.create());
        setIgnoreRepaint(true);
        paintBackground(((Graphics2D) g));
        drawAxes((Graphics2D) g);
        drawFunc((Graphics2D) g);
        LOG.debug(Log.GRAPHIC_PAINTER_ENDED, getMessageDependingOnType());
    }

    private void paintBackground(Graphics2D g) {
        GradientPaint layer1 = new GradientPaint(
                0, getHeight() / 2, new Color(12, 18, 11),
                getWidth(), getHeight(), new Color(32, 32, 38));
        g.setPaint(layer1);
        g.fillRect(0, getHeight() / 2, getWidth(), getHeight() / 2);
        GradientPaint layer2 = new GradientPaint(
                getWidth(), getHeight(), new Color(32, 32, 38),
                getWidth() - BORDER_GAP, 0, new Color(15, 5, 5));
        g.setPaint(layer2);
        g.fillRect(getWidth() - (BORDER_GAP + 30), 0, 2 * BORDER_GAP, getHeight());
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
        int marksCount = 19;
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

        GradientPaint gridAreaColor = new GradientPaint(
                BORDER_GAP, (float) (getHeight() - BORDER_GAP - marksCount * stepY), new Color(50, 50, 90),
                (float) (BORDER_GAP + marksCount * stepX), getHeight() - BORDER_GAP, new Color(63, 63, 27)
        );
        g.setPaint(gridAreaColor);
        g.fill(new Rectangle2D.Double(
                BORDER_GAP, getHeight() - BORDER_GAP - marksCount * stepY,
                marksCount * stepX, marksCount * stepY)
        );

        Color gridColor = new Color(75, 75, 75);
        g.setColor(gridColor);
        g.setStroke(new BasicStroke(
                1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, new float[]{5.0f}, 0.0f)
        );
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
            maxCapacityOrSpeed = new BigDecimal(points.getMaxSpeed());
        }
        long maxRuntime = lastPoint.getRuntime();

        BigDecimal stepByCapacityOrSpeed = maxCapacityOrSpeed.divide(
                BigDecimal.valueOf(marksCount * 1024), //1024 - convert to Mbyte or Mbyte/s
                16,
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
            if (maxCapacityOrSpeed.toString().length() < 12) {
                g.drawString(stepByCapacityOrSpeed.multiply(
                        BigDecimal.valueOf(i))
                                .setScale(1, RoundingMode.HALF_EVEN)
                                .toString(),
                        (float) (BORDER_GAP + i * stepX - 20),
                        i % 2 == 1 ?
                                (float) (getHeight() - BORDER_GAP + 20) :
                                (float) (getHeight() - BORDER_GAP + 33)
                );
            } else {
                g.drawString(stepByCapacityOrSpeed.multiply(
                        BigDecimal.valueOf(i))
                                .setScale(0, RoundingMode.HALF_EVEN)
                                .toString(),
                        (float) (BORDER_GAP + i * stepX - BORDER_GAP * 0.6),
                        i % 2 == 1 ?
                                (float) (getHeight() - BORDER_GAP + 20) :
                                (float) (getHeight() - BORDER_GAP + 33)
                );
            }
        }
        this.gridX = stepByCapacityOrSpeed.divide(
                BigDecimal.valueOf(stepX), 16, RoundingMode.HALF_EVEN)
                .doubleValue();
        this.gridY = stepByRuntime / stepY;
    }

    private void drawFunc(Graphics2D g) {
        int countSplits = getCountSplitsDependingOnCountPoints();
        Color funcColor = new Color(0, 8, 255);
        g.setStroke(new BasicStroke(5.5f));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setColor(funcColor);
        for (int i = 0; i < points.size() - 1; i++) {
            PointsList.Point firstPoint = points.get(i);
            PointsList.Point secondPoint = points.get(i + 1);
            double startPointX = computeX(firstPoint);
            double startPointY = computeY(firstPoint);
            double stepByX = (computeX(secondPoint) - startPointX) / countSplits;
            double stepByY = (computeY(secondPoint) - startPointY) / countSplits;
            for (int j = 0; j <= countSplits - 1; j++) {
                g.draw(new Line2D.Double(
                        startPointX + j * stepByX, startPointY + j * stepByY,
                        startPointX + (j + 1) * stepByX, startPointY + (j + 1) * stepByY)
                );
            }
        }
        Color boundaryPointsColor = new Color(255, 0, 0);
        double boundSize = 3.5;
        g.setColor(boundaryPointsColor);
        g.draw(new Ellipse2D.Double(
                BORDER_GAP - boundSize / 2,
                getHeight() - BORDER_GAP - boundSize / 2,
                boundSize,
                boundSize)
        );
        g.draw(new Ellipse2D.Double(
                computeX(points.getLast()) - boundSize,
                computeY(points.getLast()) - boundSize / 2,
                boundSize,
                boundSize)
        );
    }

    private int getCountSplitsDependingOnCountPoints() {
        int countSplits;
        int countPoints = points.size();
        if (countPoints < 6) {
            countSplits = countPoints * 100;
        } else if (countPoints < 12) {
            countSplits = countPoints * 25;
        } else if (countPoints < 25) {
            countSplits = countPoints * 7;
        } else if (countPoints < 50) {
            countSplits = countPoints * 3;
        } else if (countPoints < 75) {
            countSplits = 100;
        } else if (countPoints < 100) {
            countSplits = 75;
        } else if (countPoints < 250) {
            countSplits = 25;
        } else if (countPoints < 500) {
            countSplits = 7;
        } else if (countPoints < 1000) {
            countSplits = 3;
        } else {
            countSplits = 1;
        }
        return countSplits;
    }

    private double computeX(PointsList.Point point) {
        if (this.TYPE == CAPACITY_TIME_TYPE) {
            return BORDER_GAP + point.getCapacity().doubleValue() / (1024 * gridX);
        } else {
            return BORDER_GAP + point.getSpeed().doubleValue() / (1024 * gridX);
        }
    }

    private double computeY(PointsList.Point point) {
        return getHeight() - BORDER_GAP - point.getRuntime() / gridY;
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

    private int getScreenWidth() {
        return Toolkit.getDefaultToolkit().getScreenSize().width - 100;
    }

    private int getScreenHeight() {
        return Toolkit.getDefaultToolkit().getScreenSize().height - 100;
    }

    private class CoordinatesPainter {
        private String x;  // a capacity or a speed coordinate
        private String y;  // a runtime coordinate
        private int outputWidth;
        private Graphics2D g;
        private final int OUTPUT_HEIGHT = 40;
        private final int RIGTH_GAP = 30;

        private void paint() {
            if (g == null) {
                return;
            }
            String capacityOrSpeedText = (TYPE == CAPACITY_TIME_TYPE ? "capacity=" : "speed=") + x;
            String runtimeText = "runtime=" + y;
            outputWidth = g.getFontMetrics().stringWidth(
                    capacityOrSpeedText.length() > runtimeText.length() ? capacityOrSpeedText : runtimeText
            );
            g.setColor(Color.RED);
            g.setFont(new Font("Courier New", 2, 14));
            clear();
            g.drawString(capacityOrSpeedText, BORDER_GAP + RIGTH_GAP, BORDER_GAP - BORDER_GAP / 2 + 15);
            g.drawString(runtimeText, BORDER_GAP + RIGTH_GAP, BORDER_GAP - BORDER_GAP / 4 + 20);
        }

        private void clear() {
            if (g == null) {
                return;
            }
            g.clearRect(
                    BORDER_GAP + RIGTH_GAP - 5,
                    BORDER_GAP - BORDER_GAP / 2,
                    outputWidth + 200,
                    OUTPUT_HEIGHT
            );
        }

        private void setX(double x) {
            String remainderX = String.valueOf(x - Math.floor(x)).substring(1, 3);
            this.x = Utils.formatNumber((long) x, Locale.CANADA_FRENCH) + remainderX;
        }

        private void setY(double y) {
            this.y = formatRuntime(Math.round(y));
        }

        private void setGraphics(Graphics2D g) {
            this.g = g;
            this.g.setBackground((Color) UIManager.get("Panel.background"));
        }
    }
}
