package com.weaxme.graph.window.panel;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.weaxme.graph.service.GraphUpdater;
import com.weaxme.graph.service.IGraphApplication;
import com.weaxme.graph.service.IGraphPanel;
import com.weaxme.graph.service.PixelCoordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Iterator;
import java.util.List;

/**
 * @author Vitaliy Gonchar
 */
@Singleton
public class GraphPanel extends JPanel implements IGraphPanel {

    @Inject
    private IGraphApplication app;

    private int markLength;

    private final List<PixelCoordinate> points1 = Lists.newArrayList();
    private final List<PixelCoordinate> points2 = Lists.newArrayList();

    private final Object lock = new Object();


    public GraphPanel() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                clearAndRepaint();
                new Thread(new GraphUpdater(app, 0)).start();
            }
        });
    }

    @Override
    public void clearAndRepaint() {
        synchronized (lock) {
            points1.clear();
            points2.clear();
            repaint();
        }
    }

    @Override
    public void addVectorAndRepaint(PixelCoordinate point1, PixelCoordinate point2) {
        if (point1 == null)
            throw new IllegalArgumentException("point1 cannot be null!");
        if (point2 == null)
            throw new IllegalArgumentException("point2 cannot be null!");
        synchronized (lock) {
            points1.add(point1);
            points2.add(point2);
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
    }

    private void buildAxis(Graphics2D g2d) {
        g2d.setPaint(Color.BLACK);
        g2d.drawLine(app.getX0(), app.getBorderPixelLimit(), app.getX0(), app.getGraphMaxHeight());
        g2d.drawLine(app.getBorderPixelLimit(), app.getY0(), app.getGraphMaxWidth(), app.getY0());
        buildMarksOnX(g2d);
        buildMarksOnY(g2d);
    }

    private void buildVector(Graphics2D g2d) {
        synchronized (lock) {
            g2d.setPaint(Color.RED);
            Iterator<PixelCoordinate> iterator1 = points1.iterator();
            Iterator<PixelCoordinate> iterator2 = points2.iterator();
            while (iterator1.hasNext() && iterator2.hasNext()) {
                PixelCoordinate p1 = iterator1.next();
                PixelCoordinate p2 = iterator2.next();
                g2d.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            }
        }
    }

    private void buildMarksOnX(Graphics2D g2d) {
        for (int x = app.getX0(); x <= app.getGraphMaxWidth(); x += app.getPixelStep()) {
            g2d.drawLine(x, app.getY0() + markLength, x, app.getY0() - markLength);
        }
        for (int x = app.getX0(); x >= app.getBorderPixelLimit(); x -= app.getPixelStep()) {
            g2d.drawLine(x, app.getY0() + markLength, x, app.getY0() - markLength);
        }
    }

    private void buildMarksOnY(Graphics2D g2d) {
        for (int y = app.getY0(); y <= app.getGraphMaxHeight(); y += app.getPixelStep()) {
            g2d.drawLine(app.getX0() + markLength, y, app.getX0() - markLength, y);
        }
        for (int y = app.getY0(); y >= app.getBorderPixelLimit(); y -= app.getPixelStep()) {
            g2d.drawLine(app.getX0() + markLength, y, app.getX0() - markLength, y);
        }
    }
}
