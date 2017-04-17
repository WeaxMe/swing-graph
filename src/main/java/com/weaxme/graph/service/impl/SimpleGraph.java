package com.weaxme.graph.service.impl;

import com.google.common.collect.Lists;
import com.weaxme.graph.service.Coordinate;

import java.util.List;

/**
 * @author Vitaliy Gonchar
 */
public class SimpleGraph extends AbstractGraph {

    public static final String VAR  = "x";

    private static final List<String> ARITHMETIC_SIGNS = Lists.newArrayList();
    static {
        ARITHMETIC_SIGNS.add("+");
        ARITHMETIC_SIGNS.add("-");
        ARITHMETIC_SIGNS.add("/");
        ARITHMETIC_SIGNS.add("*");
    }

    protected SimpleGraph(String function, double min, double max, double step) {
        super(function, min, max, step);
    }


    @Override
    protected void compute(List<Coordinate> points, double min, double max, double step) {

    }

    @Override
    protected void init() {

    }

}
