package o3co.generate

import scala.concurrent.Future
import scala.util.{Success, Failure}

trait AsyncGenerate[T] extends Function0[Future[T]] {

  def apply() = generateAsync

  def generateAsync: Future[T]

}

object AsyncGenerate {
  /**
   */
  def apply[T](underlying: Generate[T]) = 
    new AsyncGenerate[T] {
      def generateAsync = 
        underlying.generate match {
          case Success(ret) => Future.successful(ret)
          case Failure(e)   => Future.failed(e)
        }
    }
}
