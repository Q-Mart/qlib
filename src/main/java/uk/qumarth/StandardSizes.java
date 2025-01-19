package uk.qumarth;

import processing.core.PVector;

public enum StandardSizes {
    A0(new PVector(9933, 14043)),
    A1(new PVector(7016, 9933)),
    A2(new PVector(4960, 7016)),
    A3(new PVector(3508, 4960)),
    A4(new PVector(2480, 3508)),
    A5(new PVector(1748, 2480));

    StandardSizes(PVector pVector) {
    }
}