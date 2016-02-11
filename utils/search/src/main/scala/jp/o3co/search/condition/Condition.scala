package jp.o3co.search
package condition

import java.lang.Comparable

/**
 *
 */
trait ListCondition[A] extends Condition[Traversable[A]]

/**
 *
 */
case class Equals[T](value: T) extends Condition[T]

/**
 *
 */
case class NotEquals[T](value: T) extends Condition[T]

/**
 *
 */
case class LessThan[T](value: T) extends Condition[T] 

/**
 *
 */
case class LessThanOrEquals[T](value: T) extends Condition[T]

/**
 *
 */
case class GreaterThan[T](value: T) extends Condition[T]

/**
 *
 */
case class GreaterThanOrEquals[T](value: T) extends Condition[T]

/**
 *
 */
//case class Range[T <% Comparable[T]](min: Option[T], max: Option[T], includeMin: Boolean = true, includeMax: Boolean = false) extends Condition[T] {
case class Range[T](min: Option[T], max: Option[T], includeMin: Boolean = true, includeMax: Boolean = false) extends Condition[T] {

  if(min.isEmpty && max.isEmpty) throw new IllegalArgumentException("Range cannot be both min and max empty.")

  //// 
  //val (min: Option[T], max: Option[T]) = (left, right) match {
  //  case (Some(l), Some(r)) if (l.compareTo(r) > 0) => (right, left) 
  //  case _                                          => (left, right)
  //}

  def minCondition: Option[Condition[T]] = min.map { n => 
    if(includeMin) GreaterThanOrEquals(n)
    else GreaterThan(n)
  }

  def maxCondition: Option[Condition[T]] = max.map { x => 
    if(includeMax) LessThanOrEquals(x)
    else LessThan(x)
  }

  /**
   * Split the range condition to AND(GT/GE, LT/LE) or one of the condition.
   */
  def toComparisons: Condition[T] = (minCondition, maxCondition) match {
    case (Some(n), Some(x))   => And(n, x)
    case (Some(n), None)      => n
    case (None, Some(x))      => x
    case (None, None)         => throw new IllegalStateException("Cannot spliti to compatible conditions, cause range is illegal state.")
  }
}

/**
 * InSet is a shortcut operation of "x == a or x == b or x == c or ...."
 */
case class InSet[T](values: Set[T]) extends Condition[T] {

  /**
   * Convert to OR operation.
   * InSet case search not support IN operator
   */
  def toOr: Or[T] = Or(values.toSeq.map(value => Equals(value)): _*)
}

/**
 *
 */
case class And[T](conds: Condition[T] *) extends Condition[T] 

/**
 *
 */
case class Or[T](conds: Condition[T] *) extends Condition[T]

/**
 *
 */
case class Contains[A](value: A) extends ListCondition[A]

/**
 * Companion object of Contains
 */
object Contains {
  /**
   * Create And combination of contains
   *
   */
  def all[A](values: A*)    = And(values.map(value => Contains(value)).toSeq: _*)

  /**
   * Create OR combination of contains
   *
   * @param values Values of traversal contains
   */
  def either[A](values: A*) = Or(values.map(value => Contains(value)).toSeq: _*)
}
