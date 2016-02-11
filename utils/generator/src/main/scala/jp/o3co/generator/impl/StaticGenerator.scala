package jp.o3co.generator
package impl

/**
 * Dummy generator which always return static value
 */
class StaticGenerator[T](value: T) extends Generator[T] {
  /**
   *
   */
  def generate() = value
}

object StaticGenerator {
  def apply[T](value: T) = new StaticGenerator(value)
}
