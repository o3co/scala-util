package o3co.generate

import scala.util.Try
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.Await

trait Generate[T] extends Function0[Try[T]] {

  /**
   */
  def apply() = generate

  /**
   */
  def generate: Try[T]
}

object Generate {
  
  /**
   */
  def apply[T](underlying: AsyncGenerate[T])(implicit timeout: FiniteDuration) = 
    new Generate[T] {
      def generate = Try {
        Await.result(underlying.generateAsync, timeout)
      }
    }
}
