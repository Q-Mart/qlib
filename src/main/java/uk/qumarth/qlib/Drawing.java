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

    public static void drawNaturalLine(PVector start, PVector end, int iterations, Random r, PApplet sketch) {
        // Wobble points from coords to create a base line
        List<PVector> baseLine = wobbleLine(start, end, r, sketch);
        baseLine = chaikinCurveSmoothing(baseLine, 5);
        baseLine = smooth(baseLine);

        drawLineFromPVectors(baseLine, sketch);

        int transparentCol = Colour.addAlphaToHSBColour(sketch.g.fillColor, 1f / iterations, sketch);
        // TODO: push this onto a stack then pop off
        sketch.color(transparentCol);
        for (int i=0; i<iterations; i++) {
            List<PVector> textured = baseLine.stream().map(v -> wobblesAfter(v, r, sketch)).toList();
            for (PVector v : textured) {
                sketch.circle(v.x, v.y, 0.2f);
            }
        }
    }

    private static List<PVector> wobbleLine(PVector start, PVector end, Random r, PApplet sketch) {
        // Add a random slant
        PVector slantedStart = initialWobble(start, r, sketch);
        PVector slantedEnd = initialWobble(end, r, sketch);

        // Interpolate points and add wobble
        List<PVector> points = new ArrayList<>();
        points.add(slantedStart);
        for (float interpolationAmount = 0.1f; interpolationAmount < 1f; interpolationAmount += 0.05f) {
            PVector nextPoint = new PVector(
                    PApplet.lerp(slantedStart.x, slantedEnd.x, interpolationAmount),
                    PApplet.lerp(slantedStart.y, slantedEnd.y, interpolationAmount)
            );

            points.add(initialWobble(nextPoint, r, sketch));
        }

        points.add(slantedEnd);

        return points;
    }

    private static PVector initialWobble(PVector v, Random r, PApplet sketch) {
        //
        return new PVector(
                v.x + RandomUtils.randomGaussian(sketch.width/300f, sketch.width/500f, r),
                v.y + RandomUtils.randomGaussian(sketch.height/300f, sketch.height/500f, r)
        );
    }

    private static PVector wobblesAfter(PVector v, Random r, PApplet sketch) {
        return new PVector(
                v.x + RandomUtils.randomGaussian(sketch.width/750f, sketch.width/750f, r),
                v.y + RandomUtils.randomGaussian(sketch.height/7500f, sketch.height/750f, r)
        );

    }

    private static List<PVector> smooth(List<PVector> points) {
        List<PVector> result = new ArrayList<>();

        // Sliding window over two points
        for (int i=0; i<points.size()-2; i++) {
            PVector p0 = points.get(i);
            PVector p1 = points.get(i+1);

            result.add(
                    new PVector(
                            (p0.x + p1.x) / 2f,
                            (p0.y + p1.y) / 2f
                    )
            );
        }

        return result;
    }
}
