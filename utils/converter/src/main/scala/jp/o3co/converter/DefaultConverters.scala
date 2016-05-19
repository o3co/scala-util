package jp.o3co.converter

trait DefaultConverters {
  val UUID2ByteArrayConverter = new UUID2ByteArrayConverter()

  val ByteArray2UUIDConverter = new ByteArray2UUIDConverter()
}

object DefaultConverters extends DefaultConverters
