package o3co.count

import scala.math.Numeric

/**
 * Trait of counting.
 */
trait Count[T] {
  /**
   * Numeric for type T to calcurate the numbers
   */
  def numeric: Numeric[T]

  /**
   * Count value
   */
  protected var _count: T  = numeric.zero 

  /**
   * Setter of count 
   */
  protected def count_=(num: T) = {
    _count = num
  }
  /**
   * Getter of count
   */
  def count: T = _count

  /**
   * Short name of increment. Increment one 
   */
  def incr: T = increment()

  /**
   * Short name of increment. Increment num
   *
   * @param num Number of increment. 
   */
  def incr(num: T): T = increment(num)

  /**
   *
   */
  def increment(): T = increment(None)
  
  /**
   *
   */
  def increment(num: T): T = increment(Option(num))

  /**
   *
   */
  def increment(num: Option[T]): T

  /**
   *
   */
  def decr: T = decrement

  /**
   *
   */
  def decr(num: T): T = decrement(num)

  /**
   *
   */
  def decrement(): T = decrement(None)

  /**
   *
   */
  def decrement(num: T): T = decrement(Option(num))
  
  /**
   *
   */
  def decrement(num: Option[T]): T

  /**
   *
   */
  def +(num: T): T = increment(Option(num))

  /**
   *
   */
  def -(num: T): T = decrement(Option(num))

  /**
   *
   */
  def reset: Unit = reset(None)

  /**
   *
   */
  def reset(num: T): Unit = reset(Option(num))

  /**
   *
   */
  def reset(num: Option[T]): Unit
}

object Count {
  
  def apply[T: Numeric]() = 
    new Count[T] with CountLike[T] {
      val numeric = implicitly[Numeric[T]]      
    }

  def apply[T: Numeric](default: T) = new Count[T] with CountLike[T] {
    val numeric = implicitly[Numeric[T]]

    count = default
  }
}


trait CountLike[T] {
  this: Count[T] =>

  /**
   */
  def reset(num: Option[T]) = count = num.getOrElse(numeric.zero)

  /**
   */
  def increment(num: Option[T]): T = {
    count = numeric.plus(count, num.getOrElse(numeric.one))
    count
  }

  /**
   */
  def decrement(num: Option[T]): T = {
    count = numeric.minus(count, num.getOrElse(numeric.one))
    count
  }
}
