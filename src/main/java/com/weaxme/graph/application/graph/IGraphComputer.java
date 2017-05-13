package com.weaxme.graph.application.graph;

import java.util.List;
import java.util.concurrent.Callable;

interface IGraphComputer extends Callable<List<Coordinate>> {
    public Coordinate compute(double x);
    public List<Coordinate> getXZeroes();
    public List<Coordinate> getYZeroes();
}
