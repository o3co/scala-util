package o3co.search

sealed trait Size {
  def value: Option[Long]

  def map[T](f: Long => T): Option[T] = 
    value.map(x => f(x))

  def flatMap[T](f: Long => Option[T]): Option[T] = 
    value.flatMap(x => f(x))

  def getOrElse(default: Long): Long = 
    value.getOrElse(default)
}

object Size {

  trait ImplicitConversions extends Any {
    import scala.language.implicitConversions
  
    // Null to Size conversion
    def nullToSize(nil: Null): Size = All 
    // Number to Size conversions
    implicit def intToSize(value: Int): Size = Limit(value.toLong) 
    implicit def longToSize(value: Long): Size = Limit(value) 
    implicit def floatToSize(value: Float): Size = Limit(value.toLong) 
    implicit def doubleToSize(value: Double): Size = Limit(value.toLong) 
  
    // Limit to Number convresions
    implicit def limitToInt(offset: Limit): Int = offset.raw.toInt
    implicit def limitToLong(offset: Limit): Long = offset.raw 
    implicit def limitToFloat(offset: Limit): Float = offset.raw.toFloat
    implicit def limitToDouble(offset: Limit): Double = offset.raw.toDouble
  
    // Size to Numberic Option convresions
    implicit def sizeToInt(offset: Size): Option[Int] = offset.map(_.toInt)
    implicit def sizeToLong(offset: Size): Option[Long] = offset.value
    implicit def sizeToFloat(offset: Size): Option[Float] = offset.map(_.toFloat)
    implicit def sizeToDouble(offset: Size): Option[Double] = offset.map(_.toDouble)
  }

  object ImplicitConversions extends ImplicitConversions
}

case class Limit(raw: Long) extends Size {
  val value: Option[Long] = Option(raw)
}

case object All extends Size {
  val value: Option[Long] = None
}
