package jp.o3co.generator
package impl

import scala.concurrent.ExecutionContext

/**
 *
 */
trait AsyncGeneratorProxy[T] extends AsyncGenerator[T] {

  def underlying: AsyncGenerator[T]

  def generateAsync() = underlying.generateAsync
}

object AsyncGeneratorProxy {
  def apply[T](generator: AsyncGenerator[T]) = new AsyncGeneratorProxy[T] {
    override val underlying = generator
  }

  def apply[T](generator: Generator[T])(implicit ec: ExecutionContext) = new GeneratorProxy[T] with AsAsyncGenerator[T] {
    override val underlying = generator
    override val executionContext = ec
  }
}
