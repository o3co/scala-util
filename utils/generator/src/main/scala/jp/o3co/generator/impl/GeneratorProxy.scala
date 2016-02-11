package jp.o3co.generator
package impl

/**
 *
 */
trait GeneratorProxy[T] extends Generator[T] {

  def underlying: Generator[T]

  def generate() = underlying.generate()
}

object GeneratorProxy {
  def apply[T](generator: Generator[T]) = new GeneratorProxy[T] {
    override val underlying = generator
  }
}

