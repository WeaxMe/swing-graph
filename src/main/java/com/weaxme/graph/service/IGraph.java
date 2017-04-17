package com.weaxme.graph.service;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vitaliy Gonchar
 */
public interface IGraph extends Serializable {
    IGraph setNewGraphFunction(String function);
    String getGraphFunction();
    IGraph refresh();
    List<Coordinate> computeAndGetPoints(double min, double max, double step);
}
