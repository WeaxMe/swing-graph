package com.weaxme.graph.component.util;

public abstract class GraphPaintUtil {

    private GraphPaintUtil() {
    }

    public static String getDoubleFormat(double number) {
        return number < 1 ? "%.3f" : number >= 10 ? "%.0f" : "%.1f";
    }

    public static String getDoubleString(double number) {
        return String.format(getDoubleFormat(number), number);
    }
}
