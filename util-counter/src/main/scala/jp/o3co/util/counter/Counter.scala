package jp.o3co.util.counter

import scala.math.Numeric

trait Counter[T] {
  /**
   * Numeric for type T
   */
  def numeric: Numeric[T]

  /**
   * Count value
   */
  protected var _count: T  = numeric.zero 

  protected def count_=(num: T) = {
    _count = num
  }

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
  def rest(): Unit = rest(None)

  /**
   *
   */
  def rest(num: T): Unit = rest(Option(num))

  /**
   *
   */
  def rest(num: Option[T]): Unit
}

