package jp.o3co.converter

import java.util.UUID

class ByteArray2UUIDConverter extends Converter[Array[Byte], UUID] {
  import java.nio.ByteBuffer

  def convert = { bytes => 
    val buffer = ByteBuffer.wrap(bytes)
    new UUID(buffer.getLong(), buffer.getLong())
  }
}

class Base642UUIDConverter extends Converter[String, UUID] {
  import org.apache.commons.codec.binary.Base64

  def convert = { source =>
    DefaultConverters.ByteArray2UUIDConverter.convert(Base64.decodeBase64(source))
  }
}
