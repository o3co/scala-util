package o3co.util

import java.nio.ByteBuffer
import java.util.UUID
import org.apache.commons.codec.binary.Base64

/**
 * Short UUID with Base64
 */
case class Base64UUID(value: String) {
  if(!Base64.isBase64(value)) {
    throw new IllegalArgumentException("Invalid Base64 value.")
  }

  lazy val uuid: UUID = {
    val bb = ByteBuffer.wrap(Base64.decodeBase64(value))
    new UUID(bb.getLong(), bb.getLong());
  }

  override def toString: String = value
}

/**
 */
object Base64UUID {

  /**
   */
  def apply(uuid: UUID): Base64UUID = {
    val buffer = ByteBuffer.allocate(java.lang.Long.BYTES * 2)
    buffer.putLong(uuid.getMostSignificantBits)
    buffer.putLong(uuid.getLeastSignificantBits)

    val base64 = Base64.encodeBase64URLSafeString(buffer.array())

    Base64UUID(base64)
  }

  implicit class UUIDAsBase64(val uuid: UUID) {
    def asBase64: Base64UUID = Base64UUID(uuid)
  }
}
