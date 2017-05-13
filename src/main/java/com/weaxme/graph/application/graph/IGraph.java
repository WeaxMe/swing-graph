package com.weaxme.graph.application.graph;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vitaliy Gonchar
 */
public interface IGraph extends Serializable {
    public IGraph setNewGraphFunction(String function, double min, double max, double step);
    public Coordinate compute(double x);
    public String getGraphFunction();
    public IGraph refresh();
    public List<Coordinate> getPoints();
    public List<Coordinate> getXZeroPoints();
    public List<Coordinate> getYZeroPoints();
    public double getMin();
    public double getMax();
    public double getStep();
    public double getMarkStep();
}
