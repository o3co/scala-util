package jp.o3co.converter

import java.util.UUID
import java.nio.ByteBuffer
import org.apache.commons.codec.binary.Base64

class UUID2Base64Converter(urlSafe: Boolean = true) extends Converter[UUID, String] {
  protected def uuidToBytes(uuid: UUID): Array[Byte] = {
    ByteBuffer.allocate(16)
      .putLong(uuid.getMostSignificantBits())
      .putLong(uuid.getLeastSignificantBits())
      .array()
  }

  def convert = { uuid => 
    urlSafe match {
      case true => Base64.encodeBase64URLSafeString(uuidToBytes(uuid))
      case false => Base64.encodeBase64String(uuidToBytes(uuid))
    }
  }
}

