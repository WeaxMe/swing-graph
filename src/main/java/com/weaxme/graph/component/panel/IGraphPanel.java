package com.weaxme.graph.component.panel;

import com.weaxme.graph.application.IGraphApplication;
import com.weaxme.graph.application.graph.PixelCoordinate;

/**
 * @author Vitaliy Gonchar
 */
public interface IGraphPanel {
    public void clearAndRepaint();
    public void addVectorAndRepaint(PixelCoordinate point1, PixelCoordinate point2);
    public void addZeroPointsAndRepaint(PixelCoordinate zero, boolean yAxis);
    public IGraphApplication getApplication();
}
