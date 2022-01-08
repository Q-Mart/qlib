package qlib

import scala.util.Random

case class Point(val x: Float, val y: Float)

object Utils {
  def random_float(max: Float): Float = Random.nextFloat * max

  def random_gaussian(mean: Float, std_dev: Float): Float = {
    mean + Random.nextGaussian.toFloat * std_dev
  }

  def random_point(max_x: Float, max_y: Float): Point = {
    Point(random_float(max_x), random_float(max_y))
  }
}
