package com.weaxme.graph.service.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;


public class GraphUtilTest {

    private static final Logger LOG = LoggerFactory.getLogger(GraphUtilTest.class);

    @Test
    public void getGraphPointMultiplierCoeff() throws Exception {
        double multiplierCoeff = GraphUtil.getGraphMarkStep(-10, 10);
        assertTrue("10", multiplierCoeff == 10);
        multiplierCoeff = GraphUtil.getGraphMarkStep(-100, 100);
        assertTrue("100", multiplierCoeff == 100);
        multiplierCoeff = GraphUtil.getGraphMarkStep(-1000, 1000);
        assertTrue("1000",multiplierCoeff == 1000);
        multiplierCoeff = GraphUtil.getGraphMarkStep(3, 4);
        assertTrue("0.1", multiplierCoeff == 0.1);
        multiplierCoeff = GraphUtil.getGraphMarkStep(-10, -5);
        assertTrue("1", multiplierCoeff == 1);
    }

}
