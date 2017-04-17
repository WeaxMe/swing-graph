package com.weaxme.graph.service;

/**
 * @author Vitaliy Gonchar
 */
public interface IGraphPanel {
    public void clearAndRepaint();
    public void addVectorAndRepaint(PixelCoordinate point1, PixelCoordinate point2);
    public IGraphApplication getApplication();
}
