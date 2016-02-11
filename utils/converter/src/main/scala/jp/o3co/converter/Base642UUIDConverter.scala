package jp.o3co.converter

import java.util.UUID
import java.nio.ByteBuffer
import org.apache.commons.codec.binary.Base64

class Base642UUIDConverter extends Converter[String, UUID] {

  def convert = { source => 
    val bytes = ByteBuffer.wrap(Base64.decodeBase64(source))
    new UUID(bytes.getLong(), bytes.getLong())
  }
}
