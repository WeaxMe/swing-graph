package com.weaxme.graph.application;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import com.weaxme.graph.application.graph.Coordinate;
import com.weaxme.graph.application.graph.IGraph;
import com.weaxme.graph.application.graph.PixelCoordinate;
import com.weaxme.graph.component.panel.IGraphPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Vitaliy Gonchar
 */
@Singleton
public class GraphApplication implements IGraphApplication {

    private static final Logger LOG = LoggerFactory.getLogger(GraphApplication.class);

    private IGraph graph;
    private IGraphPanel graphPanel;

    private long delayToBuildGraph = 1000;
    private int markPixelStep     = 10;
    private double markStep       = 0;
    private int borderPixelLimit  = 20;
    private int markLength        = 10;
    private int x0;
    private int y0;

    private int maxScheduleHeight;
    private int maxScheduleWidth;

    private int graphLineWidth     = 2;
    private double pointMultiplier = 1;

    private boolean nowRepaint = false;
    private boolean build      = false;

    private IGraphUpdater graphUpdater;

    private final List<IGraphUpdateListener> graphUpdateListeners = Lists.newArrayList();

    @Override
    public IGraphApplication updateGraph(PixelCoordinate point1, PixelCoordinate point2) {
        graphPanel.addVectorAndRepaint(point1, point2);
        return this;
    }

    @Override
    public IGraphApplication buildGraphAxisZeroLines() {
        if (graph == null)
            return this;
        for (Coordinate point : graph.getXZeroPoints()) {
            graphPanel.addZeroPointsAndRepaint(new PixelCoordinate(point, this), false);
        }
        for (Coordinate point : graph.getYZeroPoints()) {
            graphPanel.addZeroPointsAndRepaint(new PixelCoordinate(point, this), true);
        }
        return this;
    }

    @Override
    public IGraphApplication repaintGraph() {
        return repaintGraph(delayToBuildGraph);
    }

    @Override
    public IGraphApplication repaintGraph(long delay) {
        if (!nowRepaint) {
            graphPanel.clearAndRepaint();
            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
            executor.schedule(getGraphUpdater().setDelayForBuild(delay), 0, TimeUnit.MILLISECONDS);
        }
        return this;
    }

    @Override
    public IGraphApplication setGraph(IGraph graph) {
        if (graph == null)
            throw new IllegalArgumentException("graph cannot be null!");
        this.graph = graph;
        this.markStep = graph.getMarkStep();
        this.pointMultiplier = 1 / markStep;
        return this;
    }

    @Override
    public IGraph getGraph() {
        return graph;
    }

    @Override
    public IGraphApplication setGraphDelay(long delay) {
        if (delay < 0) throw new IllegalArgumentException("delayToBuildGraph cannot be < 0");
        this.delayToBuildGraph = delay;
        return this;
    }

    @Override
    public IGraphApplication setBorderPixelLimit(int limit) {
        if (limit <= 1) throw new IllegalArgumentException("border pixel limit must be > 1");
        this.borderPixelLimit = limit;
        return null;
    }

    @Override
    public IGraphApplication setMarkLength(int markLength) {
        this.markLength = markLength;
        return this;
    }

    @Override
    public IGraphApplication setGraphPanel(IGraphPanel graphPanel) {
        if (graphPanel == null)
            throw new IllegalArgumentException("graph panel cannot be null!");
        this.graphPanel = graphPanel;
        return this;
    }

    @Override
    public IGraphApplication setGraphMaxHeight(int height) {
        this.maxScheduleHeight = height;
        return this;
    }

    @Override
    public IGraphApplication setGraphMaxWidth(int width) {
        this.maxScheduleWidth = width;
        this.markPixelStep = width / MAX_MARKS;
        return this;
    }

    @Override
    public IGraphApplication setX0(int x0) {
        this.x0 = x0;
        return this;
    }

    @Override
    public IGraphApplication setY0(int y0) {
        this.y0 = y0;
        return this;
    }

    @Override
    public IGraphApplication addGraphUpdateListener(IGraphUpdateListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener can't be null");
        graphUpdateListeners.add(listener);
        return this;
    }

    @Override
    public long getGraphDelay() {
        return delayToBuildGraph;
    }

    @Override
    public IGraphPanel getGraphPanel() {
        return graphPanel;
    }

    @Override
    public IGraphApplication setNowRepaint(boolean repaint) {
        this.nowRepaint = repaint;
        return this;
    }

    @Override
    public IGraphApplication setNowGraphBuild(boolean build) {
        this.build = build;
        return this;
    }

    @Override
    public int getMarkPixelStep() {
        return markPixelStep;
    }

    @Override
    public int getXPixelStep() {
        return 0;
    }

    @Override
    public int getYPixelStep() {
        return 0;
    }

    @Override
    public int getBorderPixelLimit() {
        return borderPixelLimit;
    }

    @Override
    public int getMarkLength() {
        return markLength;
    }

    @Override
    public int getX0() {
        return x0;
    }

    @Override
    public int getY0() {
        return y0;
    }

    @Override
    public int getGraphMaxHeight() {
        return maxScheduleHeight;
    }

    @Override
    public int getGraphMaxWidth() {
        return maxScheduleWidth;
    }

    @Override
    public double getMarkStep() {
        return markStep;
    }

    @Override
    public boolean isNowGraphBuild() {
        return build;
    }

    @Override
    public IGraphApplication setGraphLineWidth(int width) {
        if (width <= 0)
            throw new IllegalStateException("Line width cannot be  <= 0");
        this.graphLineWidth = width;
        return this;
    }

    @Override
    public int getGraphLineWidth() {
        return graphLineWidth;
    }

    @Override
    public List<Integer> getPossibleWidth() {
        List<Integer> widths = Lists.newArrayList();
        for (int i = 1; i <= 10; i++) {
            widths.add(i);
        }
        return widths;
    }

    @Override
    public double getPointMultiplier() {
        return pointMultiplier;
    }

    @Override
    public IGraphUpdater getGraphUpdater() {
        if (graphUpdater != null && graphUpdater.isUpdated() || graphUpdater == null)
            graphUpdater = new GraphUpdater(this);
        return graphUpdater;
    }

    @Override
    public IGraphApplication setPointMultiplier(double multiplier) {
        if (multiplier <= 0)
            throw new IllegalArgumentException("Point multiplier cannot be <= 0");
        this.pointMultiplier = multiplier;
        return this;
    }

    @Override
    public void notifyGraphUpdateListeners() {
        for (IGraphUpdateListener listener : graphUpdateListeners) {
            listener.graphUpdate(this);
        }
    }
}
