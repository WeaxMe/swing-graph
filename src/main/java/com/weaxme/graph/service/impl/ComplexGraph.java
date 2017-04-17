package com.weaxme.graph.service.impl;

import com.weaxme.graph.service.Coordinate;
import com.weaxme.graph.service.IGraph;

import java.util.List;

/**
 * @author Vitaliy Gonchar
 * Represents complex schedules like p = j * w; D(p) = 2p^3 + 9p^2 + 13p + 6;
 */
public class ComplexGraph implements IGraph {
    @Override
    public IGraph setNewGraphFunction(String function) {
        return null;
    }

    @Override
    public String getGraphFunction() {
        return null;
    }

    @Override
    public IGraph refresh() {
        return null;
    }

    @Override
    public List<Coordinate> computeAndGetPoints(double min, double max, double step) {
        return null;
    }
}
