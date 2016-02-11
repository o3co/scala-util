package jp.o3co.aws
package cloudsearch
package query

import scala.collection.immutable

/**
 *
 */
class QueryOptions(val options: Map[String, QueryOption] = Map.empty) extends immutable.Map[String, QueryOption] with immutable.MapLike[String, QueryOption, QueryOptions] {

  def get(key: String): Option[QueryOption] = options.get(key)

  def iterator: Iterator[(String, QueryOption)] = options.iterator

  def +[B1 >: QueryOption](kv: (String, B1)): Map[String, B1] = options + kv

  def +(option: QueryOption): QueryOptions = new QueryOptions(options + (option.name -> option))

  def - (key: String): QueryOptions = new QueryOptions(options - key)

  override def empty: QueryOptions = new QueryOptions(Map.empty)

  override def toString: String = options.mapValues(v => v.toString).mkString("{", ",", "}")
}

/**
 *
 */
object QueryOptions {
  def apply() = empty

  def apply(kvs: (String, QueryOption)*) = new QueryOptions(kvs.toMap)

  //def apply[QueryOption <: QueryOption](kvs: Map[String, QueryOption]) = new QueryOptions(kvs)

  def empty = new QueryOptions()
}

/**
 *
 */
trait QueryOption {
  def name: String

  def optionValue: String

  def tupled: (String, String) = (name, optionValue)

  override def toString = s"$name=$optionValue"
}

/**
 *
 */
sealed abstract class AbstractQueryOption(val name: String) extends QueryOption

// DefaultOperator
sealed abstract class DefaultOperator(val value: String) extends AbstractQueryOption("defaultOperator") {
  def optionValue = s"'$value'"
}

object DefaultOperator {
  case object OR  extends DefaultOperator("OR")
  case object AND extends DefaultOperator("AND")
}

case class Fields(val fields: Field *) extends AbstractQueryOption("fields") {

  def fieldNames: Seq[String] = fields.map(field => field.name)

  def optionValue = fields
    .map(field => (field.name, field))
    .toMap // reduce by key
    .values
    .mkString("[", ", ", "]")
}

/**
 *
 */
object Fields extends ImplicitConversions {
  
}

/**
 *
 */
trait ImplicitConversions {
  import scala.language.implicitConversions

  implicit def stringToField(name: String): Field = FieldName(name)
}

/**
 *
 */
object ImplicitConversions extends ImplicitConversions

/**
 *
 */
trait Field {
  def name: String
}

/**
 *
 */
case class FieldName(name: String) extends Field {
  override def toString = s"'$name'"

  def ^(weight: Int): WeightedField = WeightedField(this, weight)
}

/**
 *
 */
case class WeightedField(fieldname: FieldName, weight: Int) extends Field {

  def name: String = fieldname.name

  override def toString = s"'$name^$weight'"
}
