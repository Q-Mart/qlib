package uk.qumarth.qlib.ManualTesting;

import processing.core.PApplet;

public class TestApplication extends PApplet {

    public void settings() {
        size(500, 500);
    }

    public void setup() {
        colorMode(HSB, 360, 100, 100);
        noLoop();
        noStroke();
    }

    public void draw() {}

    public static void runMain() {
        TestApplication.main("uk.qumarth.qlib.ManualTesting.TestApplication");
    }

    public static void main(String[] args) {
        runMain();
    }
}
