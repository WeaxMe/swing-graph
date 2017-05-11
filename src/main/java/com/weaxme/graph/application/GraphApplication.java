package com.weaxme.graph.application;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import com.weaxme.graph.application.graph.IGraph;
import com.weaxme.graph.service.*;
import com.weaxme.graph.service.util.GraphUtil;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Vitaliy Gonchar
 */
@Singleton
public class GraphApplication implements IGraphApplication {

    private static final int MAX_MARKS   = 22;
    private static final int MAX_X_MARKS = 10;
    private static final int MAX_Y_MARKS = 10;

    private IGraph graph;
    private IGraphPanel graphPanel;

    private long delay           = 1;
    private int markPixelStep    = 10;
    private double markStep      = 0;
    private int borderPixelLimit = 20;
    private int markLength       = 10;
    private int x0;
    private int y0;

    private int maxScheduleHeight;
    private int maxScheduleWidth;


    private int graphLineWidth     = 2;
    private double pointMultiplier = 1;

    private boolean nowRepaint = false;
    private boolean build      = false;

    @Override
    public IGraphApplication updateGraph(PixelCoordinate point1, PixelCoordinate point2) {
        graphPanel.addVectorAndRepaint(point1, point2);
        return this;
    }

    @Override
    public IGraphApplication repaintGraph() {
        if (!nowRepaint) {
            graphPanel.clearAndRepaint();
            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
            executor.schedule(new GraphUpdater(this), 1, TimeUnit.SECONDS);
        }
        return this;
    }

    @Override
    public IGraphApplication repaintGraphWithoutDelay() {
        if (!nowRepaint) {
            graphPanel.clearAndRepaint();
            new Thread(new GraphUpdater(this, 0)).start();
        }
        return this;
    }

    @Override
    public IGraphApplication setGraph(IGraph graph) {
        if (graph == null)
            throw new IllegalArgumentException("graph cannot be null!");
        this.graph = graph;
        double divider = GraphUtil.getGraphMarkStep(graph.getMin(), graph.getMax());
        this.markStep = divider / MAX_X_MARKS;
        this.pointMultiplier = MAX_X_MARKS / divider;
        return this;
    }

    @Override
    public IGraph getGraph() {
        return graph;
    }

    @Override
    public IGraphApplication setGraphDelay(long delay) {
        if (delay < 0) throw new IllegalArgumentException("delay cannot be < 0");
        this.delay = delay;
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
    public long getGraphDelay() {
        return delay;
    }

    @Override
    public List<Coordinate> getGraphPoints() {
        if (graph != null) {
            return graph.getPoints();
        }
        return Lists.newArrayList();
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
    public IGraphApplication setPointMultiplier(double multiplier) {
        if (multiplier <= 0)
            throw new IllegalArgumentException("Point multiplier cannot be <= 0");
        this.pointMultiplier = multiplier;
        return this;
    }
}
