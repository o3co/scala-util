package o3co.store.vs 

import akka.actor.ActorSelection
import akka.pattern.ask
import akka.util.Timeout
import o3co.store
import scala.concurrent.ExecutionContext

trait ActorSelectionProxy[V] {
  def endpoint: ActorSelection

  implicit def timeout: Timeout

  implicit def executionContext: ExecutionContext

  val protocol: ValueStoreProtocol[V]
}

trait ActorSelectionReadAccessLike[V] extends ActorSelectionProxy[V] with ReadAccessLike[V] {
  this: ReadAccess[V] => 

  import protocol._

  /**
   *
   */
  def existsAsync(value: V) = {
    (endpoint ? ValueExists(value))
      .map {
        case ValueExistsSuccess(ret) => ret
        case ValueExistsFailure(cause) => throw cause 
      }
  }

  def valuesAsync() = {
    (endpoint ? GetValues)
      .map {
        case GetValuesSuccess(ret) => ret
        case GetValuesFailure(cause) => throw cause 
      }
  }
}

trait ActorSelectionWriteAccessLike[V] extends ActorSelectionProxy[V] with WriteAccessLike[V] {
  this: WriteAccess[V] =>

  import protocol._

  def putAsync(values: V *) = {
    (endpoint ? PutValues(values))
      .map {
        case PutValuesSuccess   => // Unit
        case PutValuesFailure(cause) => throw cause 
      }
  }

  def addAsync(values: V *) = {
    (endpoint ? AddValues(values))
      .map {
        case AddValuesSuccess => //
        case AddValuesFailure(cause) => throw cause 
      }
  }

  def deleteAsync(values: V *) = {
    (endpoint ? DeleteValues(values))
      .map {
        case DeleteValuesSuccess => //
        case DeleteValuesFailure(cause) => throw cause 
      }
  }
}

/*
 *
 */
trait ReadOnlyActorSelectionValueStore[V] extends ReadOnlyValueStore[V] with store.StoreLike with ActorSelectionReadAccessLike[V]

/*
 *
 */
trait ActorSelectionValueStore[V] extends ValueStore[V] with store.StoreLike with ActorSelectionReadAccessLike[V] with ActorSelectionWriteAccessLike[V] 

