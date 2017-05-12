package com.weaxme.graph.application.graph;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.weaxme.graph.application.IGraphApplication;
import com.weaxme.graph.service.Coordinate;

import java.util.Collections;
import java.util.List;

/**
 * @author Vitaliy Gonchar
 */
public abstract class AbstractGraph implements IGraph {

    private String function;

    private final Object lock = new Object();

    private final List<Coordinate> points      = Lists.newArrayList();
    private final List<Coordinate> xZeroPoints = Lists.newArrayList();
    private final List<Coordinate> yZeroPoints = Lists.newArrayList();

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
            initPoints(step);
            this.markStep = computeMarkStep(this.min, this.max);
            return this;
        }
    }

    protected abstract void initPoints(double step);
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

    protected final void addPoint(Coordinate point) {
        if (!points.contains(point))
            points.add(point);
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
}
