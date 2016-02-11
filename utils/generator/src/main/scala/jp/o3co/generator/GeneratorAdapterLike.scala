package jp.o3co.generator

import akka.actor.ActorSelection
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.ExecutionContext

trait GeneratorAdapterLike[T] extends AsyncGenerator[T] {

  val protocol: GeneratorProtocolLike[T]

  import protocol._

  def endpoint: ActorSelection

  implicit def timeout: Timeout

  implicit def executionContext: ExecutionContext

  /**
   *
   */
  def generateAsync = {
    (endpoint ? Generate)
      .map {
        case GenerateComplete(value) => value
        case GenerateFailure(cause)  => throw cause
      }
  }
}
