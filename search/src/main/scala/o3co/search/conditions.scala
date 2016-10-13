package o3co.search
package conditions

import java.lang.Comparable

/**
 *
 */
trait ListCondition[A] extends Condition[Traversable[A]] {
  def asCondition[B](implicit f: A => B): Condition[Traversable[B]]
}

/**
 *
 */
case class Equals[T](value: T) extends Condition[T] with ScalarConditionLike[T] {
  def asCondition[B](implicit f: T => B) = 
    Equals(f(value))
}

/**
 *
 */
case class NotEquals[T](value: T) extends Condition[T] with ScalarConditionLike[T] {
  def asCondition[B](implicit f: T => B) = 
    NotEquals(f(value))
}

/**
 *
 */
case class LessThan[T](value: T) extends Condition[T] with ScalarConditionLike[T] {
  def asCondition[B](implicit f: T => B) = 
    LessThan(f(value))
}

/**
 *
 */
case class LessThanOrEquals[T](value: T) extends Condition[T] with ScalarConditionLike[T] {
  def asCondition[B](implicit f: T => B) = 
    LessThanOrEquals(f(value))
}

/**
 *
 */
case class GreaterThan[T](value: T) extends Condition[T] with ScalarConditionLike[T] {
  def asCondition[B](implicit f: T => B) = 
    GreaterThan(f(value))
}

/**
 *
 */
case class GreaterThanOrEquals[T](value: T) extends Condition[T] with ScalarConditionLike[T] {
  def asCondition[B](implicit f: T => B) = 
    GreaterThanOrEquals(f(value))
}

/**
 *
 */
//case class Range[T <% Comparable[T]](min: Option[T], max: Option[T], includeMin: Boolean = true, includeMax: Boolean = false) extends Condition[T] {
case class Range[T](min: Option[T], max: Option[T], includeMin: Boolean = true, includeMax: Boolean = false) extends Condition[T] with ScalarConditionLike[T] {

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

  def asCondition[B](implicit f: T => B) =
    Range(min.map(f(_)), max.map(f(_)), includeMin, includeMax)
}

/**
 * InSet is a shortcut operation of "x == a or x == b or x == c or ...."
 */
case class InSet[T](values: Set[T]) extends Condition[T] with ScalarConditionLike[T] {

  /**
   * Convert to OR operation.
   * InSet case search not support IN operator
   */
  def toOr: Or[T] = Or(values.toSeq.map(value => Equals(value)): _*)

  def asCondition[B](implicit f: T => B) =
    InSet(values.map(f(_)))
}

/**
 *
 */
case class And[T](conds: Condition[T] *) extends Condition[T] with ScalarConditionLike[T] {
  def asCondition[B](implicit f: T => B) = 
    //And(conds.map(_.asCondition[B]): _*)
    throw new Exception("not impl")
}

/**
 *
 */
case class Or[T](conds: Condition[T] *) extends Condition[T] with ScalarConditionLike[T] {
  def asCondition[B](implicit f: T => B) = 
    throw new Exception("not impl")
    //Or(conds.map(_.asCondition[B]): _*)
}

/**
 *
 */
case class Contains[A](value: A) extends ListCondition[A] {
  def asCondition[B](implicit f: A => B) = 
    Contains(f(value))
}

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

trait TextCondition[T] extends Condition[T]
case class Prefix[T](value: String) extends TextCondition[T] {
}

case class Suffix[T](value: String) extends TextCondition[T]{
}
