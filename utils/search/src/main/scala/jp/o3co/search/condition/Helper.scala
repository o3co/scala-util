package jp.o3co.search
package condition

import java.lang.Comparable
/**
 * Helper to create Conditions
 */
trait Helper {

  def eq[T](value: T): Equals[T]    = Equals[T](value)
  def ne[T](value: T)               = NotEquals[T](value)
  def lt[T](value: T)               = LessThan[T](value)
  def le[T](value: T)               = LessThanOrEquals[T](value)
  def gt[T](value: T)               = GreaterThan[T](value)
  def ge[T](value: T)               = GreaterThanOrEquals[T](value)
  def range[T <: Comparable[T]](left: Option[T], right: Option[T], incMin: Boolean = true, incMax: Boolean = true)   = Range[T](left, right, incMin, incMax)
  def inSet[T](values: Set[T]) = InSet[T](values)
  def inSet[T](values: T *) = InSet[T](values.toSet)

  /**
   * Create OR with conditionscondition 
   *
   * @param conds Internal conditions
   */
  def or[T](conds: Condition[T] *)  = Or[T](conds: _*)

  /**
   * Create AND with conditions
   * {{{
   *   // And(LessThan(3), GreaterThan(1))
   *   and(lt(3), gt(1))
   * }}}
   * @param conds Internal conditions 
   */
  def and[T](conds: Condition[T] *) = And[T](conds: _*)

  /**
   * 
   * @param value Value for traversal contains
   */
  def contains[T](value: T)       = Contains[T](value)

  /**
   * 
   * @param values Values for traversal contains
   */
  def containsAll[T](values: T *)    = Contains.all[T](values: _*)

  /**
   * 
   * @param values Values for traversal contains
   */
  def containsEither[T](values: T *) = Contains.either[T](values: _*)
}

object Helper extends Helper
