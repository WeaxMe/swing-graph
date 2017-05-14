package com.weaxme.graph.application.graph;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.weaxme.graph.application.IGraphApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author Vitaliy Gonchar
 */
public abstract class AbstractGraph implements IGraph {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractGraph.class);

    private String function;

    private final Object lock = new Object();

    private final List<Coordinate> points      = Lists.newArrayList();
    private final List<Coordinate> xZeroPoints = Lists.newArrayList();
    private final List<Coordinate> yZeroPoints = Lists.newArrayList();

    private final Object lockX = new Object();
    private final Object lockY = new Object();

    private double min;
    private double max;
    private double step;
    private double markStep;

    protected AbstractGraph(String function, double min, double max, double step) {
        setNewGraphFunction(function, min, max, step);
    }

    @Override
    public final IGraph setNewGraphFunction(String function, double min, double max, double step) {
        synchronized (lock) {
            if (Strings.isNullOrEmpty(function))
                throw new IllegalArgumentException("function cannot be null or empty: " + function);
            if (!function.contains(" "))
                throw new IllegalArgumentException("function must be like '1 3 4 -5 -34 -1'. "
                        + "It is equals '1 + 3p^1 + 4p^2 - 5p^3 - 34p^4 - p^5'");
            this.function = function;
            this.min = min;
            this.max = max;
            this.step = step;
            refresh();
            computePoints();
            this.markStep = computeMarkStep(this.min, this.max);
            return this;
        }
    }

    private void computePoints() {
        final int availableProcessors = Runtime.getRuntime().availableProcessors();
        final double zone =  Math.abs(getMax() - min) / availableProcessors;
        ExecutorService executorService = Executors.newFixedThreadPool(availableProcessors + 1);
        List<FutureTask<List<Coordinate>>> tasks = Lists.newArrayList();
        List<IGraphComputer> graphComputers = Lists.newArrayList();
        double counter = min;
        for (int i = 0; i < availableProcessors; i++) {
            double newMax = counter + zone;
            IGraphComputer computer = getGraphComputer(counter, newMax, step);
            tasks.add(new FutureTask<>(computer));
            graphComputers.add(computer);
            counter = newMax;
        }

        for (FutureTask<List<Coordinate>> task : tasks) {
            executorService.execute(task);
        }

        for (int i = 0; i < tasks.size(); i++) {
            try {
                addPoints(tasks.get(i).get());
                addXZeroPoints(graphComputers.get(i).getXZeroes());
                addYZeroPoints(graphComputers.get(i).getYZeroes());
            } catch (InterruptedException | ExecutionException e) {
                LOG.error("Can't compute graph: {} in range: min = {} max: {}", getGraphFunction(), getMin(), getMax());
                if (LOG.isDebugEnabled()) e.printStackTrace();
            }
        }
    }

    protected abstract IGraphComputer getGraphComputer(double min, double max, double step);
    protected abstract void init();

    protected final void setMin(double min) {
        this.min = min;
    }

    protected final void setMax(double max) {
        this.max = max;
    }

    protected final void addXZeroPoint(Coordinate point) {
        if (!xZeroPoints.contains(point))
            xZeroPoints.add(point);
    }

    protected final void addYZeroPoint(Coordinate point) {
        if (!yZeroPoints.contains(point))
            yZeroPoints.add(point);
    }

    protected final void addYZeroPoints(List<Coordinate> points) {
        yZeroPoints.addAll(points);
    }

    protected final void addXZeroPoints(List<Coordinate> points) {
        xZeroPoints.addAll(points);
    }

    protected final void addPoint(Coordinate point) {
        if (!points.contains(point))
            points.add(point);
    }

    protected final void addPoints(List<Coordinate> points) {
        this.points.addAll(points);
    }

    protected final int pointsSize() {
        return points.size();
    }

    protected final Coordinate getPoint(int index) {
        return index >= 0 ? points.get(index) : null;
    }

    protected final Coordinate getLastPoint() {
        return points.get(pointsSize() - 1);
    }

    protected double computeMarkStep(double min, double max) {
        return Math.abs(max) / IGraphApplication.MAX_X_MARKS;
    }

    @Override
    public final String getGraphFunction() {
        return function;
    }

    @Override
    public final List<Coordinate> getPoints() {
        synchronized (lock) {
            return Collections.unmodifiableList(points);
        }
    }

    @Override
    public List<PixelCoordinate> getPixelPoints(IGraphApplication app) {
        List<PixelCoordinate> pixelPoints = Lists.newArrayList();
        for (Coordinate coordinate : points) {
            PixelCoordinate pixelCoordinate = new PixelCoordinate(coordinate, app);
            if (pixelCoordinate.isValid()) {
                pixelPoints.add(pixelCoordinate);
            }
        }
        return pixelPoints;
    }

    @Override
    public final IGraph refresh() {
        points.clear();
        xZeroPoints.clear();
        yZeroPoints.clear();
        init();
        return this;
    }

    @Override
    public double getMin() {
        return min;
    }

    @Override
    public double getMax() {
        return max;
    }

    @Override
    public double getStep() {
        return step;
    }

    @Override
    public double getMarkStep() {
        return markStep;
    }

    @Override
    public List<Coordinate> getXZeroPoints() {
        synchronized (lock) {
            return Collections.unmodifiableList(xZeroPoints);
        }
    }

    @Override
    public List<Coordinate> getYZeroPoints() {
        synchronized (lock) {
            return Collections.unmodifiableList(yZeroPoints);
        }
    }

    @Override
    public boolean equals(String function, double min, double max, double step) {
        return this.function.equals(function) && this.min == min && this.max == max && this.step == step;
    }
}
