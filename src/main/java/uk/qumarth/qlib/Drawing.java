package uk.qumarth.qlib;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Drawing {
    public static void drawLineFromPVectors(List<PVector> pVectors, PApplet sketch) {
        sketch.beginShape(PConstants.LINES);

        Stream<List<PVector>> slidingWindow = IntStream.range(0, pVectors.size() - 1)
                .mapToObj(start -> pVectors.subList(start, start + 2));

        slidingWindow.forEach(window -> {
            if (window.size() == 2) {
                PVector v1 = window.getFirst();
                PVector v2 = window.getLast();

                sketch.vertex(v1.x, v1.y);
                sketch.vertex(v2.x, v2.y);
            } else if (window.size() == 1) {
                PVector v1 = window.getFirst();

                sketch.vertex(v1.x, v1.y);
            }
        });

        sketch.endShape();
    }

    public static List<PVector> chaikinCurveSmoothing(List<PVector> points, int n) {
        List<PVector> currentChaikinPoints = points;
        for (int i=0; i<n; i++) {
            currentChaikinPoints = singleChaikinStep(currentChaikinPoints);
        }

        return currentChaikinPoints;
    }

    private static List<PVector> singleChaikinStep(List<PVector> points) {
        List<PVector> result = new ArrayList<>();

        // Sliding window over two points
        for (int i=0; i<points.size()-2; i++) {
            PVector p0 = points.get(i);
            PVector p1 = points.get(i+1);

            PVector q = new PVector(
                    (.75f * p0.x) + (0.25f * p1.x),
                    (.75f * p0.y) + (0.25f * p1.y)
            );

            PVector r = new PVector(
                    (.25f * p0.x) + (0.75f * p1.x),
                    (.25f * p0.y) + (0.75f * p1.y)
            );

            result.add(q);
            result.add(r);
        }

        return result;
    }
}
