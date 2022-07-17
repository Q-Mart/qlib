package qlib

import processing.core._
import PConstants._
import PApplet._

import scala.annotation.tailrec

import qlib._
import scala.util.Random

class NaturalLineGenerator(sketch: PApplet) {

  private def offset(p: Point, sd: Float): Point = {
    Point(
      Utils.random_gaussian(p.x, p.x / sd),
      Utils.random_gaussian(p.y, p.y / sd)
    )
  }

  private def offset_with_factor(p: Point, factor: Float): Point = {
    val shift1 = Utils.random_gaussian(10 * factor, 10 * factor / 4)
    val shift2 = Utils.random_gaussian(10 * factor, 10 * factor / 4)
    Point(
      p.x + shift1,
      p.y + shift2
    )
  }

  @tailrec
  private def chaikin_curve_smoothing(
      points: Array[Point],
      num: Int
  ): Array[Point] = {
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

    chaikin_curve_smoothing(
      Array(points.head) ++ smooth ++ Array(points.last),
      num - 1
    )
  }

  def create(start: Point, end: Point): List[Point] = {
    var result = Array(start)

    // Randomly offset the end point
    val new_end = offset(end, 40)

    // Deviate from the straight line N times
    val n = Utils.random_gaussian(10, 1).toInt

    for (i <- 1 to n) {
      val lerp_val = i.toFloat / n.toFloat

      val x = lerp(start.x, new_end.x, lerp_val)
      val y = lerp(start.y, new_end.y, lerp_val)

      result +:= offset(Point(x, y), 150)
    }

    // Add the end point
    result +:= new_end

    // Neighbour influence: Create a new path using averages of the current
    // points. This makes the line appear more naturally random
    result = result
      .sliding(2)
      .map {
        case Array(p1, p2) =>
          Point(
            (p1.x + p2.x) / 2f,
            (p1.y + p2.y) / 2f
          )

        case Array(p) => p
      }
      .toArray

    // Smooth the resulting curve before returning
    chaikin_curve_smoothing(result, 5).toList
  }

  def draw(ps: List[Point]) = {
    Utils.draw_line_from_points(sketch, ps)
  }

  def draw_pencil(ps: List[Point], colour: Int = 0) = {
    sketch.colorMode(HSB, 360, 100, 100)
    val hue = sketch.hue(colour)
    val sat = sketch.saturation(colour)
    val brightness = sketch.brightness(colour)

    for (i <- 1 to 5) {
      var new_ps = ps.map(p =>
        // Don't offset every line or it looks too fuzzy
        if (Utils.random_gaussian(1, 0.2f) >= 0.4f) {
          offset_with_factor(p, 0.2f)
        } else {
          p
        }
      )

      for (p <- new_ps) {
        sketch.stroke(
          sketch.color(
            hue,
            sat,
            Utils.random_gaussian(brightness, 10)
          )
        )
        sketch.point(p.x, p.y)
      }
    }
    sketch.colorMode(RGB, 255, 255, 255)
  }
}
