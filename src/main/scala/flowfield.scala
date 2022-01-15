package qlib

import processing.core._
import PConstants._
import PApplet._

import scala.annotation.tailrec

import scala.util.Random

import qlib._

class Flowfield(
  val sketch: PApplet,
  val width: Integer,
  val height: Integer,
  val resolution_fraction: Float
)  {
  private val left_x = (width.toFloat * -0.5).toInt
  private val right_x = (width.toFloat * 1.5).toInt
  private val top_y = (height.toInt * -0.5).toInt
  private val bottom_y = (height.toInt * 1.5).toInt

  private val resolution = (width.toFloat * resolution_fraction).toInt

  private val num_columns = (right_x - left_x) / resolution
  private val num_rows = (bottom_y - top_y) / resolution

  var grid = Array.ofDim[Float](num_rows, num_columns)

  for (col <- 0 to num_columns-1) {
    for (row <- 0 to num_rows-1) {
      val angle = (row.toFloat / num_rows.toFloat) * PI
      grid(row)(col) = angle
    }
  }

  private def grid_to_canvas_x(x : Int): Int = {
    (x * resolution) + left_x
  }

  private def grid_to_canvas_y(y: Int): Int = {
    (y * resolution) + top_y
  }

  private def canvas_x_to_grid(x : Int): Int = {
    var r = ((x - left_x) / resolution)

    while (r < 0) {
      r = num_columns + r
    }

    r % num_columns
  }

  private def canvas_y_to_grid(y : Int): Int = {
    var r = ((y - top_y) / resolution) % num_rows

    while (r < 0) {
      r = num_rows + r
    }

    r % num_rows
  }

  def change_value_at(p: Point, v: Float) {
    val x = canvas_x_to_grid(p.x.toInt)
    val y = canvas_y_to_grid(p.y.toInt)

    grid(y)(x) = v
  }

  def randomly_generate_values {

    val egen1 = new ExpressionGenerator(sketch)
    val egen2 = new ExpressionGenerator(sketch)
    println("x_func=", egen1)
    println("y_func=", egen2)

    for (y <- 0 to num_rows-1) {
      for (x <- 0 to num_columns-1) {
        val p = Point(
          x.toFloat/num_columns.toFloat,
          y.toFloat/num_rows.toFloat
        )

        val x_mag = egen1.eval(p.x, p.y)
        val y_mag = egen2.eval(p.x, p.y)
        val angle = atan(y_mag/x_mag) * 2 * PI
        grid(y)(x) = angle
      }
    }
  }

  def draw_arrows() {
    for (col <- 0 to num_columns-1) {
      for (row <- 0 to num_rows-1) {
        val (x1, y1) = (grid_to_canvas_x(col), grid_to_canvas_y(row))
        val angle = grid(row)(col)

        val x2 = x1.toFloat + (3 * cos(angle))
        val y2 = y1.toFloat + (3 * sin(angle))

        sketch.line(x1, y1, x2, y2)
        sketch.circle(x2, y2, 2)
      }
    }
  }

  @tailrec
  final def get_points_on_curve(
    points: List[Point],
    step_size: Int,
    num_steps: Int
  ): List[Point] = num_steps match {
    case 0 => points
    case _ => {
      val start = points.last
      val col = canvas_x_to_grid(start.x.toInt)
      val row = canvas_y_to_grid(start.y.toInt)

      val angle = grid(row)(col)

      val x2 = start.x.toFloat + (step_size * cos(angle))
      val y2 = start.y.toFloat + (step_size * sin(angle))

      val new_point = Point(x2, y2)

      get_points_on_curve(points :+ new_point, step_size, num_steps-1)
    }
  }

  def create_curve(
    start: Point,
    step_size: Int=10,
    num_steps: Int=500
    ): List[Point] = get_points_on_curve(List(start), step_size, num_steps)

  def draw_curve(ps: List[Point]) {
    draw_line_from_points(sketch, ps)
  }

  def copy: Flowfield = {
    var new_flowfield = new Flowfield(sketch, width, height, resolution_fraction)

    for (col <- 0 to num_columns-1) {
      for (row <- 0 to num_rows-1) {
        val x = grid_to_canvas_x(col)
        val y = grid_to_canvas_y(row)
        val p = Point(x, y)

        new_flowfield.change_value_at(p, grid(row)(col))
      }
    }

    new_flowfield
  }
}
