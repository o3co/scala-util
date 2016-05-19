package jp.o3co.converter

import java.util.UUID

/**
 *
 */
class UUID2ByteArrayConverter extends Converter[UUID, Array[Byte]] {
  import java.nio.ByteBuffer

  def convert = { uuid => 
    ByteBuffer.allocate(16)
      .putLong(uuid.getMostSignificantBits())
      .putLong(uuid.getLeastSignificantBits())
      .array()
  }
}

