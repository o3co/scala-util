package o3co.search 

/**
 * 
 */
//case class Offset(value: Long) extends AnyVal
case class Offset(value: Long) 

object Offset {

  trait ImplicitConversions extends Any {
    import scala.language.implicitConversions
    // Number to Offset convresions
    implicit def intToOffset(value: Int): Offset = Offset(value.toLong) 
    implicit def longToOffset(value: Long): Offset = Offset(value) 
    implicit def floatToOffset(value: Float): Offset = Offset(value.toLong) 
    implicit def doubleToOffset(value: Double): Offset = Offset(value.toLong) 

    // Offset to Number convresions
    implicit def offsetToInt(offset: Offset): Int = offset.value.toInt
    implicit def offsetToLong(offset: Offset): Long = offset.value 
    implicit def offsetToFloat(offset: Offset): Float = offset.value.toFloat
    implicit def offsetToDouble(offset: Offset): Double = offset.value.toDouble
  }

  object ImplicitConversions extends ImplicitConversions 
}
