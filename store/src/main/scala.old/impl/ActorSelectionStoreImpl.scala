package o3co.store

import akka.actor.ActorSelection
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.ExecutionContext

/**
 */
trait ActorSelectionStoreLike extends StoreLike {
  this: Store => 

  def endpoint: ActorSelection

  implicit def timeout: Timeout

  implicit def executionContext: ExecutionContext

  val protocol: StoreProtocol
  import protocol._
  
  /**
   */
  def countAsync() = {
    (endpoint ? Count)
      .map {
        case CountSuccess(count) => count
        case CountFailure(cause) => throw cause
      }
  }
}

trait ActorSelectionStore extends Store with ActorSelectionStoreLike
