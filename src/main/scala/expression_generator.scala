package qlib

import processing.core._
import PConstants._
import PApplet._

import scala.annotation.tailrec

import scala.util.Random

import qlib._

sealed trait Expr

case class Num(f: Float) extends Expr
case class X() extends Expr
case class Y() extends Expr

case class Cos(e: Expr) extends Expr
case class Sin(e: Expr) extends Expr
case class Tan(e: Expr) extends Expr
case class Pow2(e: Expr) extends Expr
case class Pow3(e: Expr) extends Expr

case class Add(e1: Expr, e2: Expr) extends Expr
case class Sub(e1: Expr, e2: Expr) extends Expr
case class Mul(e1: Expr, e2: Expr) extends Expr
case class Div(e1: Expr, e2: Expr) extends Expr

class ExpressionGenerator(val sketch: PApplet) {
  private val expression = generate(None)

  private def _eval(e: Expr, x:Float, y: Float): Float = e match {
    case Num(f) => f
    case X() => x
    case Y() => y

    case Cos(e) => cos(_eval(e, x, y))
    case Sin(e) => sin(_eval(e, x, y))
    case Tan(e) => tan(_eval(e, x, y))
    case Pow2(e) => pow(_eval(e, x, y), 2f)
    case Pow3(e) => pow(_eval(e, x, y), 3f)

    case Add(e1, e2) => _eval(e1, x, y) + _eval(e2, x, y)
    case Sub(e1, e2) => _eval(e1, x, y) - _eval(e2, x, y)
    case Mul(e1, e2) => _eval(e1, x, y) * _eval(e2, x, y)
    case Div(e1, e2) => _eval(e1, x, y) / _eval(e2, x, y)
  }

  def eval(x: Float, y: Float): Float = _eval(expression, x, y)

  private def random_select(probability_map: Map[Expr, Int]): Expr = {
   var items = probability_map.flatMap(x => Seq.fill(x._2)(x._1))
   Random.shuffle(items).head
  }

  def generate(e: Option[Expr]): Expr = e match {
    case None => {
      val seed_map = Map[Expr, Int](
        X() -> 3,
        Y() -> 3,
        Num(Random.nextInt(10).toFloat) -> 2
      )

      val e = Some(random_select(seed_map))
      generate(e)
    }

    case Some(e) => {
      // 50:50 chance of quiting, this helps mitigate the generation of complex
      // expressions, which usually result in noise
      val quit = Random.nextInt(2)
      if (quit > 0) {
        return e
      }

      // Give a 3 in 4 chance that we use an operator
      val use_operator = Random.nextInt(4)
      if (use_operator < 3) {
        val e2 = generate(None)

        val op_map = Map[Expr, Int](
          Add(e, e2) -> 2,
          Sub(e, e2) -> 2,
          Mul(e, e2) -> 1,
          Div(e, e2) -> 1,
        )

        val new_e = Some(random_select(op_map))
        return generate(new_e)
      }

      val func_map = Map[Expr, Int](
        Pow3(e) -> 3,
        Pow2(e) -> 2,
        Cos(e) -> 2,
        Sin(e) -> 2,
        Tan(e) -> 2
      )

      val new_e = Some(random_select(func_map))

      generate(new_e)
    }
  }

  private def expression_to_string(e: Expr): String = e match {
    case Num(f) => f.toString()
    case X() => "x"
    case Y() => "y"

    case Cos(e) => "cos(" + expression_to_string(e) + ")"
    case Sin(e) => "sin(" + expression_to_string(e) + ")"
    case Tan(e) => "tan(" + expression_to_string(e) + ")"
    case Pow2(e) => "pow2(" + expression_to_string(e) + ")"
    case Pow3(e) => "pow3(" + expression_to_string(e) + ")"

    case Add(e1, e2) => expression_to_string(e1) + " + " + expression_to_string(e2)
    case Sub(e1, e2) => expression_to_string(e1) + " - " + expression_to_string(e2)
    case Mul(e1, e2) => expression_to_string(e1) + " * " + expression_to_string(e2)
    case Div(e1, e2) => expression_to_string(e1) + " / " + expression_to_string(e2)
  }

  override def toString(): String = expression_to_string(expression)
}
