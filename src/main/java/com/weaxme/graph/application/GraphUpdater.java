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
class GraphUpdater implements IGraphUpdater {

    private static final Logger LOG = LoggerFactory.getLogger(GraphUpdater.class);

    private final IGraphApplication app;
    private long delayToBuildGraphMs;

    private boolean stop;
    private boolean pause;
    private boolean updated;

    private final Object lock = new Object();

    public GraphUpdater(IGraphApplication app) {
        this(app, app.getGraphDelay());
    }

    public GraphUpdater(IGraphApplication app, long delayToBuildGraph) {
        this.app = app;
        this.delayToBuildGraphMs = delayToBuildGraph;
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
        ExecutorService executor = Executors.newFixedThreadPool(10);
        long timerDelay = delay != 0 && microseconds ? delay : 0;
        FutureTask<Void> task = null;
        while (!stop && iterator.hasNext()) {
            if (!pause) {
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
                point1 = point2;
            } else delay(300, false);
        }
        if (!stop) {
            app.buildGraphAxisZeroLines();
        }
        app.notifyGraphUpdateListeners();
        app.setNowRepaint(false);
        updated = true;
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

    @Override
    public IGraphUpdater setDelayForBuild(long delay) {
        if (delay < 0)
            throw new IllegalArgumentException("delay can't be < 0");
        this.delayToBuildGraphMs = delay;
        return this;
    }

    @Override
    public IGraphUpdater setStop(boolean stop) {
        synchronized (lock) {
            this.stop = stop;
            return this;
        }
    }

    @Override
    public IGraphUpdater setPause(boolean pause) {
        synchronized (lock) {
            this.pause = pause;
            return this;
        }
    }

    @Override
    public boolean isUpdated() {
        return updated;
    }

    private void delay(long delay, boolean microseconds) {
        try {
            Thread.sleep(microseconds ? 1 : delay);
        } catch (InterruptedException e) {
            LOG.error("Can't create delay!", e);
        }
    }
}
