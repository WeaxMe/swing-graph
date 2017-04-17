package com.weaxme.graph.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author Vitaliy Gonchar
 */
public class GraphUpdater implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(GraphUpdater.class);

    private final IGraphApplication app;
    private long delay;

    public GraphUpdater(IGraphApplication app) {
        this(app, app.getGraphDelay());
    }

    public GraphUpdater(IGraphApplication app, long delay) {
        this.app = app;
        this.delay = delay;
    }

    @Override
    public void run() {
        List<Coordinate> points = app.getGraphPoints();
        if (points.size() == 0) return;
        app.setNowRepaint(true);
        boolean firstPoint = true;
        LOG.debug("Start repaint schedule");
        PixelCoordinate point1 = null;
        Iterator<Coordinate> iterator = points.iterator();
        while (iterator.hasNext()) {
            PixelCoordinate point2 = new PixelCoordinate(iterator.next(), app);
            if (point2.isValid()) {
                if (firstPoint) {
                    point1 = point2;
                    firstPoint = false;
                }
                app.updateGraph(point1, point2);
                delay();
                point1 = point2;
            }
        }
        app.setNowRepaint(false);

    }

    private void delay() {
        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            LOG.error("Cannot create delay!", e);
        }
    }
}
