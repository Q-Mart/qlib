package qlib

import processing.core._
import PApplet._

object SobelOperator {
  // private val blur = Array(
  //     Array(0.075f, 0.15f, 0.075f),
  //     Array(0.15f, 0.3f, 0.15f),
  //     Array(0.075f, 0.15f, 0.075f)
  // )

  private val blur = Array(
      Array(0.05f, 0.1f, 0.05f),
      Array(0.1f, 0.2f, 0.01f),
      Array(0.05f, 0.1f, 0.05f)
  )

  private val gx = Array(
      Array(1.0f, 0.0f, -1.0f),
      Array(2.0f, 0.0f, -2.0f),
      Array(1.0f, 0.0f, -1.0f)
  )

  private val gy = Array(
      Array(1.0f, 2.0f, 1.0f),
      Array(0.0f, 0.0f, 0.0f),
      Array(-1.0f, -2.0f, -1.0f)
  )

  var x_img: PImage = null
  var y_img: PImage = null

  def apply(sketch: PApplet, img_path: String): PImage = {
    val blur_applier = new ConvolutionApplier(sketch, blur)
    val x_applier = new ConvolutionApplier(sketch, gx)
    val y_applier = new ConvolutionApplier(sketch, gy)

    x_img = blur_applier.apply(img_path)
    y_img = blur_applier.apply(img_path)

    x_img = x_applier.apply(x_img)
    y_img = y_applier.apply(y_img)

    x_img.loadPixels()
    y_img.loadPixels()

    for (i <- 0 to x_img.pixels.length-1) {
      val x = sketch.red(x_img.pixels(i)).toFloat
      val y = sketch.red(y_img.pixels(i)).toFloat

      val v = sqrt(pow(x, 2.0f) + pow(y, 2.0f)).toInt
      x_img.pixels(i) = sketch.color(v, v, v)
    }

    x_img.updatePixels()
    x_img
  }
}
