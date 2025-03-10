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

    public static void drawNaturalLine(PVector start, PVector end, Random r, PApplet sketch) {
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

        sketch.beginShape();
        for (PVector point: points) {
            sketch.vertex(point.x, point.y);
        }
        sketch.endShape();
    }

    private static PVector addNoiseToPVector(PVector v, Random r, PApplet sketch) {
        return new PVector(
                v.x + RandomUtils.randomGaussian(sketch.width/100000f, sketch.width/1000f, r),
                v.y + RandomUtils.randomGaussian(sketch.height/100000f, sketch.height/1000f, r)
        );
    }
}
