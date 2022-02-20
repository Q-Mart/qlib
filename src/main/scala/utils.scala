package qlib

import processing.core._
import PConstants._
import PApplet._

import scala.util.Random
import scala.annotation.tailrec

case class Point(val x: Float, val y: Float)

object Utils {
  def random_range(min: Int, max: Int): Int = {
    min + Random.nextInt((max - min) + 1)
  }

  def random_float_range(min: Float, max: Float): Float = {
    min + random_float((max - min) + 1)
  }

  def random_float(max: Float): Float = Random.nextFloat * max

  def random_gaussian(mean: Float, std_dev: Float): Float = {
    mean + Random.nextGaussian.toFloat * std_dev
  }

  def random_point(max_x: Float, max_y: Float): Point = {
    Point(random_float(max_x), random_float(max_y))
  }

  def draw_line_from_points(sketch: PApplet, ps: List[Point]) = {
    sketch.beginShape(LINES)
    ps.sliding(2).foreach {
      case List(p1, p2) => {
        sketch.vertex(p1.x, p1.y)
        sketch.vertex(p2.x, p2.y)
      }
      case List(p1) => {
        sketch.vertex(p1.x, p1.y)
      }
    }
    sketch.endShape()
  }

  @tailrec
  def chaikin_curve_smoothing(points: Array[Point], num: Int): Array[Point] = {
    if (num <= 0) {
      return points
    }

    val l = points.length

    val smooth = points.zipWithIndex.map {
      case (_, i) if i == l - 1 => List()

      case (p, i) =>
        val p_next = points((i + 1) % l)
        List(
          Point(
            0.75f * p.x + 0.25f * p_next.x,
            0.75f * p.y + 0.25f * p_next.y
          ),
          Point(
            0.25f * p.x + 0.75f * p_next.x,
            0.25f * p.y + 0.75f * p_next.y
          )
        )
    }.flatten

    if (num == 1) {
      return smooth
    }

    chaikin_curve_smoothing(smooth, num - 1)
  }

}
