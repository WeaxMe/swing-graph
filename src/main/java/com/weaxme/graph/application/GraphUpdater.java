package com.weaxme.graph.application;

import com.google.common.collect.Lists;
import com.weaxme.graph.application.graph.Coordinate;
import com.weaxme.graph.application.graph.IGraph;
import com.weaxme.graph.application.graph.PixelCoordinate;
import org.apache.log4j.net.SocketNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;


/**
 * @author Vitaliy Gonchar
 */
public class GraphUpdater implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(GraphUpdater.class);

    private final IGraphApplication app;
    private final long delayToBuildGraphMs;

    public GraphUpdater(IGraphApplication app) {
        this(app, app.getGraphDelay());
    }

    public GraphUpdater(IGraphApplication app, int delayToBuildGraph) {
        this.app = app;
        this.delayToBuildGraphMs = delayToBuildGraph * 1000;
    }

    @Override
    public void run() {
        app.setNowRepaint(true);
        IGraph graph = app.getGraph();
        List<PixelCoordinate> points = graph != null ? graph.getPixelPoints(app) : Lists.<PixelCoordinate>newArrayList();
        if (points.size() == 0) {
            app.setNowRepaint(false);
            return;
        }
        long delay = delayToBuildGraphMs / points.size();
        boolean microseconds = false;
        if (delay == 0 && delayToBuildGraphMs != 0) {
            microseconds = true;
            delay = (delayToBuildGraphMs * 1000) / points.size();
        }
        boolean firstPoint = true;
        PixelCoordinate point1 = null;
        Iterator<PixelCoordinate> iterator = points.iterator();
        long startTime = System.currentTimeMillis();
        long counter = 0;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        long timerDelay = delay != 0 && microseconds ? delay : 0;
        FutureTask<Void> task = null;
        LOG.debug("====================================================================");
        while (iterator.hasNext()) {
            PixelCoordinate point2 = iterator.next();
            if (firstPoint) {
                point1 = point2;
                firstPoint = false;
            }
            app.updateGraph(point1, point2);
            if (delay != 0) {
                if (task == null || task.isDone()) {
                    if (timerDelay != 0 && (task == null || task.isDone())) {
                        task = new FutureTask<>(new SleepCounter(timerDelay));
                        executor.execute(task);
                    } else {
                        delay(delay, microseconds);
                    }
                }
            }

            counter++;

            point1 = point2;
        }
        LOG.debug("====================================================================");
        LOG.debug("delay counter: {}", counter);
        long time = System.currentTimeMillis() - startTime;
        LOG.debug("Time for build graph: {} ms,  {} s", time, TimeUnit.MILLISECONDS.toSeconds(time));
        app.buildGraphAxisZeroLines();
        app.setNowRepaint(false);
        app.notifyGraphUpdateListeners();
    }

    private class SleepCounter implements Callable<Void> {

        private final long delay;

        public SleepCounter(long delay) {
            this.delay = delay;
        }

        @Override
        public Void call() throws Exception {
            try {
                LOG.info("call");
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                LOG.error("Can't create delay!", e);
            }
            return null;
        }
    }

    private void delay(long delay, boolean microseconds) {
        try {
            Thread.sleep(microseconds ? 1 : delay);
        } catch (InterruptedException e) {
            LOG.error("Can't create delay!", e);
        }
    }
}
