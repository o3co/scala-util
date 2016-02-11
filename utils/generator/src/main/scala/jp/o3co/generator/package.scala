package jp.o3co

package object generator {

  val DEFAULT_RETRIES = 3

  /**
   *
   */
  type Validator[T] = (T) => Boolean

  //trait Validator[T] extends Validation[T]
  
  /**
   *
   */
  object Validator {
    /**
     * Craete validator with Function T => Boolean
     */
    def apply[T](f: T => Boolean) = new Function1[T, Boolean] {
      def apply(value: T) = f(value)
    }
  
    /**
     * Factory method to create validator which always return true
     */
    //def noValidation[T] = new Validator[T] {
    //  def  apply(value: T): Boolean = true
    //}
    def noValidation[T] = new Function1[T, Boolean] {
      def apply(value: T) = true 
    }
  }
}


