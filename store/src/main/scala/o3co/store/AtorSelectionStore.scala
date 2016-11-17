package o3co.store

import akka.actor.ActorSelection
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.ExecutionContext

object ActorSelectionStore {
  
  trait Access extends Store.Access {
    this: Store =>
    
    val protocol: StoreProtocol
  }
}

/**
 */
trait ActorSelectionStore extends Store with ActorSelectionStore.Access { 

  def endpoint: ActorSelection

  implicit def timeout: Timeout

  implicit def executionContext: ExecutionContext

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
