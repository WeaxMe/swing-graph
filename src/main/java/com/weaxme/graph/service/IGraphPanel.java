package com.weaxme.graph.service;

import com.weaxme.graph.application.IGraphApplication;

import java.util.List;

/**
 * @author Vitaliy Gonchar
 */
public interface IGraphPanel {
    public void clearAndRepaint();
    public void addVectorAndRepaint(PixelCoordinate point1, PixelCoordinate point2);
    public void addZeroPointsAndRepaint(PixelCoordinate zero, boolean yAxis);
    public IGraphApplication getApplication();
}
