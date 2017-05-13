package com.weaxme.graph.application.graph;

import com.weaxme.graph.application.IGraphApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author Vitaliy Gonchar
 * Represents pixel coordinates in Swing component
 */
public class PixelCoordinate implements Serializable {
    private final int x;
    private final int y;

    private final Coordinate point;

    private final boolean valid;

    private static final Logger LOG = LoggerFactory.getLogger(PixelCoordinate.class);

    public PixelCoordinate(Coordinate point, IGraphApplication app) {
        this.point = point;
        Double x = point.getX();
        Double y = point.getY();
        int xLength = (int) Math.abs(x * app.getMarkPixelStep() * app.getPointMultiplier());
        int yLength = (int) Math.abs(y * app.getMarkPixelStep() * app.getPointMultiplier());
        if (x < 0) {
            xLength = app.getX0() - xLength;
        } else xLength += app.getX0();

        if (y < 0) {
            yLength = Math.abs(app.getY0() + yLength);
        } else yLength = app.getY0() - yLength;

        if (yLength > app.getGraphMaxHeight() || xLength > app.getGraphMaxWidth()
                || yLength < app.getBorderPixelLimit() || xLength < app.getBorderPixelLimit() || xLength <= 0 || yLength <= 0) {
            this.x = -1;
            this.y = -1;
            valid = false;
        } else {
            this.x = xLength;
            this.y = yLength;
            valid = true;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isValid() {
        return valid;
    }

    public Coordinate getPoint() {
        return point;
    }

    @Override
    public String toString() {
        return "PixelCoordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
