package com.weaxme.graph.service;

import com.google.inject.ImplementedBy;
import com.weaxme.graph.service.impl.GraphApplication;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vitaliy Gonchar
 */
@ImplementedBy(GraphApplication.class)
public interface IGraphApplication extends Serializable {
    public IGraphApplication updateGraph(PixelCoordinate point1, PixelCoordinate point2);
    public IGraphApplication repaintGraph();
    public IGraphApplication setGraph(IGraph graph, double min, double max, double step);
    public IGraphApplication updateGraphDelay(long delay);
    public IGraphApplication setPixelStep(int pixelStep);
    public IGraphApplication setBorderPixelLimit(int limit);
    public IGraphApplication setMarkLength(int markLength);
    public IGraphApplication setGraphPanel(IGraphPanel graphPanel);
    public IGraphApplication setGraphMaxHeight(int height);
    public IGraphApplication setGraphMaxWidth(int width);
    public IGraphApplication setX0(int x0);
    public IGraphApplication setY0(int y0);
    public long getGraphDelay();
    public int getPixelStep();
    public int getBorderPixelLimit();
    public int getMarkLength();
    public int getX0();
    public int getY0();
    public int getGraphMaxHeight();
    public int getGraphMaxWidth();
    public List<Coordinate> getGraphPoints();
    public IGraphPanel getGraphPanel();
    public IGraphApplication setNowRepaint(boolean repaint);
    public IGraphApplication setNowGraphBuild(boolean build);
    public boolean isNowGraphBuild();

}
