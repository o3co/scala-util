package o3co.conversion

/**
 */
trait Conversion[A, B] extends Function[A, B] {
  def apply(from: A): B
}

/**
 * Reversible conversion for both A => B and B => A.
 */
trait ReversibleConversion[A, B] extends Conversion[A, B] {

  def reverse: Conversion[B, A]
}

/**
 */
object Conversion {

  type Forward[A, B]  = A => B
  type Backward[A, B] = B => A
  
  /**
   */
  def apply[A, B](f: Forward[A, B]): Conversion[A, B] = new Conversion[A, B] {
    def apply(from: A) = f(from)
  }

  /**
   */
  def apply[A, B](f: Forward[A, B], g: Backward[A, B]): ReversibleConversion[A, B] = 
    ReversibleConversion[A, B](f, g)
}

object ReversibleConversion {
  import scala.language.existentials

  /**
   *
   */
  def apply[A, B](f: Conversion.Forward[A, B], g: Conversion.Backward[A, B]) = 
    new ReversibleConversion[A, B] {
      forwardConversion => 

      def apply(from: A): B = f(from)

      def reverse = new ReversibleConversion[B, A] {
        reverseConversion => 
        
        def apply(from: B): A = g(from)

        val reverse = forwardConversion 
      }
    }
}
