package com.weaxme.graph.component.panel;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.weaxme.graph.application.IGraphApplication;
import com.weaxme.graph.application.graph.PixelCoordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Iterator;
import java.util.List;

import com.weaxme.graph.component.util.GraphPaintUtil;

/**
 * @author Vitaliy Gonchar
 */
@Singleton
public class GraphPanel extends JPanel implements IGraphPanel {

    @Inject
    private IGraphApplication app;

    private static final Color GRAPH_COLOR = Color.RED;
    private static final Color ZERO_COLOR  = Color.CYAN;
    private static final Color TEXT_COLOR  = Color.BLACK;

    private int markLength;

    private final List<PixelCoordinate> points1 = Lists.newArrayList();
    private final List<PixelCoordinate> points2 = Lists.newArrayList();

    private final List<PixelCoordinate> xZeroes = Lists.newArrayList();
    private final List<PixelCoordinate> yZeroes = Lists.newArrayList();

    private final Object lock = new Object();

    private static final int AXIS_WIDTH = 1;

    public GraphPanel() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                clearAndRepaint();
                app.getGraphUpdater().setDelayForBuild(0);
                app.repaintGraph(0);
            }
        });
    }

    @Override
    public void clearAndRepaint() {
        synchronized (lock) {
            points1.clear();
            points2.clear();
            xZeroes.clear();
            yZeroes.clear();
            repaint();
        }
    }

    @Override
    public void addVectorAndRepaint(PixelCoordinate point1, PixelCoordinate point2) {
        if (point1 == null)
            throw new IllegalArgumentException("point1 can't be null!");
        if (point2 == null)
            throw new IllegalArgumentException("point2 can't be null!");
        synchronized (lock) {
            points1.add(point1);
            points2.add(point2);
            repaint();
        }
    }

    @Override
    public void addZeroPointsAndRepaint(PixelCoordinate zero, boolean yAxis) {
        if (zero == null)
            throw new IllegalArgumentException("zero can't be null!");
        synchronized (lock) {
            if (yAxis) {
                if (!yZeroes.contains(zero)) yZeroes.add(zero);
            } else {
                if (!xZeroes.contains(zero)) xZeroes.add(zero);
            }
            repaint();
        }
    }


    @Override
    public IGraphApplication getApplication() {
        return app;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2d = (Graphics2D) graphics;
        app.setX0(getWidth() / 2)
                .setY0(getHeight() / 2)
                .setGraphMaxHeight(getHeight() - app.getBorderPixelLimit())
                .setGraphMaxWidth(getWidth() - app.getBorderPixelLimit());

        markLength = app.getMarkLength() / 2;
        buildAxis(g2d);
        buildVector(g2d);
        buildZeroes(g2d);
    }

    private void buildAxis(Graphics2D g2d) {
        g2d.setPaint(Color.BLACK);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawLine(app.getX0(), app.getBorderPixelLimit(), app.getX0(), app.getGraphMaxHeight());
        g2d.drawLine(app.getBorderPixelLimit(), app.getY0(), app.getGraphMaxWidth(), app.getY0());
        buildMarksOnX(g2d);
        buildMarksOnY(g2d);
        buildGraphBorder(g2d);
        buildAxisArrows(g2d);
    }

    private void buildVector(Graphics2D g2d) {
        synchronized (lock) {
            g2d.setPaint(GRAPH_COLOR);
            g2d.setStroke(new BasicStroke(app.getGraphLineWidth()));
            Iterator<PixelCoordinate> iterator1 = points1.iterator();
            Iterator<PixelCoordinate> iterator2 = points2.iterator();
            while (iterator1.hasNext() && iterator2.hasNext()) {
                PixelCoordinate p1 = iterator1.next();
                PixelCoordinate p2 = iterator2.next();
                g2d.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            }
        }
    }

    private void buildZeroes(Graphics2D g2d) {
        synchronized (lock) {
            Stroke previousStroke = g2d.getStroke();
            Color previousColor = g2d.getColor();
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(1));
            buildXZeroes(g2d);
            buildYZeroes(g2d);
            g2d.setStroke(previousStroke);
            g2d.setColor(previousColor);
        }
    }

    private void buildXZeroes(Graphics2D g2d) {
        final int length = app.getGraphMaxHeight() / 20;
        for (PixelCoordinate point : xZeroes) {
            if (point.isValid()) {
                g2d.drawLine(point.getX(), app.getY0(), point.getX(), app.getY0() + length);
                double x = point.getPoint().getX();
                String format = GraphPaintUtil.getDoubleFormat(x);
                g2d.drawString(String.format(format, x), point.getX() + 5, app.getY0() + length + length / 2);
            }
        }
    }

    private void buildYZeroes(Graphics2D g2d) {
        final int length = app.getGraphMaxWidth() / 20;
        for (PixelCoordinate point : yZeroes) {
            if (point.isValid()) {
                g2d.drawLine(app.getX0(), point.getY(), app.getX0() + length, point.getY());
                double y = point.getPoint().getY();
                String format = GraphPaintUtil.getDoubleFormat(y);
                g2d.drawString(String.format(format, y), app.getX0() + length + length / 2, point.getY());
            }
        }
    }

    private void buildMarksOnX(Graphics2D g2d) {
        final double step = app.getMarkStep();
        String format = GraphPaintUtil.getDoubleFormat(step);
        double counter = step;
        g2d.drawString("0", app.getX0() + 2, app.getY0() + 2 * markLength + 2);
        for (int x = app.getX0() + app.getMarkPixelStep(); x <= app.getGraphMaxWidth(); x += app.getMarkPixelStep()) {
            buildGraphAxisLine(g2d, x, app.getGraphMaxHeight(), x, app.getBorderPixelLimit());
            g2d.drawLine(x, app.getY0() + markLength, x, app.getY0() - markLength);
            g2d.drawString(String.format(format, counter), x + 2, app.getY0() + 2 * markLength + 2);
            counter += step;
        }
        counter = -step;
        for (int x = app.getX0() - app.getMarkPixelStep(); x >= app.getBorderPixelLimit(); x -= app.getMarkPixelStep()) {
            buildGraphAxisLine(g2d, x, app.getGraphMaxHeight(), x, app.getBorderPixelLimit());
            g2d.drawLine(x, app.getY0() + markLength, x, app.getY0() - markLength);
            g2d.drawString(String.format(format, counter), x + 2, app.getY0() + 2 * markLength + 2);
            counter -= step;
        }
    }

    private void buildMarksOnY(Graphics2D g2d) {
        final double step = app.getMarkStep();
        String format = GraphPaintUtil.getDoubleFormat(step);
        double counter = step;
        for (int y = app.getY0() - app.getMarkPixelStep(); y >= app.getBorderPixelLimit(); y -= app.getMarkPixelStep()) {
            buildGraphAxisLine(g2d, app.getBorderPixelLimit(), y, app.getGraphMaxWidth(), y);
            g2d.drawLine(app.getX0() + markLength, y, app.getX0() - markLength, y);
            g2d.drawString(String.format(format, counter), app.getX0() + markLength + 2, y - 2);
            counter += step;
        }

        counter = -step;
        for (int y = app.getY0() + app.getMarkPixelStep(); y <= app.getGraphMaxHeight(); y += app.getMarkPixelStep()) {
            buildGraphAxisLine(g2d, app.getBorderPixelLimit(), y, app.getGraphMaxWidth(), y);
            g2d.drawLine(app.getX0() + markLength, y, app.getX0() - markLength, y);
            g2d.drawString(String.format(format, counter), app.getX0() + markLength + 2, y - 2);
            counter -= step;
        }
    }

    private void buildGraphAxisLine(Graphics2D g2d, int x0, int y0, int x1, int y1) {
        g2d.setColor(Color.green);
        Stroke baseStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(AXIS_WIDTH));
        g2d.drawLine(x0, y0, x1, y1);
        g2d.setStroke(baseStroke);
        g2d.setColor(Color.black);
    }

    private void buildGraphBorder(Graphics2D g2d) {
        int borderPixelLimit = app.getBorderPixelLimit();
        int graphMaxWidth    = app.getGraphMaxWidth();
        int graphMaxHeight   = app.getGraphMaxHeight();
        g2d.drawLine(borderPixelLimit, borderPixelLimit, graphMaxWidth, borderPixelLimit);
        g2d.drawLine(graphMaxWidth, borderPixelLimit, graphMaxWidth, graphMaxHeight);
        g2d.drawLine(graphMaxWidth, graphMaxHeight, borderPixelLimit, graphMaxHeight);
        g2d.drawLine(borderPixelLimit, graphMaxHeight, borderPixelLimit, borderPixelLimit);
    }

    private void buildAxisArrows(Graphics2D g2d) {
        int borderPixelLimit = app.getBorderPixelLimit();
        int graphMaxWidth    = app.getGraphMaxWidth();
        int step = app.getMarkPixelStep() / 5;
        int [] xH = {app.getX0() - step, app.getX0(), app.getX0() + step};
        int [] yH = {borderPixelLimit + step, borderPixelLimit, borderPixelLimit + step};
        int [] xW = {graphMaxWidth - step, graphMaxWidth, graphMaxWidth - step};
        int [] yW = {app.getY0() + step, app.getY0(), app.getY0() - step};
        g2d.fillPolygon(new Polygon(xH, yH, xH.length));
        g2d.fillPolygon(new Polygon(xW, yW, xW.length));
    }
}