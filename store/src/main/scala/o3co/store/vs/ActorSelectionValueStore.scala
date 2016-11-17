package o3co.store
package vs 

import akka.actor.ActorSelection
import akka.pattern.ask
import akka.util.Timeout
import o3co.store
import scala.concurrent.ExecutionContext


object ActorSelectionValueStore {

  trait Read[V] extends ActorSelectionStore.Access with ValueStore.Read[V] {
    this: ActorSelectionStore =>

    val protocol: StoreProtocol with ValueStoreProtocol.Read[V]
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

  trait Write[V] extends ActorSelectionStore.Access with ValueStore.Write[V] {
    this: ActorSelectionStore =>

    val protocol: StoreProtocol with ValueStoreProtocol.Write[V]
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

  type Full[V] = Read[V] with Write[V]
}

trait ActorSelectionValueStore[V] extends ActorSelectionStore 
  with ValueStore[V] 
  with ActorSelectionValueStore.Read[V]
  with ActorSelectionValueStore.Write[V]
{

  val protocol: StoreProtocol with ValueStoreProtocol.Full[V]
  
}
