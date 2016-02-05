package jp.o3co.util.generator

import org.apache.commons.lang3.RandomStringUtils

/*
 *
 */
trait RandomStringGenerator extends Generator[String] {
  import RandomStringGenerator._

  def length: Int

  def valueType: ValueType

  override def doGenerate: String = valueType match {
    case ValueTypes.AlphaNumeric => RandomStringUtils.randomAlphanumeric(length)
    case ValueTypes.Alphabetic   => RandomStringUtils.randomAlphabetic(length)
    case ValueTypes.Numeric      => RandomStringUtils.randomNumeric(length)
    case ValueTypes.Any          => RandomStringUtils.random(length)
  }
}

object RandomStringGenerator {
  def apply(len: Int) = new RandomStringGenerator {
    override val length = len
    override val valueType = ValueTypes.Any
  }

  def apply(len: Int, vt: ValueType) = new RandomStringGenerator {
    override val length = len
    override val valueType = vt
  }

  def apply(len: Int, f: String => Boolean, tries: Int = 1, vt: ValueType = ValueTypes.Any) = new RandomStringGenerator {
    override val length = len
    override lazy val validation = f
    override val numOfTries = tries
    override val valueType = vt
  }

  sealed trait ValueType

  object ValueTypes {
    case object Alphabetic extends ValueType
    case object AlphaNumeric extends ValueType
    case object Numeric extends ValueType
    case object Any extends ValueType
  }
}
