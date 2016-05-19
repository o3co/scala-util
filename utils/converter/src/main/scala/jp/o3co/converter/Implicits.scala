package jp.o3co.converter

import java.util.UUID

object Implicits {

  implicit class UUID2ByteArrayConverter(val uuid: UUID) extends AnyVal {
    
    def toBytes: Array[Byte] = jp.o3co.converter.DefaultConverters.UUID2ByteArrayConverter.convert(uuid)
  }

  implicit class ByteArray2UUIDConverter(val bytes: Array[Byte]) extends AnyVal {
    def toUUID: UUID = jp.o3co.converter.DefaultConverters.ByteArray2UUIDConverter.convert(bytes)
  }
}
