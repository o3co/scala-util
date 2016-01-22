package jp.o3co.util.counter
package impl

/**
 *
 */
trait CounterImpl[T] extends Counter[T] {
  /**
   * {@inheritDoc}
   */
  def rest(num: Option[T]) = count = num.getOrElse(numeric.zero)

  /**
   * {@inheritDoc}
   */
  def increment(num: Option[T]): T = {
    count = numeric.plus(count, num.getOrElse(numeric.one))
    count
  }

  /**
   * {@inheritDoc}
   */
  def decrement(num: Option[T]): T = {
    count = numeric.minus(count, num.getOrElse(numeric.one))
    count
  }
}

/**
 * 
 */
object CounterImpl {
  /**
   * Create CounterImpl 
   */
  def apply[T: Numeric]() = new CounterImpl[T] {
    override def numeric = implicitly[Numeric[T]]
  }
}

