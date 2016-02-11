package jp.o3co.aws
package cloudsearch

import java.util.Date
import scala.collection.immutable

/**
 *
 */
object FieldValue {

  /**
   * Convert FieldValue to Json format string
   */
  def valueToJson(value: Any): String = value match {
    case v: String => s""""$v""""
    case s: Seq[_] => s.map(v => valueToJson(v)).mkString("[", ",", "]")
    case _  => value.toString
  }
}

/**
 *
 */
sealed trait FieldValue[T] {
  def value: T

  def toJson: String = FieldValue.valueToJson(value)
}
sealed trait ArrayFieldValue[T] extends FieldValue[Seq[T]] 

case class DateFieldValue(value: Date) extends FieldValue[Date]
case class DateArrayFieldValue(value: Seq[Date]) extends ArrayFieldValue[Date]
case class DoubleFieldValue(value: Double) extends FieldValue[Double]
case class DoubleArrayFieldValue(value: Seq[Double]) extends ArrayFieldValue[Double]
case class IntFieldValue(value: Long) extends FieldValue[Long]
case class IntArrayFieldValue(value: Seq[Long]) extends ArrayFieldValue[Long]
case class LatLonFieldValue(value: (Double, Double)) extends FieldValue[(Double, Double)]
case class LiteralFieldValue(value: String) extends FieldValue[String]
case class LiteralArrayFieldValue(value: Seq[String]) extends ArrayFieldValue[String]
case class TextFieldValue(value: String) extends FieldValue[String]
case class TextArrayFieldValue(value: Seq[String]) extends ArrayFieldValue[String]

trait FieldValueConversions {
  import scala.language.implicitConversions

  implicit def stringToFieldValue(value: String)   = TextFieldValue(value)
  implicit def intToFieldValue(value: Int)         = IntFieldValue(value.toLong)
  implicit def shortToFieldValue(value: Short)     = IntFieldValue(value.toLong)
  implicit def longToFieldValue(value: Long)       = IntFieldValue(value)
  implicit def booleanToFieldValue(value: Boolean) = IntFieldValue(value match {
    case true  => 1L
    case false => 0L
  })
  implicit def doubleToFieldValue(value: Double) = DoubleFieldValue(value)
  implicit def floatToFieldValue(value: Float)   = DoubleFieldValue(value.toDouble)
  implicit def dateToFieldValue(value: Date)     = DateFieldValue(value)
  implicit def doublePairToFieldValue(value: (Double, Double)) = LatLonFieldValue(value)

  implicit def stringSeqToFieldValue(value: Seq[String]): TextArrayFieldValue  = TextArrayFieldValue(value)
  implicit def intSeqToFieldValue(value: Seq[Int])        = IntArrayFieldValue(value.map(_.toLong))
  implicit def longSeqToFieldValue(value: Seq[Long])      = IntArrayFieldValue(value)
  implicit def shortSeqToFieldValue(value: Seq[Short])    = IntArrayFieldValue(value.map(_.toLong))
}
object FieldValueConversions extends FieldValueConversions

class FieldValues(val fields: Map[String, FieldValue[_]] = Map.empty) extends immutable.Map[String, FieldValue[_]] with immutable.MapLike[String, FieldValue[_], FieldValues] {

  def get(key: String): Option[FieldValue[_]] = fields.get(key)

  def iterator: Iterator[(String, FieldValue[_])] = fields.iterator

  def +[B1 >: FieldValue[_]](kv: (String, B1)): Map[String, B1] = fields + kv

  def - (key: String): FieldValues = new FieldValues(fields - key)

  override def empty: FieldValues = new FieldValues(Map.empty)

  def toJson: String = fields.map(kv => s""""${kv._1}":${kv._2.toJson}""").mkString("{", ",", "}")
}

object FieldValues extends FieldValueConversions {
  def apply() = empty

  def apply(kvs: (String, FieldValue[_])*) = new FieldValues(kvs.toMap)

  //def apply[FieldValue <: FieldValue](kvs: Map[String, FieldValue]) = new FieldValues(kvs)

  def empty = new FieldValues()
}
