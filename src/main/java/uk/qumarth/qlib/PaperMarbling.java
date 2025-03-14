package uk.qumarth.qlib;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static processing.core.PApplet.*;
import static processing.core.PConstants.TWO_PI;

/**
 * Implementation of https://www.youtube.com/watch?v=p7IGZTjC008
 */
public class PaperMarbling {

    public static void draw(Random r, PApplet sketch) {
        List<InkDrop> drops = new ArrayList<>();

        for (int i=0; i<50; i++) {
            InkDrop newDrop = new InkDrop(
                    RandomUtils.randomPVector2(sketch.width, sketch.height, r),
                    RandomUtils.randomGaussian(
                            (sketch.width * sketch.height) / pow(100, 2),
                            (sketch.width * sketch.height) / pow(200, 2),
                            r
                    )
            );

            for (InkDrop drop : drops) {
                drop.marble(newDrop);
            }

            drops.add(newDrop);
        }

        for (InkDrop drop : drops) {
            sketch.fill(RandomUtils.randomRange(0, 360, r));
            drop.draw(sketch);
        }
    }
}
