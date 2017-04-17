package com.weaxme.graph.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.weaxme.graph.service.Coordinate;
import com.weaxme.graph.service.IGraph;
import org.apache.commons.math3.complex.Complex;

import java.util.List;

/**
 * @author Vitaliy Gonchar
 * Simple realisation for represents godograph functions
 * 1 3 4 -5 -34 -1
 * equals
 * 1 + 3p + 4p^2 - 5p^3 - 34p^4 - p^5
 */
public class DefaultGodographAxisGraph implements IGraph {

    private String function;

    private final List<Double> coefficients    = Lists.newArrayList();
    private final List<Coordinate> points = Lists.newArrayList();

    private final Object lock = new Object();

    public DefaultGodographAxisGraph(String function) {
        setNewGraphFunction(function);
    }

    @Override
    public IGraph setNewGraphFunction(String function) {
        if (Strings.isNullOrEmpty(function))
            throw new IllegalArgumentException("function cannot be null or empty: " + function);
        if (!function.contains(" "))
            throw new IllegalArgumentException("function must be like '1 3 4 -5 -34 -1'. "
                                                + "It is equals '1 + 3p^1 + 4p^2 - 5p^3 - 34p^4 - p^5'");
        this.function = function;
        coefficients.clear();
        points.clear();
        initCoefficients();
        return this;
    }

    @Override
    public String getGraphFunction() {
        return function;
    }

    @Override
    public IGraph refresh() {
        coefficients.clear();
        points.clear();
        initCoefficients();
        return this;
    }

    @Override
    public List<Coordinate> computeAndGetPoints(double min, double max, double step) {
        if (points.size() == 0) {
            while (min <= max) {
                Complex complex = computeW(min);
                points.add(new Coordinate(complex.getReal(), complex.getImaginary()));
                min += step;
            }
        }
        return points;
    }


    private void initCoefficients() {
        String coeffs[] = function.split(" ");
        for (String c : coeffs) {
            coefficients.add(Double.valueOf(c));
        }
    }

    /**
     * i * i = -1
     * i * i * i = -i
     * i * i * i * i = 1
     * i * i * i * i * i = -i
     * i * i * i * i * i * i = -1
     * @param w angular frequency
     * @return complex result of function
     */
    private Complex computeW(double w) {
        int count = 0;
        Complex result = new Complex(0, 0);
        Complex j = new Complex(0, 1).multiply(w);
        for (Double coeff : coefficients) {
            Complex pow = j.pow(count);
            Complex multiply = pow.multiply(coeff);
            result = result.add(multiply);
            count++;
        }
        return result;
    }

    @Override
    public String toString() {
        return "DefaultGodographAxisGraph{" +
                "function='" + function + '\'' +
                ", coefficients=" + coefficients +
                ", points=" + points +
                '}';
    }
}
