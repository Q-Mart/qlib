package uk.qumarth;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

import java.util.List;

public class Convolutions {
    public static PImage apply(String imgPath, List<List<Float>> kernel, PApplet sketch) {
        PImage img = sketch.loadImage(imgPath);
        return apply(img, kernel, sketch);
    }

    public static PImage apply(PImage img, List<List<Float>> kernel, PApplet sketch) {
        // Convert to grayscale
        img.filter(PConstants.GRAY);
        img.loadPixels();

        int widthStop = sketch.width - kernel.getFirst().size();
        int heightStop = sketch.height - kernel.size();

        for (int x=0; x<widthStop; x++) {
            for (int y=0; y<=heightStop; y++) {
                applyKernelToSubPixelArea(img, kernel, x, y, sketch);
            }
        }

        return img;
    }

    private static void applyKernelToSubPixelArea(
            PImage img,
            List<List<Float>> kernel,
            int startX,
            int startY,
            PApplet sketch
    ) {
        int kernelWidth = kernel.getFirst().size();
        int kernelHeight = kernel.size();

        float newPixelValue = 0;
        for (int i=0; i<kernelWidth; i++) {
            for (int j=0; j<kernelHeight; j++) {
                int x = startX + i;
                int y = startY + j;

                int index = getIndexFromXYCoords(img, x, y);
                newPixelValue += sketch.red(img.pixels[index]) * kernel.get(j).get(i);
            }
        }

        int index = getIndexFromXYCoords(img, startX, startY);
        img.pixels[index] = sketch.color((int) newPixelValue, (int) newPixelValue, (int) newPixelValue);
    }

    private static int getIndexFromXYCoords(PImage img, int x, int y) {
        if (x >= img.width) {
            throw new IndexOutOfBoundsException("x value of "+x+" greater than image width of "+img.width);
        }

        if (y >= img.height) {
            throw new IndexOutOfBoundsException("y value of "+y+" greater than image height of "+img.height);
        }

        return (y * (img.width)) + x;
    }
}
