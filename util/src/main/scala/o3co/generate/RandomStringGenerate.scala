package o3co.generate

import org.apache.commons.lang3.RandomStringUtils
import scala.util.Try

trait RandomStringGenerate extends Generate[String] {

  import RandomStringGenerate.ValueTypes

  def length: Int

  def valueType: RandomStringGenerate.ValueType

  def generate = Try {
    valueType match {
      case ValueTypes.AlphaNumeric  => RandomStringUtils.randomAlphanumeric(length)
      case ValueTypes.Alphabetic    => RandomStringUtils.randomAlphabetic(length)
      case ValueTypes.Numeric       => RandomStringUtils.randomNumeric(length)
      case ValueTypes.Ascii         => RandomStringUtils.randomAscii(length)
      case ValueTypes.Any           => RandomStringUtils.random(length)
      case ValueTypes.Custom(chars) => RandomStringUtils.random(length, chars)
    }
  }
}

object RandomStringGenerate {

  def apply(len: Int): RandomStringGenerate = new RandomStringGenerate {
    override val length = len
    override val valueType = ValueTypes.AlphaNumeric
  }

  def apply(len: Int, vt: ValueType): RandomStringGenerate = new RandomStringGenerate {
    override val length = len
    override val valueType = vt
  }

  def apply(len: Int, chars: String): RandomStringGenerate = new RandomStringGenerate {
    override val length = len
    override val valueType = ValueTypes.Custom(chars)
  }

  sealed trait ValueType

  @SerialVersionUID(1L)
  case object ValueTypes {
    @SerialVersionUID(1L) case object Alphabetic extends ValueType
    @SerialVersionUID(1L) case object AlphaNumeric extends ValueType
    @SerialVersionUID(1L) case object Numeric extends ValueType
    @SerialVersionUID(1L) case object Ascii extends ValueType
    @SerialVersionUID(1L) case object Any extends ValueType
    @SerialVersionUID(1L) case class Custom(chars: String) extends ValueType
  }
}
