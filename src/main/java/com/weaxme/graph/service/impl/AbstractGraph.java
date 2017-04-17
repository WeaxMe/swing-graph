package com.weaxme.graph.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.weaxme.graph.service.Coordinate;
import com.weaxme.graph.service.IGraph;

import java.util.List;

/**
 * @author Vitaliy Gonchar
 */
public abstract class AbstractGraph implements IGraph {

    private String function;

    private final Object lock = new Object();

    private List<Coordinate> points = Lists.newArrayList();

    private double min;
    private double max;
    private double step;

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
            refresh();
            compute(points, min, max, step);
            this.min = min;
            this.max = max;
            this.step = step;
            return this;
        }
    }

    protected abstract void compute(List<Coordinate> points, double min, double max, double step);
    protected abstract void init();

    @Override
    public final String getGraphFunction() {
        return function;
    }

    @Override
    public final List<Coordinate> getPoints() {
        synchronized (lock) {
            return points;
        }
    }

    @Override
    public final IGraph refresh() {
        points.clear();
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
}
