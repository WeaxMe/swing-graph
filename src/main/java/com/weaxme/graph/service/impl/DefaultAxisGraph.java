package com.weaxme.graph.service.impl;

import com.google.common.collect.Lists;
import com.weaxme.graph.service.Coordinate;
import com.weaxme.graph.service.IAxisGraph;
import org.apache.commons.math3.complex.Complex;

import java.util.List;

/**
 * @author Vitaliy Gonchar
 * Example:
 * D(p) = 2p^3 + 9p^2 + 13p + 6
 * D(jw) = 2(jw)^3 + 9(jw)^2 + 13(jw) + 6
 */
public class DefaultAxisGraph implements IAxisGraph {
    @Override
    public List<Coordinate> getPoints() {
        List<Coordinate> points = Lists.newArrayList();
        for (double x = -100; x <= 100; x += 0.001) {
            Complex complex = complexFunction(x);
            points.add(new Coordinate(complex.getReal(), complex.getImaginary()));
//            points.add(new Coordinate(x, function(x)));
        }
        return points;
    }

    private Double function(Double x) {
        return x * x;
    }


    private Complex complexFunction(Double w) {
//        Complex a0 = new Complex(0, 1)
//                .multiply(2 * 2 * 2 * w * w * w);
//        Complex a1 = new Complex(0, 1)
//                .multiply(9 * 9 * w * w);
//        Complex a2 = new Complex(0, 1)
//                .multiply(13);
//        return a0.add(a1).add(a2).add(6);
        return new Complex(6 - 9 * w * w, 13 * w - 2 * w * w * w);
//        return new Complex(-2 * w * w + 1,
//                -4 * Math.pow(w, 3) + w);
    }
}
