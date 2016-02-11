package jp.o3co.generator

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.ExecutionContext

/**
 *
 */
trait GeneratorActorLike[T] {
  this: Actor with AsyncGenerator[T] => 

  val protocol: GeneratorProtocolLike[T]

  import protocol._

  implicit def executionContext: ExecutionContext

  def receiveGeneratorCommand: Receive = {
    case Generate => 
      generateAsync
        .map(value => GenerateComplete(value))
        .recover {
          case e: Throwable => GenerateFailure(e)
        }
        .pipeTo(sender)
  }
}
