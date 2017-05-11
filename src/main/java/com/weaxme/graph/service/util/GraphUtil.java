package com.weaxme.graph.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GraphUtil {

    private static final Logger LOG = LoggerFactory.getLogger(GraphUtil.class);

    private GraphUtil() {}

    public static double getGraphMarkStep(double min, double max) {
        double sumOfTwo = Math.abs(min - max) / 2;
        double multiplier = 1;
        if (sumOfTwo >= 10) {
            while (multiplier < sumOfTwo) {
                multiplier *= 10;
            }
        } else {
            while (multiplier >= sumOfTwo) {
                multiplier /= 10;
            }
        }
        return multiplier;
    }
}
