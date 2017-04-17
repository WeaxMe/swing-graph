package com.weaxme.graph.service;

import java.io.Serializable;

/**
 * @author Vitaliy Gonchar
 * Represents pixel coordinates in Swing window
 */
public class PixelCoordinate implements Serializable {
    private final int x;
    private final int y;

    private final boolean valid;

    public PixelCoordinate(Coordinate point, IGraphApplication app) {
        Double x = point.getX();
        Double y = point.getY();
        int xLength = Math.abs((int) (x * app.getPixelStep()));
        int yLength = Math.abs((int) (y * app.getPixelStep()));
        if (x < 0) {
            xLength = app.getX0() - Math.abs(xLength);
        } else xLength += app.getX0();
        if (y < 0) {
            yLength = Math.abs(app.getY0() + Math.abs(yLength));
        } else yLength = app.getY0() - yLength;

        if (yLength > app.getGraphMaxHeight() || xLength > app.getGraphMaxWidth()
                || yLength < app.getBorderPixelLimit() || xLength < app.getBorderPixelLimit() || xLength < 0 || yLength < 0) {
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

    @Override
    public String toString() {
        return "PixelCoordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
