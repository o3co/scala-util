package jp.o3co.search

import condition.ConditionParser
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

object Condition {
  def apply[T](notation: String)(implicit parser: ConditionParser): Condition[T] = parser.parse(notation)

  def as[A, B](condition: Condition[A])(implicit f: A => B): Condition[B] = condition match {
    case x: ScalarConditionLike[A] => x.asCondition(f)
  }
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

//case class FieldCondition[T](fieldname: String, condition: Condition[T]) {
//}
//
//case class FieldConditions(fields: Seq[FieldCondition[_]] = Seq()) {
//
//  def getFieldNames = fields.map(f => f.fieldname)
//
//  def toMap = fields.map(f => (f.fieldname, f.condition)).toMap
//
//  def get(fieldname: String): Option[FieldCondition[_]] = fields.find(_.fieldname == fieldname)
//
//  def collect(f: PartialFunction[FieldCondition[_], FieldCondition[_]]) = copy(fields = fields.collect(f))
//}
//
