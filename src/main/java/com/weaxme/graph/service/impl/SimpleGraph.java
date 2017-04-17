package com.weaxme.graph.service.impl;

import com.google.common.collect.Lists;
import com.weaxme.graph.service.Coordinate;
import com.weaxme.graph.service.IGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Vitaliy Gonchar
 */
public class SimpleGraph implements IGraph {
    private String function;

    public static final String VAR         = "x";
    public static final String COMPLEX_VAR = "w";

    private boolean complex;

    private static final Logger LOG = LoggerFactory.getLogger(SimpleGraph.class);

    private static final List<String> ARITHMETIC_SIGNS = Lists.newArrayList();
    static {
        ARITHMETIC_SIGNS.add("+");
        ARITHMETIC_SIGNS.add("-");
        ARITHMETIC_SIGNS.add("/");
        ARITHMETIC_SIGNS.add("*");
    }

    public SimpleGraph(String function) {
        this.function = function;
        if (function.contains(VAR)) complex = false;
    }

    @Override
    public IGraph setNewGraphFunction(String function) {
        this.function = function;
        if (this.function.contains(VAR)) complex = false;
        return this;
    }

    @Override
    public String getGraphFunction() {
        return function;
    }

    @Override
    public IGraph refresh() {
        return null;
    }

    @Override
    public List<Coordinate> computeAndGetPoints(double min, double max, double step) {
        List<Coordinate> points = Lists.newArrayList();
        if (step == 0) return Lists.newArrayList();
        while (min <= max) {
            points.add(new Coordinate(min, computeX(min)));
            min += step;
        }
        return points;
    }

    // x + x * x * x + 18
    @SuppressWarnings("unchecked")
    protected double computeX(double x) {
        String expression = complex ? function.replace(COMPLEX_VAR, Double.toString(x)) : function.replace(VAR, Double.toString(x));
        double result = 0;

        return result;
    }
}
