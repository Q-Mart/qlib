package uk.qumarth.qlib;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

import static processing.core.PApplet.*;
import static processing.core.PConstants.TWO_PI;

public class InkDrop {
    private static final int NUMBER_OF_VERTICES = 100;

    private final PVector center;
    private final List<PVector> vertices;
    private final float radius;

    public InkDrop(PVector center, float radius) {
        this.center = center;
        this.vertices = createVertices(center, radius);
        this.radius = radius;
    }

    private static List<PVector> createVertices(PVector center, float radius) {
        List<PVector> vertices = new ArrayList<>();

        for (int i=0; i<NUMBER_OF_VERTICES; i++) {
            float angle = map(i, 0, NUMBER_OF_VERTICES, 0, TWO_PI);

            // TODO: Add some noise here
            PVector vertex = new PVector(radius * cos(angle), radius * sin(angle));
            // Offset by center of circle
            vertex.add(center);

            vertices.add(vertex);
        }

        return vertices;
    }

    public void marble(InkDrop other) {
        for (PVector vertex : vertices) {
            PVector p = vertex.copy();
            PVector c = other.center;

            // Maths on a PVector alters its state
            // Create a tmp PVector to save the workings of every mathematical step
            // We should end up with tmp being the new value of p
            PVector tmp = p.sub(c);

            float root = sqrt(1 + (pow(other.radius, 2) / tmp.magSq()));
            tmp.mult(root);
            tmp.add(c);

            vertex.set(tmp);
        }
    }

    public void draw(PApplet sketch) {
        sketch.beginShape();
        for (PVector vertex : vertices) {
            sketch.vertex(vertex.x, vertex.y);
        }
        sketch.endShape();
    }
};

