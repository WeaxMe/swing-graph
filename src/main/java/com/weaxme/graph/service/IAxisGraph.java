package com.weaxme.graph.service;

import com.google.inject.ImplementedBy;
import com.weaxme.graph.service.impl.DefaultAxisGraph;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vitaliy Gonchar
 */
@ImplementedBy(DefaultAxisGraph.class)
public interface IAxisGraph extends Serializable {
    List<Coordinate> getPoints(); // get x and y
}
