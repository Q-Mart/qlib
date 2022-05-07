package qlib

import processing.core._
import PConstants._
import PApplet._

import scala.util.Random

class DiamondSqareAlgo(sketch: PApplet) {

  type HeightMap = Array[Array[Float]]

  private def square_step(
      chunk_size: Int,
      roughness: Int,
      height_map: HeightMap
  ): HeightMap = {

    val half = chunk_size / 2
    val end = height_map.length

    for (y <- 0 until end - 1 by chunk_size) {
      for (x <- 0 until end - 1 by chunk_size) {
        var count = 1
        var total = height_map(y)(x)

        val x_in_bounds = x + chunk_size < end
        val y_in_bounds = y + chunk_size < end

        if (x_in_bounds) {
          total += height_map(y)(x + chunk_size)
          count += 1
        }

        if (y_in_bounds) {
          total += height_map(y + chunk_size)(x)
          count += 1
        }

        if (y_in_bounds && x_in_bounds) {
          total += height_map(y + chunk_size)(x + chunk_size)
          count += 1
        }

        val noise = -roughness + Random.nextInt((roughness * 2) + 1)
        val y_half = y + half
        val x_half = x + half
        height_map(y + half)(x + half) = (total / count) + noise
      }
    }

    height_map
  }

  private def diamond_step(
      chunk_size: Int,
      roughness: Int,
      height_map: HeightMap
  ): HeightMap = {

    val half = chunk_size / 2
    val end = height_map.length

    for (y <- 0 until end by half) {
      for (x <- (y + half) % chunk_size until end by chunk_size) {
        var count = 0
        var total = 0f

        if (0 <= x - half) {
          total += height_map(y)(x - half)
          count += 1
        }

        if (x + half < end) {
          total += height_map(y)(x + half)
          count += 1
        }

        if (0 <= y - half) {
          total += height_map(y - half)(x)
          count += 1
        }

        if (y + half < end) {
          total += height_map(y + half)(x)
          count += 1
        }

        val noise = -roughness + Random.nextInt((roughness * 2) + 1)
        height_map(y)(x) = (total / count) + noise
      }
    }

    height_map
  }

  def run(n: Int, max_height: Int = 8): HeightMap = {
    val size = (pow(2f, n) + 1).toInt
    var height_map = Array.ofDim[Float](size, size)

    height_map(0)(0) = Random.nextInt(max_height.toInt).toFloat
    height_map(0)(size - 1) = Random.nextInt(max_height.toInt).toFloat
    height_map(size - 1)(0) = Random.nextInt(max_height.toInt).toFloat
    height_map(size - 1)(size - 1) = Random.nextInt(max_height.toInt).toFloat

    var chunk_size = size - 1
    var roughness = n

    while (chunk_size > 1) {
      height_map = square_step(chunk_size, roughness, height_map)
      height_map = diamond_step(chunk_size, roughness, height_map)
      chunk_size /= 2;
      roughness /= 2;
    }

    height_map
  }
}
