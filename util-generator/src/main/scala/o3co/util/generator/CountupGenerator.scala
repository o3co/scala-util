package o3co.util.generator

import o3co.util.counter.impl.CounterImpl
import scala.math.Numeric

/**
 * CountupGenerator for numeric type
 */
class CountupGenerator[T: Numeric](override val validation: T => Boolean, override val numOfTries: Int) extends Generator[T] with CounterImpl[T] {

  override def numeric = implicitly[Numeric[T]] 

  /**
   * {@inheritDoc}
   */
  override protected def doGenerate: T = incr
}

object CountupGenerator {

  def apply[T: Numeric](): CountupGenerator[T] = {
    val numeric = implicitly[Numeric[T]]
    apply({any => true}, 1)
  }

  def apply[T: Numeric](f: T => Boolean, tries: Int = 1): CountupGenerator[T] = {
    val numeric = implicitly[Numeric[T]]
    new CountupGenerator(f, tries)
  }
}

