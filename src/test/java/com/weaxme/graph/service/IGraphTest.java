package com.weaxme.graph.service;

import com.google.common.collect.Lists;
import com.weaxme.graph.service.impl.DefaultGodographAxisGraph;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * @author Vitaliy Gonchar
 */
public class IGraphTest {
    private static final Logger LOG = LoggerFactory.getLogger(IGraphTest.class);

    private long start;
    private long end;

    @Before
    public void beforeTest() {
        start = System.currentTimeMillis();
    }

    @After
    public void afterTest() {
        end = System.currentTimeMillis();
        long time = end - start;
        LOG.info("Test runs: {} ms, {} s", time, TimeUnit.MILLISECONDS.toSeconds(time));
    }


    @Test
    public void testJavaCompute() {
        LOG.info("Java compute!");
        List<Coordinate> trueCoordinates = xPlusX(-10, 10, 0.01);
        LOG.info("Points number: {}", trueCoordinates.size());
    }

    @Test
    public void testGodograph() {
        IGraph graph = new DefaultGodographAxisGraph("1 2", -1, 1, 0.01);
        LOG.info("graph: {}", graph);
        List<Coordinate> coordinates = graph.getPoints();
        for (Coordinate coordinate : coordinates) {
            LOG.info("coordinate: {}", coordinate);
        }
    }

    @Ignore
    @Test
    public void testSimpleSchedule() {
        String scheduleString1 = "x^2";
        String shceduleString2 = "x * x";
    }

    private void testPoints(List<Coordinate> points1, List<Coordinate> points2) {
        assertEquals("Size must equals", points1.size(), points2.size());
        Iterator<Coordinate> iterator = points2.iterator();
        for (Coordinate point1 : points1) {
            Coordinate point2 = iterator.next();
            assertEquals("Points must equals: \n point1: " + point1 + " \npoint2: " + point2, point1, point2);
        }
    }

    private List<Coordinate> xPlusX(double min, double max, double step) {
        List<Coordinate> points = Lists.newArrayList();
        while (min <= max) {
            points.add(new Coordinate(min, functionXplusX(min)));
            min += step;
        }
        return points;
    }

    private double functionXplusX(double x) {
        return x + x;
    }
}
