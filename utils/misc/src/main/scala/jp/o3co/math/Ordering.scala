package jp.o3co.math

import scala.math.Ordering

/**
 * Collection of ordering to apply in priority
 */
case class PriorityOrdering[T](orderings: Ordering[T] *) extends Ordering[T] {
  import jp.o3co.iterator.NextOptionIterator

  /**
   *
   */
  def compare(x: T, y: T): Int = {
    orderings.iterator.map(_.compare(x, y)).dropWhile(_ == 0).nextOrElse(0)
  }
}
