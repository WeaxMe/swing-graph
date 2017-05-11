package com.weaxme.graph.service;

import com.weaxme.graph.application.IGraphApplication;

/**
 * @author Vitaliy Gonchar
 */
public interface IGraphPanel {
    public void clearAndRepaint();
    public void addVectorAndRepaint(PixelCoordinate point1, PixelCoordinate point2);
    public IGraphApplication getApplication();
}
