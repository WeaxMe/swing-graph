package com.weaxme.graph.application.graph;

import com.weaxme.graph.service.Coordinate;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vitaliy Gonchar
 */
public interface IGraph extends Serializable {
    IGraph setNewGraphFunction(String function, double min, double max, double step);
    IGraph setNewGraphFunction(String function, double step);
    String getGraphFunction();
    IGraph refresh();
    List<Coordinate> getPoints();
    double getMin();
    double getMax();
    double getStep();
}
