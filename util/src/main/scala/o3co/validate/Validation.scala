package o3co.validate

/**
 */
trait Validation[T] extends Function1[T, Boolean] 

/**
 */
object Validation {

  /**
   *
   */
  def apply[T](f: T => Boolean) = 
    new Validation[T] {
      def apply(value: T): Boolean = f(value)
    }

  /**
   * Static result validate
   */
  def apply[T](always: Boolean) = 
    new Validation[T] {
      def apply(value: T) = always
    }

  /**
   *
   */
  def noValidation[T]: Validation[T] = apply(true)
}
