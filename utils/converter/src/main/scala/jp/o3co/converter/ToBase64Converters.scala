package jp.o3co.converter

import java.util.UUID
import org.apache.commons.codec.binary.Base64

class UUID2Base64Converter(urlSafe: Boolean = true) extends Converter[UUID, String] {

  import jp.o3co.converter.Implicits.UUID2ByteArrayConverter

  def convert = { uuid => 
    urlSafe match {
      case true  => Base64.encodeBase64URLSafeString(uuid.toBytes)
      case false => Base64.encodeBase64String(uuid.toBytes)
    }
  }
}

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
