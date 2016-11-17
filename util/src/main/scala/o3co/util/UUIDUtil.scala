package o3co.util

import java.nio.ByteBuffer
import java.util.UUID
import org.apache.commons.codec.binary.Base64

trait UUIDUtil {
  
  def base64ToUUID(value: String): UUID = {
    if(!Base64.isBase64(value)) {
      throw new IllegalArgumentException(s"'$value' is not value Base64 string.")
    }
    val bb = ByteBuffer.wrap(Base64.decodeBase64(value))
    new UUID(bb.getLong(), bb.getLong());
  }

  def base64ToUUID(value: Base64Encoded): UUID = {
    val bb = ByteBuffer.wrap(value.decode)
    new UUID(bb.getLong(), bb.getLong())
  }

  def uuidToBase64(uuid: UUID): Base64Encoded = {
    val buffer = ByteBuffer.allocate(java.lang.Long.BYTES * 2)
    buffer.putLong(uuid.getMostSignificantBits)
    buffer.putLong(uuid.getLeastSignificantBits)

    Base64Encode.encodeURL(buffer.array())
  }
}

/**
 * {{{
 *   import o3co.util.UUIDUtil._
 *   uuid.toShortString
 *
 *   base64ToUUID(str)
 * }}}
 */
object UUIDUtil extends UUIDUtil {
  util =>

  implicit class UUIDExtension(uuid: UUID) {
    def toBase64String: String  = toBase64.toString

    def toBase64: Base64Encoded = util.uuidToBase64(uuid)
  }
}
