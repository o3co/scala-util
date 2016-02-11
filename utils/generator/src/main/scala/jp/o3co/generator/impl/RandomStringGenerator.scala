package jp.o3co.generator
package impl

import org.apache.commons.lang3.RandomStringUtils

/*
 *
 */
trait RandomStringGenerator extends Generator[String] {
  import RandomStringGenerator._

  def length: Int

  def valueType: ValueType

  def customChars: String = ""

  def generate(): String = valueType match {
    case ValueTypes.AlphaNumeric => RandomStringUtils.randomAlphanumeric(length)
    case ValueTypes.Alphabetic   => RandomStringUtils.randomAlphabetic(length)
    case ValueTypes.Numeric      => RandomStringUtils.randomNumeric(length)
    case ValueTypes.Ascii        => RandomStringUtils.randomAscii(length)
    case ValueTypes.Custom       => RandomStringUtils.random(length, customChars)
    case ValueTypes.Any          => RandomStringUtils.random(length)
  }
}

object RandomStringGenerator extends BaseImplicitConversions {
  def apply(len: Int): RandomStringGenerator = new RandomStringGenerator {
    override val length = len
    override val valueType = ValueTypes.AlphaNumeric
  }

  def apply(len: Int, vt: ValueType): RandomStringGenerator = new RandomStringGenerator {
    override val length = len
    override val valueType = vt
  }

  def apply(len: Int, chars: String): RandomStringGenerator = new RandomStringGenerator {
    override val length = len
    override val valueType = ValueTypes.Custom
    override val customChars = chars
  }

  sealed trait ValueType

  object ValueTypes {
    case object Alphabetic extends ValueType
    case object AlphaNumeric extends ValueType
    case object Numeric extends ValueType
    case object Ascii extends ValueType
    case object Custom extends ValueType
    case object Any extends ValueType
  }
}
