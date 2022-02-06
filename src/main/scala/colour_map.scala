package qlib

import processing.core._
import PConstants._
import PApplet._

import scala.util.Random

import qlib._

case class Colour(value: Int, x: Float, y: Float)

class ColourMap(val sketch: PApplet, val max_x: Float, val max_y: Float) {
  private var colours: Array[Colour] = Array()

  def add_colour(c: Colour) = colours +:= c

  def add_colour_at_random_point(c: Int) = {
    add_colour(
      Colour(
        c,
        Utils.random_gaussian(max_x/2, max_x/4),
        Utils.random_gaussian(max_y/2, max_y/4)
      )
    )
  }

  private def distance(p0: Point, p1: Point): Float = {
    sqrt(
      pow(p0.x - p1.x, 2f) +
      pow(p0.y - p1.y, 2f)
    )
  }

  private def closer_than(p0: Point, p1: Point, p2: Point): Boolean = {
    distance(p0, p1) < distance(p0, p2)
  }

  def get_random_col = colours(Random.nextInt(colours.size)).value

  def get_nearest_col(p: Point): Int = {
    val closest = colours.sortWith((c1, c2) => {
      closer_than(p, Point(c1.x, c1.y), Point(c2.x, c2.y))
    }).head

    sketch.color(closest.value)
  }

  def get_colour_at(p: Point): Int = {
    // First, find the 2 nearest neighbours
    val closest_2_cols = colours.sortWith((c1, c2) => {
      closer_than(p, Point(c1.x, c1.y), Point(c2.x, c2.y))
    }).take(2)

    val closest = closest_2_cols(0)
    val second_closest = closest_2_cols(1)

    val d_to_closest = distance(p, Point(closest.x, closest.y))
    val d_to_2nd_closest = distance(p, Point(second_closest.x, second_closest.y))

    val lerp_value = (1f - (d_to_closest / (d_to_closest + d_to_2nd_closest)))

    sketch.lerpColor(closest.value, second_closest.value, lerp_value)
  }

  def get_colours: Array[Colour] = colours

  def draw_points = {
    val col_coords = colours.map(c => Point(c.x, c.y))
    for (p <- col_coords) {
      sketch.stroke(0)
      sketch.circle(p.x, p.y, 40)
    }
  }

  def draw_colours_on_canvas = {
    for (x <- 0 to max_x.toInt-1) {
      for (y <- 0 to max_y.toInt-1) {
        val current_point = Point(x.toFloat, y.toFloat)
        sketch.stroke(get_colour_at(current_point))
        sketch.circle(x, y, 1)
      }
    }
  }

  def draw_colours_and_points = {
    draw_colours_on_canvas
    draw_points
  }
}
