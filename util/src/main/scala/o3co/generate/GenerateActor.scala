package o3co.generate

import akka.actor.Actor
import akka.pattern.pipe
import scala.util.Try
import scala.util.{Success, Failure}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

trait ReceiveGenerate[T] {
  this: Actor with Generate[T] => 

  val protocol: GenerateProtocol[T]
  import protocol._

  implicit def executionContext: ExecutionContext

  def receiveGenerate: Receive = {
    case Generate => 
      sender() ! (generate.map(GenerateSuccess(_)).recover {
        case e: Throwable => GenerateFailure(e)
      })
  }
}

trait ReceiveAsyncGenerate[T] {
  this: Actor with AsyncGenerate[T] => 

  val protocol: GenerateProtocol[T]
  import protocol._

  implicit def executionContext: ExecutionContext

  def receiveGenerate: Receive = {
    case Generate => 
      generateAsync
        .map(GenerateSuccess(_))
        .recover {
          case e: Throwable => GenerateFailure(e)
        }
        .pipeTo(sender())
  }
}

trait GenerateActor[T] extends Actor with ReceiveGenerate[T] {
  this: Generate[T] =>  
}

trait AsyncGenerateActor[T] extends Actor with ReceiveAsyncGenerate[T] {
  this: AsyncGenerate[T] =>  
}
