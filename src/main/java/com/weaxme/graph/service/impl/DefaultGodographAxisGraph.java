package com.weaxme.graph.service.impl;

import com.google.common.collect.Lists;
import com.weaxme.graph.service.Coordinate;
import org.apache.commons.math3.complex.Complex;

import java.util.List;

/**
 * @author Vitaliy Gonchar
 * Simple realisation for represents godograph functions
 * 1 3 4 -5 -34 -1
 * equals
 * 1 + 3p + 4p^2 - 5p^3 - 34p^4 - p^5
 */
public class DefaultGodographAxisGraph extends AbstractGraph {

    private List<Double> coefficients;

    public DefaultGodographAxisGraph(String function, double min, double max, double step) {
        super(function, min, max, step);
    }

    @Override
    protected void compute(List<Coordinate> points, double min, double max, double step) {
        while (min <= max) {
            Complex complex = computeW(min);
            points.add(new Coordinate(complex.getReal(), complex.getImaginary()));
            min += step;
        }
    }

    @Override
    protected void init() {
        coefficients = Lists.newArrayList();
        String coeffs[] = getGraphFunction().split(" ");
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
                "function='" + getGraphFunction() + '\'' +
                ", coefficients=" + coefficients +
                ", points=" + getPoints() +
                '}';
    }
}
