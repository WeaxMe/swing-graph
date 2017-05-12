package com.weaxme.graph.application.graph;

import com.google.common.collect.Lists;
import com.weaxme.graph.service.Coordinate;
import org.apache.commons.math3.complex.Complex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Vitaliy Gonchar
 * Simple realisation for represents godograph functions
 * 1 3 4 -5 -34 -1
 * equals
 * 1 + 3p + 4p^2 - 5p^3 - 34p^4 - p^5
 */
public class DefaultGodographAxisGraph extends AbstractGraph {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultGodographAxisGraph.class);

    private List<Double> coefficients;

    public DefaultGodographAxisGraph(String function, double min, double max, double step) {
        super(function, min, max, step);
    }

    @Override
    protected void initPoints(double step) {
        double min = getMin();
        double max = getMax();
        boolean previousIsNaN = false;
        while (min <= max) {
            Coordinate point = compute(min);
            if (pointsSize() > 0) {
                Coordinate previous = getLastPoint();
                if (previous.getY() > 0 && point.getY() < 0
                        || previous.getY() < 0 && point.getY() > 0
                        || point.getY() == 0) {
                    Coordinate zeroY = getZeroFromSegment(previous.getX(), point.getX(), false);
                    addXZeroPoint(zeroY);
                }

                if (previous.getX() > 0 && point.getX() < 0
                        || previous.getX() < 0 && point.getX() > 0
                        || point.getX() == 0) {
                    Coordinate zeroX = getZeroFromSegment(previous.getY(), point.getY(), true);
                    addYZeroPoint(zeroX);
                }
            } else if (previousIsNaN && point.getY() < 0.5) {
                addXZeroPoint(point);
            }
            if (!point.isNaN()) {
                addPoint(point);
                previousIsNaN = false;
            } else {
                previousIsNaN = true;
            }
            min += step;
        }
    }

    public Coordinate getZeroFromSegment(double first, double second, boolean yAxis) {
        if (Double.isNaN(first) || Double.isNaN(second))
            throw new IllegalArgumentException("first or second is NaN");
        Coordinate zero;
        while (true) {
            double res = (first + second) / 2;
            if (res == first || res == second) {
                zero = yAxis ? new Coordinate(0.0, res) : new Coordinate(res, 0.0);
                break;
            }
            if (compute(res).getY() < 0) {
                first = res;
            } else second = res;
        }
        return zero;
    }

    private Complex searchZero(Complex first, Complex second, double baseStep) {
        Complex zero = new Complex(-1);

        return zero;
    }

    @Override
    protected void init() {
        coefficients = Lists.newArrayList();
        String coeffs[] = getGraphFunction().split(" ");
        for (String c : coeffs) {
            coefficients.add(Double.valueOf(c));
        }
    }

    @Override
    public String toString() {
        return "DefaultGodographAxisGraph{" +
                "function='" + getGraphFunction() + '\'' +
                ", coefficients=" + coefficients +
                ", points=" + getPoints() +
                '}';
    }



    /**
     * i * i = -1
     * i * i * i = -i
     * i * i * i * i = 1
     * i * i * i * i * i = -i
     * i * i * i * i * i * i = -1
     * @param x angular frequency
     * @return complex result of function
     */
    @Override
    public Coordinate compute(double x) {
        int count = 0;
        Complex result = new Complex(0, 0);
        Complex j = new Complex(0, 1).multiply(x);
        for (Double coeff : coefficients) {
            Complex pow = j.pow(count);
            Complex multiply = pow.multiply(coeff);
            result = result.add(multiply);
            count++;
        }
        return new Coordinate(result.getReal(), result.getImaginary());
    }
}
