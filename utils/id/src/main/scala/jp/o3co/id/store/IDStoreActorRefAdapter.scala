package jp.o3co.id.store

import akka.util.Timeout
import akka.actor.ActorRef
import akka.pattern.ask
import scala.concurrent.ExecutionContext

/**
 *
 */
trait IDStoreActorRefAdapterLike[ID] extends impl.IDStoreImpl[ID] {

  val protocol: IDStoreProtocolLike[ID]
  import protocol._

  implicit def timeout: Timeout

  implicit def executionContext: ExecutionContext

  def endpoint: ActorRef

  /**
   *
   */
  def existsAsync(id: ID) = {
    (endpoint ? IDExists(id))
      .map {
        case IDExistsSuccess(exists) => exists
        case IDExistsFailure(cause) => throw cause 
      }
  }

  /**
   *
   */
  def putAsync(id: ID) = {
    (endpoint ? PutID(id))
      .map {
        case PutIDSuccess() => (): Unit
        case PutIDFailure(cause) => throw cause 
      }
  }

  /**
   *
   */
  def deleteAsync(id: ID) = {
    (endpoint ? DeleteID(id))
      .map {
        case DeleteIDSuccess() => (): Unit 
        case DeleteIDFailure(cause) => throw cause 
      }
  }
}

trait IDStoreActorRefAdapter[ID] extends IDStore[ID] with IDStoreActorRefAdapterLike[ID] {
  val protocol: IDStoreProtocol[ID]
}


