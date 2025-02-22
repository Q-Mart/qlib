package uk.qumarth.qlib;

import processing.core.PApplet;
import processing.core.PVector;

public class Border {
    /**
     * Traditional border:
     *  - top = + 7% of image size
     *  - bottom = +13% of image size
     *  - sides = +5% of image size
     *
     *  Modern border:
     *  - +5% of image size to every size
     *
     * E.g. modern side borders
     *  x + (0.1x) = 1000
     *  x = 1000/1.1
     *
     * E.g. traditional top and bottom borders
     * y + (0.2y) = 1000
     * y = 1000/1.2
     *
     * highest_y_coord = y * 0.07
     * lowest_y_coord = highest_y_coord + (1000/0.2)
     */

    public enum BorderType {
        TRADITIONAL,
        MODERN;
    }

    private final BorderType type;
    private final PVector start;
    private final PVector end;

    public Border(BorderType type, PApplet app) {
        this.type = type;
        this.start = calculateStart(app);
        this.end = calculateEnd(app);
    }

    private PVector calculateStart(PApplet app) {
        float totalImageWidth = app.width / 1.1f;
        float x = totalImageWidth * 0.05f;

        float y = 0f;
        switch (type) {
            case TRADITIONAL -> {
                float totalImageHeight = app.height / 1.2f;
                y = totalImageHeight * 0.07f;
            }
            case MODERN -> {
                float totalImageHeight = app.height /  1.1f;
                y = totalImageHeight * 0.05f;
            }
        }

        return new PVector(x, y);
    }

    private PVector calculateEnd(PApplet app) {
        float totalImageWidth = app.width / 1.1f;
        float x = start.x + totalImageWidth;

        float totalImageHeight = type.equals(BorderType.TRADITIONAL) ?
                app.height / 1.2f : app.height / 1.1f;
        float y = start.y + totalImageHeight;

        return new PVector(x, y);
    }

    public PVector getStart() {
        return start;
    }

    public PVector getEnd() {
        return end;
    }
}
