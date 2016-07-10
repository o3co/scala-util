package jp.o3co.tag
package filter

/**
 * Base filter with conversion from A to B.
 */
trait Filter[-T1,+R] extends ((T1) => R) {
  self =>

  def apply(value: T1): R = filter(value)

  def filter(value: T1): R

  override def compose[A](prev: (A) => T1): Filter[A, R] = new Filter[A, R] {
    def filter(value: A): R = self(prev(value))
  }

  override def andThen[A](next: R => A): Filter[T1, A] = new Filter[T1, A] {
    def filter(value: T1): A = next(self(value))
  }

  def ~>[A](next: Filter[R, A]): Filter[T1, A] = andThen(next)
}

trait ReversibleFilter[T1, R] {
  this: Filter[T1, R] =>

  def reverse: Filter[R, T1]
}


