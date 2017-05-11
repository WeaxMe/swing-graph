package com.weaxme.graph.application;

import com.google.inject.ImplementedBy;
import com.weaxme.graph.service.Coordinate;
import com.weaxme.graph.application.graph.IGraph;
import com.weaxme.graph.service.IGraphPanel;
import com.weaxme.graph.service.PixelCoordinate;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vitaliy Gonchar
 */
@ImplementedBy(GraphApplication.class)
public interface IGraphApplication extends Serializable {
    public IGraphApplication updateGraph(PixelCoordinate point1, PixelCoordinate point2);
    public IGraphApplication repaintGraph();
    public IGraphApplication repaintGraphWithoutDelay();
    public IGraphApplication setGraph(IGraph graph);
    public IGraphApplication setGraphDelay(long delay);
    public IGraphApplication setBorderPixelLimit(int limit);
    public IGraphApplication setMarkLength(int markLength);
    public IGraphApplication setGraphPanel(IGraphPanel graphPanel);
    public IGraphApplication setGraphMaxHeight(int height);
    public IGraphApplication setGraphMaxWidth(int width);
    public IGraphApplication setNowRepaint(boolean repaint);
    public IGraphApplication setNowGraphBuild(boolean build);
    public IGraphApplication setGraphLineWidth(int width);
    public IGraphApplication setPointMultiplier(double multiplier);
    public IGraphApplication setX0(int x0);
    public IGraphApplication setY0(int y0);

    public IGraph getGraph();
    public long getGraphDelay();
    public int getMarkPixelStep();
    public int getXPixelStep();
    public int getYPixelStep();
    public int getBorderPixelLimit();
    public int getMarkLength();
    public int getX0();
    public int getY0();
    public int getGraphMaxHeight();
    public int getGraphMaxWidth();
    public double getMarkStep();
    public List<Coordinate> getGraphPoints();
    public IGraphPanel getGraphPanel();
    public boolean isNowGraphBuild();
    public int getGraphLineWidth();
    public List<Integer> getPossibleWidth();
    public double getPointMultiplier();

}