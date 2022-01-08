package qlib

import processing.core._
import PApplet._
import PConstants._

class ConvolutionApplier(val sketch: PApplet, val kernel: Array[Array[Float]]) {
  private def get_index_from_x_y(img: PImage, x: Int, y: Int): Int = {
    if (x >= img.width) {
      System.err.println("x value of "+x+" greater than image width of "+img.width)
    }

    if (y >= img.height) {
      System.err.println("y value of "+y+" greater than image height of "+img.height)
    }

    (y * (img.width)) + x
  }

  private def apply_kernel_to_subpixel_area(img: PImage, start_x: Int, start_y: Int) {
    val kernel_height = kernel.length
    val kernel_width = kernel(0).length

    var new_pixel_value = 0.0f

    for (i <- 0 to kernel_width-1) {
      for (j <- 0 to kernel_height-1) {
        val x = start_x + i
        val y = start_y + j

        val index = get_index_from_x_y(img, x, y)
        new_pixel_value += sketch.red(img.pixels(index)).toFloat * kernel(j)(i)
      }
    }

    val index = get_index_from_x_y(img, start_x, start_y)
    val value_int = new_pixel_value.toInt

    img.pixels(index) = sketch.color(value_int, value_int, value_int)
  }

  def apply(img_path: String): PImage = {
    // convert image to greyscale
    var grey_img = sketch.loadImage(img_path)
    apply(grey_img)
  }

  def apply(img: PImage): PImage = {
    img.filter(GRAY)

    img.loadPixels()

    val width_stop = img.width - 4
    val height_stop = img.height - 4
    for (x <- 0 to width_stop) {
      for (y <- 0 to height_stop) {
        apply_kernel_to_subpixel_area(img, x, y)
      }
    }

    img.updatePixels()
    img
  }
}
