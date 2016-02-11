package jp.o3co.matcher

import shapeless._
import HList._

object Matcher extends ImplicitConversions {
  trait Matching[+T]

  /**
   *
   */
  case class Matched[T](matched: HList) extends Matching[T]

  object Matched {
    def apply[T](matched: T): Matched[T] = Matched(matched :: HNil)
  }

  /**
   *
   */
  case class Unmatched(causes: String *) extends Matching[Nothing] {
    override def equals(other: Any) = other match {
      case _: Unmatched => true
      case _ => false
    }
  }

  /**
   * Create functionaly matcher
   */
  def apply[T](f: T => Matching[T]) = new Matcher[T] {
    def matches(value: T): Matching = f(value)
  }
}

trait Matcher[T] extends (T => Matcher.Matching[T]) {
  type Matching  = Matcher.Matching[T]

  type Matched   = Matcher.Matched[T]
  val  Matched   = Matcher.Matched

  type Unmatched = Matcher.Unmatched
  val  Unmatched = Matcher.Unmatched

  final def apply(value: T): Matcher.Matching[T] = matches(value)

  def matches(value: T): Matching

  def by[B](f: B => T): Matcher[B] = Matcher(value => matches(f(value)) match {
    case _: Matched   => Matched(value)
    case _: Unmatched => Unmatched()
  })
}

trait ImplicitConversions {
  import Matcher._
  import scala.language.implicitConversions

  /**
   * Implicit Conversion from Matching to Boolean 
   */
  implicit def matchingToBoolean[T](matching: Matching[T]): Boolean = matching match {
    case x: Matched[T]  => true
    case x: Unmatched   => false
  }
}
object ImplicitConversions extends ImplicitConversions
