package com.weaxme.graph.application;

import com.google.common.collect.Lists;
import com.weaxme.graph.application.graph.Coordinate;
import com.weaxme.graph.application.graph.IGraph;
import com.weaxme.graph.application.graph.PixelCoordinate;
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
        app.setNowRepaint(true);
        IGraph graph = app.getGraph();
        List<Coordinate> points = graph != null ? graph.getPoints() : Lists.<Coordinate>newArrayList();
        if (points.size() == 0) {
            app.setNowRepaint(false);
            return;
        }
        boolean firstPoint = true;
        PixelCoordinate point1 = null;
        Iterator<Coordinate> iterator = points.iterator();
        while (iterator.hasNext()) {
            Coordinate coordinate = iterator.next();
            PixelCoordinate point2 = new PixelCoordinate(coordinate, app);
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
        app.buildGraphAxisZeroLines();
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
