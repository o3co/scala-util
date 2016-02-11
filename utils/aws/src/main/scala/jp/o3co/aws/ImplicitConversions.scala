package jp.o3co.aws

import scala.math.Numeric

/**
 *
 */
trait ImplicitConversions {
  import scala.language.implicitConversions
  
  implicit def sizeToLong(size: Size): Long = size.toLong

  implicit def sizeToInt(size: Size): Int = size.toInt

  implicit def numericToSize[T: Numeric](size: T): Size = Size(size.toLong)

  implicit def numberToSize(size: java.lang.Number): Size = Size(size.longValue: Long)

  implicit def sizeToJavaLong(size: Size): java.lang.Long = new java.lang.Long(size.toLong)

  implicit def endpointToString(endpoint: Endpoint): String = endpoint.path
}
object ImplicitConversions extends ImplicitConversions
