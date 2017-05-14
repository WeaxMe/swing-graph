package com.weaxme.graph.application.graph;

import com.google.common.collect.Lists;
import org.apache.commons.math3.complex.Complex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

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
    protected void init() {
        coefficients = Lists.newArrayList();
        String coeffs[] = getGraphFunction().split(" ");
        for (String c : coeffs) {
            coefficients.add(Double.valueOf(c));
        }
    }

    @Override
    protected IGraphComputer getGraphComputer(double min, double max, double step) {
        return new GraphComputer(coefficients, min, max, step);
    }

    @Override
    public String toString() {
        return "DefaultGodographAxisGraph{" +
                "function='" + getGraphFunction() + '\'' +
                ", coefficients=" + coefficients +
                ", points=" + getPoints() +
                '}';
    }

    @Override
    public Coordinate compute(double x) {
        return new GraphComputer(coefficients).compute(x);
    }


    private static class GraphComputer implements IGraphComputer {

        private final List<Double> coefficients;

        private final List<Coordinate> yZeroPoints = Lists.newArrayList();
        private final List<Coordinate> xZeroPoints = Lists.newArrayList();

        private final double min;
        private final double max;
        private final double step;

        public GraphComputer(List<Double> coefficients, double min, double max, double step) {
            this.coefficients = coefficients;
            this.min = min;
            this.max = max;
            this.step = step;
        }

        public GraphComputer(List<Double> coefficients) {
            this.coefficients = coefficients;
            this.min = 0;
            this.max = 0;
            this.step = 0;
        }

        @Override
        public List<Coordinate> call() throws Exception {
            double counter = min;
            List<Coordinate> result = Lists.newArrayList();
            boolean previousIsNaN = false;

            while (counter <= max) {
                Coordinate point = compute(counter);
                if (result.size() > 0) {
                    Coordinate previous = result.get(result.size() - 1);
                    if (previous.getY() > 0 && point.getY() < 0
                            || previous.getY() < 0 && point.getY() > 0
                            || point.getY() == 0) {
                        Coordinate zeroX = getZeroFromSegment(previous.getX(), point.getX(), false);
                        xZeroPoints.add(zeroX);
                    }

                    if (previous.getX() > 0 && point.getX() < 0
                            || previous.getX() < 0 && point.getX() > 0
                            || point.getX() == 0) {
                        Coordinate zeroY = getZeroFromSegment(previous.getY(), point.getY(), true);
                        yZeroPoints.add(zeroY);
                    }
                } else if (previousIsNaN && point.getY() < 0.5) {
                    xZeroPoints.add(point);
                }
                if (!point.isNaN()) {
                    result.add(point);
                    previousIsNaN = false;
                } else {
                    previousIsNaN = true;
                }
                counter += step;
            }

            return result;
        }

        @Override
        public List<Coordinate> getXZeroes() {
            return xZeroPoints;
        }

        @Override
        public List<Coordinate> getYZeroes() {
            return yZeroPoints;
        }

        /**
         * i * i = -1
         * i * i * i = -i
         * i * i * i * i = 1
         * i * i * i * i * i = -i
         * i * i * i * i * i * i = -1
         *
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

        private Coordinate getZeroFromSegment(double first, double second, boolean yAxis) {
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
    }
}
