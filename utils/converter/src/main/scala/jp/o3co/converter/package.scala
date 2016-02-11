package jp.o3co.converter

import java.util.UUID
import java.nio.ByteBuffer
import org.apache.commons.codec.binary.Base64
import scala.concurrent.Future

trait AsyncConverter[A, B] extends Function[A, Future[B]] {

  def apply(source: A): Future[B] = convert(source)

  def convert: A => Future[B]
}

trait Converter[A, B] extends Function[A, B] {

  def apply(source: A): B = convert(source)

  def convert: A => B
}
