package uk.qumarth.qlib;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NaturalLine {
    public static void draw(List<PVector> points, Random r, PApplet sketch) {
        List<PVector> baseLine = points.stream().map(p -> baselineNoise(p, r)).toList();
        baseLine = Drawing.chaikinCurveSmoothing(baseLine, 4);
        baseLine = smooth(baseLine);

        Drawing.drawLineFromPVectors(baseLine, sketch);

        sketch.noStroke();
        for (int i=0; i<15; i++) {
            List<PVector> textured = getTexturedLine(baseLine, r);
            for (PVector v : textured) {
                sketch.circle(v.x, v.y, 0.4f);
            }
        }
    }

    public static void draw(PVector start, PVector end, Random r, PApplet sketch) {
        List<PVector> baseLine = createInterpolatedPoints(start, end, r);
        draw(baseLine, r, sketch);
    }

    private static List<PVector> createInterpolatedPoints(PVector start, PVector end, Random r) {
        List<PVector> points = new ArrayList<>();
        points.add(start);

        // Interpolate points and add wobble
        for (float interpolationAmount = 0.1f; interpolationAmount < 1f; interpolationAmount += 0.05f) {
            PVector nextPoint = new PVector(
                    PApplet.lerp(start.x, end.x, interpolationAmount),
                    PApplet.lerp(start.y, end.y, interpolationAmount)
            );

            points.add(baselineNoise(nextPoint, r));
        }

        points.add(end);

        return points;
    }

    /**
     * Take the baseline, add noise to the Y co-ords, and get some vectors that are
     * in-between the defined points in the list
     */
    private static List<PVector> getTexturedLine(List<PVector> points, Random r) {
        List<PVector> result = new ArrayList<>();
        List<PVector> adjustedPoints = points.stream().map(v -> texturedNoise(v, r)).toList();

        // Sliding window over two points
        for (int i=0; i<adjustedPoints.size()-2; i++) {
            PVector p0 = adjustedPoints.get(i);
            PVector p1 = adjustedPoints.get(i+1);

            result.add(p0);
            for (int j=0; j<10; j++) {
                result.add(
                        new PVector(
                                PApplet.lerp(p0.x, p1.x, RandomUtils.randomRange(0f, 1f, r)),
                                PApplet.lerp(p0.y, p1.y, RandomUtils.randomRange(0f, 1f, r))
                        )
                );
            }
            result.add(p1);
        }

        return smooth(result);
    }

    private static PVector baselineNoise(PVector v, Random r) {
        return new PVector(
                v.x,
                v.y + RandomUtils.randomGaussian(2f, 5, r)
        );
    }

    private static PVector texturedNoise(PVector v, Random r) {
        return new PVector(
                v.x,
                v.y + RandomUtils.randomGaussian(0f, 0.3f, r)
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
