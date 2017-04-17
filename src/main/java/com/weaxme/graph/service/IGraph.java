package com.weaxme.graph.service;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vitaliy Gonchar
 */
public interface IGraph extends Serializable {
    IGraph setNewGraphFunction(String function, double min, double max, double step);
    String getGraphFunction();
    IGraph refresh();
    List<Coordinate> getPoints();
    double getMin();
    double getMax();
    double getStep();
}
