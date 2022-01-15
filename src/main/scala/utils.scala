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
}
