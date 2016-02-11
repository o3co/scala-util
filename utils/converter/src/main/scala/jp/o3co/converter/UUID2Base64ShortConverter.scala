package jp.o3co.converter

import java.util.UUID
import java.nio.ByteBuffer
import org.apache.commons.codec.binary.Base64


class UUID2Base64ShortConverter(defaultLength: Int = 6) extends UUID2Base64Converter(true) {

  var _length  = defaultLength

  def length = _length

  def length_= (len: Int) = {
    _length = len
  }

  override def convert = { uuid => 
    super.convert(uuid).take(length)
  }
}

object UUID2Base64ShortConverter {
  def apply(defaultLength: Int = 6) = new UUID2Base64ShortConverter(defaultLength)
}
