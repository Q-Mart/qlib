package uk.qumarth.qlib;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.List;
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
}
