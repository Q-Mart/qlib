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
    }

    public static List<PVector> chaikinCurveSmoothing(List<PVector> points, int n) {
        List<PVector> currentChaikinPoints = points;
        for (int i=0; i<n; i++) {
            currentChaikinPoints = singleChaikinStep(currentChaikinPoints);
        }

        return currentChaikinPoints;
    }

    public static void drawNaturalLine(PVector start, PVector end, int iterations, Random r, PApplet sketch) {
        int transparentCol = Colour.addAlphaToHSBColour(sketch.g.fillColor, 1f / iterations, sketch);

        // Wobble points from coords to create a base line
        List<PVector> baseLine = wobbleLine(start, end, r, sketch);
        baseLine = chaikinCurveSmoothing(baseLine, 4);

        sketch.beginShape();
        for (PVector vec: baseLine) {
            sketch.vertex(vec.x, vec.y);
        }
        sketch.endShape();
    }

    private static List<PVector> wobbleLine(PVector start, PVector end, Random r, PApplet sketch) {
        // Add a random slant
        PVector slantedStart = addNoiseToPVector(start, r, sketch);
        PVector slantedEnd = addNoiseToPVector(end, r, sketch);

        // Interpolate points and add wobble
        List<PVector> points = new ArrayList<>();
        points.add(slantedStart);
        for (float interpolationAmount = 0.001f; interpolationAmount < 1f; interpolationAmount += 0.001f) {
            PVector nextPoint = new PVector(
                    PApplet.lerp(slantedStart.x, slantedEnd.x, interpolationAmount),
                    PApplet.lerp(slantedStart.y, slantedEnd.y, interpolationAmount)
            );

            points.add(addNoiseToPVector(nextPoint, r, sketch));
        }

        points.add(slantedEnd);

        return points;
    }

    private static List<PVector> singleChaikinStep(List<PVector> points) {
        List<PVector> result = new ArrayList<>();

        // Sliding window over two points
        result.add(points.getFirst());
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
            result.add(p1);
        }

        return result;
    }

    private static PVector addNoiseToPVector(PVector v, Random r, PApplet sketch) {
        return new PVector(
                v.x + RandomUtils.randomGaussian(sketch.width/100000f, sketch.width/100000f, r),
                v.y + RandomUtils.randomGaussian(sketch.height/100000f, sketch.height/100000f, r)
        );
    }
}
