package com.weaxme.graph.service;

import java.io.Serializable;

/**
 * @author Vitaliy Gonchar
 * Represents double coordinates for graph.
 */
public class Coordinate implements Serializable {
    private final Double x;
    private final Double y;

    public Coordinate(Double x, Double y) {
        if (x == null || y == null) throw new IllegalStateException("Coordinates cannot be null! x=" + x + " y=" + y);
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public boolean isNaN() {
        return Double.isNaN(x) || Double.isNaN(y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (!x.equals(that.x)) return false;
        return y.equals(that.y);
    }

    @Override
    public int hashCode() {
        int result = x.hashCode();
        result = 31 * result + y.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
