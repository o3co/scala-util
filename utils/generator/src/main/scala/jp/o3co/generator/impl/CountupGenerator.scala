package jp.o3co.generator
package impl

import jp.o3co.counter.impl.CounterImpl
import scala.math.Numeric

/**
 * CountupGenerator for numeric type
 */
trait CountupGenerator[T] extends Generator[T] with CounterImpl[T] {

  /**
   * {@inheritDoc}
   */
  def generate: T = incr
}

/**
 * Companion object of CountupGenerator
 */
object CountupGenerator extends BaseImplicitConversions {

  /**
   *
   */
  def apply[T: Numeric](): CountupGenerator[T] = new CountupGenerator[T] {
    override def numeric = implicitly[Numeric[T]] 
  }
}

