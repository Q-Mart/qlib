package uk.qumarth.qlib;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static processing.core.PApplet.*;
import static processing.core.PConstants.TWO_PI;

public class Watercolour {


    public static void draw(PVector point, int colour, float radius, Random random, PApplet sketch) {
        int previousColor = sketch.g.fillColor;
        int colourWithAlpha = colourWithAlpha(colour, sketch);

        Drawing.Polygon p = polygon(point, radius, 10);

        // Doing lots of deforming is memory and time intensive. This is an optimisaiton
        // Create a base shape
        for (int i=0; i<10; i++) {
            p = deform(p, random);
            Drawing.drawShapeFromPVectors(p.points(), colourWithAlpha, sketch);
        }

        // Deform the layer deformations of the base shape
        for (int i=0; i<50; i++) {
            Drawing.Polygon deformed3Times = deform(
                    deform(deform(p, random), random),
                    random
            );
            Drawing.drawShapeFromPVectors(deformed3Times.points(), colourWithAlpha, sketch);
        }

        sketch.color(previousColor);
    }

    private static Drawing.Polygon polygon(PVector center, float radius, int number_of_points) {
        List<PVector> points = new ArrayList<>();
        float angle = TWO_PI / number_of_points;

        for (float a = 0f; a <= TWO_PI; a+=angle) {
            points.add(
                    new PVector(
                            center.x + cos(a) * radius,
                            center.y + sin(a) * radius
                    )
            );
        }

        return new Drawing.Polygon(center, points);
    }

    private static Drawing.Polygon deform(Drawing.Polygon polygon, Random random) {
        // We need to get a point between first and last point in the polygon
        // So create a new list and add the first point to the end of it
        // This way the sliding window can work correctly
        List<PVector> allPointsToTraverse = new ArrayList<>(polygon.points());
        allPointsToTraverse.add(polygon.points().getFirst());

        ArrayList<PVector> newPoints = new ArrayList();

        for (int i = 0; i < allPointsToTraverse.size() - 1; i++) {
            PVector p1 = allPointsToTraverse.get(i);
            PVector p2 = allPointsToTraverse.get(i+1);

            newPoints.add(p1);

            // Get dy and dx
            var diff = p1.copy().sub(p2);

            // Use dy and dx to get a point between p1 and p2
            float factor = RandomUtils.randomGaussian(0.5f, 0.1f, random);
            var newPointBetweenP1AndP2 = p1.copy().sub(diff.copy().mult(factor));

            var vectorFromCenterToNewPoint = newPointBetweenP1AndP2.copy().sub(polygon.center());
            // vectorFromCenterToNewPoint is equal to translating the polygon so that the center is on the origin
            // atan2 gives us the angle between the x axis and the vector
            float angleOfPointFromCenterOfPolygon = atan2(vectorFromCenterToNewPoint.y, vectorFromCenterToNewPoint.x);
            float angleNoise = RandomUtils.randomGaussian(1f, 0.5f, random);
            float newAngle = angleOfPointFromCenterOfPolygon + angleNoise;

            float distance = diff.mag() * factor;
            float r = RandomUtils.randomGaussian(distance, distance / 5f, random);

            PVector newPoint = newPointBetweenP1AndP2.add(
                    new PVector(cos(newAngle) * r, sin(newAngle) * r)
            );

            newPoints.add(newPoint);
        }

        return new Drawing.Polygon(polygon.center(), newPoints);
    }

    private static int colourWithAlpha(int colour, PApplet sketch) {
        float h = sketch.hue(colour);
        float s = sketch.saturation(colour);
        float b = sketch.brightness(colour);

        return sketch.color(h, s, b, 7f);
    }
}
