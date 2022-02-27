package qlib

import processing.core._
import PConstants._
import PApplet._

case class Polygon(points: Array[Point], center: Point)

case class PaintPoint(
    colval_1: Float,
    colval_2: Float,
    colval_3: Float,
    layers: Array[Polygon]
)

class WatercolourPainter(sketch: PApplet) {
  var paint_points: Array[PaintPoint] = Array()

  private def polygon(
      x: Float,
      y: Float,
      radius: Float,
      n_points: Int
  ): Polygon = {
    val angle: Float = TWO_PI / n_points;

    var line: Array[Point] = Array()

    for (a <- 0f to TWO_PI by angle) {
      val sx = x + cos(a) * radius
      val sy = y + sin(a) * radius
      line = line :+ Point(sx, sy)
    }

    val final_x = x + radius
    val final_y = y
    line = line :+ Point(final_x, final_y)

    Polygon(line, Point(x, y))
  }

  private def deform(polygon: Polygon, i: Int = 1): Polygon = {
    val deformations = polygon.points
      .sliding(2)
      .map(ps => {
        val p1 = ps(0)
        val p2 = ps(1)

        val dx = p1.x - p2.x
        val dy = p1.y - p2.y

        val factor = Utils.random_gaussian(0.5f, 0.1f)
        val between_x = p1.x - (dx * factor)
        val between_y = p1.y - (dy * factor)
        val between = Point(between_x, between_y)

        val heading =
          atan2((between_y - polygon.center.y), (between_x - polygon.center.x))
        val a_delta = Utils.random_gaussian(0f, 0.5f)
        // val a_delta = 0
        val a = heading + a_delta

        val dist = sqrt(pow(dx, 2) + pow(dy, 2)) * factor
        val r = Utils.random_gaussian(dist, dist / 5)

        val new_x = between_x + cos(a) * r
        val new_y = between_y + sin(a) * r

        Point(new_x, new_y)
      })
      .toList

    val new_points =
      polygon.points.zip(deformations).flatMap(t => List(t._1, t._2))

    val result = Polygon(new_points, polygon.center)

    i match {
      case 1 => {
        // val smoothed_points = Utils.chaikin_curve_smoothing(result.points, 2)
        val smoothed_points = result.points
        Polygon(smoothed_points, result.center)
      }
      case _ => deform(result, i - 1)
    }
  }

  private def deform_and_layer(
      shape: Polygon,
      colour: Tuple3[Float, Float, Float]
  ) = {
    val base = deform(shape, 3)

    var layers: Array[Polygon] = Array()
    for (i <- 0 to 50) {
      layers = layers :+ deform(base, 3)
    }

    PaintPoint(colour._1, colour._2, colour._3, layers)
  }

  def paint_at(
      x: Float,
      y: Float,
      r: Float = 50,
      colour: Tuple3[Float, Float, Float]
  ) = {
    val paint_point = deform_and_layer(polygon(x, y, r, 10), colour)

    paint_points = paint_points :+ paint_point
  }

  def paint_custom_shape(
      points: Array[Point],
      r: Float = 50,
      colour: Tuple3[Float, Float, Float]
  ) = {
    val dx = points.head.x - points.last.x
    val dy = points.head.y - points.last.y

    val cx = points.head.x - dx
    val cy = points.head.y - dy

    val shape = Polygon(points, Point(cx, cy))
    val paint_point = deform_and_layer(shape, colour)

    paint_points = paint_points :+ paint_point
  }

  def draw(opacity: Float = 10) = {
    for (pp <- paint_points) {
      sketch.fill(pp.colval_1, pp.colval_2, pp.colval_3, 10)

      for (l <- pp.layers) {
        sketch.beginShape()
        for (p <- l.points) {
          sketch.vertex(p.x, p.y)
        }
        sketch.endShape()
      }
    }
  }
}
