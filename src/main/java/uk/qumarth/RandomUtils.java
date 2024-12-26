package uk.qumarth;

import processing.core.PVector;

import java.util.Random;

public class RandomUtils {
    public static int randomRange(int min, int max, Random r) {
        return min + r.nextInt((max - min) + 1);
    }

    public static float randomRange(float min, float max, Random r) {
        return min + randomFloat((max - min) + 1, r);
    }

    public static float randomFloat(float max, Random r) {
        return r.nextFloat() * max;
    }

    public static float randomGaussian(float mean, float stdDev, Random r) {
        return mean + ((float) r.nextGaussian()) * stdDev;
    }

    public static PVector randomPVector2(float maxX, float maxY, Random r) {
        return new PVector(randomFloat(maxX, r), randomFloat(maxY, r));
    }
}
