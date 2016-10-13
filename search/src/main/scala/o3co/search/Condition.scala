package o3co.search

import java.lang.Comparable
import o3co.search.conditions._

/**
 * Base trait of Condition.
 *  
 * With ToMatchers, Condition can be executable.
 * {{{
 *   // use Matcher.apply or Matcher.matches
 *   fieldCondition(value) match {
 *     case Unmatched(reasons) => ...
 *     case Matched(matched)   => ...
 *   }
 *   // Or Matcher.Matching can be convert to Boolean implicitly
 *   val isMatched: Boolean = fieldCondition.matches(value)
 * }}}
 *
 * Condition dose not support Complex condition which across the fields.
 * Complexed condition(LogicalExpression across fields such ((field1 == value1 AND field2 == value2) OR (field1 == value3))
 * is not "FIELD CONDITION" and its should be declared by QUERY instead.
 */
trait Condition[+T] {
  def shape = this.asInstanceOf[this.type]

  def as[B](implicit f: T => B) = Condition.as[T, B](this)
}

/**
 * {{{
 *   Condition.eq(value) // Equals(value)
 *   Condition.eq(1)     // Equals[Int](1)
 * }}}
 */
object Condition extends ConditionFactory {

  /**
   *
   */
  def parse[T](notation: String)(implicit parser: ConditionParser): Condition[T] = parser.parse(notation)

  /**
   *
   */
  def as[A, B](condition: Condition[A])(implicit f: A => B): Condition[B] = condition match {
    case x: ScalarConditionLike[A] => x.asCondition(f)
  }

  trait ImplicitConversions extends Any { 
    import scala.language.implicitConversions

    // Any direct value to Equals condition
    implicit def valueToEquals[T](value: T) = Equals[T](value)

    // Any Numeric to Int Condition
    implicit def longToIntCondition(condition: Condition[Long]): Condition[Int] = condition.as(_.toInt)
    implicit def floatToIntCondition(condition: Condition[Float]): Condition[Int] = condition.as(_.toInt)
    implicit def doubleToIntCondition(condition: Condition[Double]): Condition[Int] = condition.as(_.toInt)
    // Any Numeric to Long Condition
    implicit def intToLongCondition(condition: Condition[Int]): Condition[Long] = condition.as(_.toLong)
    implicit def floatToLongCondition(condition: Condition[Float]): Condition[Long] = condition.as(_.toLong)
    implicit def doubleToLongCondition(condition: Condition[Double]): Condition[Long] = condition.as(_.toLong)
    // Any Numeric to Float Condition
    implicit def intToFloatCondition(condition: Condition[Int]): Condition[Float] = condition.as(_.toFloat)
    implicit def longToFloatCondition(condition: Condition[Long]): Condition[Float] = condition.as(_.toFloat)
    implicit def doubleToFloatCondition(condition: Condition[Double]): Condition[Float] = condition.as(_.toFloat)
    // Any Numeric to Double Condition
    implicit def intToDoubleCondition(condition: Condition[Int]): Condition[Double] = condition.as(_.toDouble)
    implicit def longToDoubleCondition(condition: Condition[Long]): Condition[Double] = condition.as(_.toDouble)
    implicit def floatToDoubleCondition(condition: Condition[Float]): Condition[Double] = condition.as(_.toDouble)
  }

  object ImplicitConversions extends ImplicitConversions
}

trait ScalarConditionLike[+T] {
  this: Condition[T] =>

  /**
   * {{{
   *   Equals[Int](10).asCondition[Long]  // Equals[Long](10L)
   * }}}
   */
  def asCondition[B](implicit f: T => B): Condition[B]
}



/**
 * Condition Factory to create Conditions
 */
trait ConditionFactory {

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

  def prefix[T](value: String) = Prefix[T](value)

  def suffix[T](value: String) = Suffix[T](value)
}

