package uk.qumarth.qlib;

import processing.core.PApplet;

import java.util.Random;

public class Colour {
    public static int addNoiseToHSBColour(int col, PApplet sketch, Random r) {
        return sketch.color(
                sketch.hue(col) + RandomUtils.randomGaussian(3, 4, r),
                sketch.saturation(col) + RandomUtils.randomGaussian(3, 5, r),
                sketch.brightness(col) + RandomUtils.randomGaussian(3, 5, r)
        );
    }

    public static int addAlphaToHSBColour(int col, float alpha, PApplet sketch) {
        return sketch.color(
                sketch.hue(col),
                sketch.saturation(col),
                sketch.brightness(col),
                alpha
        );
    }
}
