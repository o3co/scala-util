package o3co.store
package kvs 

import akka.actor.ActorSelection
import akka.pattern.ask
import akka.util.Timeout
import o3co.store
import scala.concurrent.ExecutionContext


object ActorSelectionKeyValueStore {

  trait Read[K, V] extends ActorSelectionStore.Access with KeyValueStore.Read[K, V] {
    this: ActorSelectionStore =>

    val protocol: StoreProtocol with KeyValueStoreProtocol.Read[K, V]
    import protocol._

    def existsAsync(key: K) = {
      (endpoint ? ExistsByKey(key))
        .map {
          case ExistsByKeySuccess(exists) => exists
          case ExistsByKeyFailure(cause)  => throw cause 
        }
    }

    def keysAsync = {
      (endpoint ? GetKeys)
        .map {
          case GetKeysSuccess(keys) => keys
          case GetKeysFailure(cause)  => throw cause 
        }
    }

    def getAsync(keys: K*) = {
      (endpoint ? GetByKeys(keys))
        .map {
          case GetByKeysSuccess(values) => values 
          case GetByKeysFailure(cause)  => throw cause 
        }
    }

    def getAsync(keys: Set[K] = Set()) = {
      (endpoint ? GetKeyValues(keys))
        .map {
          case GetKeyValuesSuccess(kvs) => kvs
          case GetKeyValuesFailure(cause)  => throw cause 
        }
    }
  }

  trait Write[K, V] extends ActorSelectionStore.Access with KeyValueStore.Write[K, V] {
    this: ActorSelectionStore =>

    val protocol: StoreProtocol with KeyValueStoreProtocol.Write[K, V]
    import protocol._

    /**
     */
    def setAsync(kvs: Map[K, V]) = {
      (endpoint ? SetKeyValues(kvs))
        .map {
          case SetKeyValuesSuccess => //
          case SetKeyValuesFailure(cause)  => throw cause 
        }
    }

    /**
     */
    def addAsync(kvs: Map[K, V]) = {
      (endpoint ? AddKeyValues(kvs))
        .map {
          case AddKeyValuesSuccess => //
          case AddKeyValuesFailure(cause)  => throw cause 
        }
    }

    def deleteAsync(keys: K*) = {
      (endpoint ? DeleteByKeys(keys))
        .map {
          case DeleteByKeysSuccess => //
          case DeleteByKeysFailure(cause)  => throw cause 
        }
    }
  }

  type Full[K, V] = Read[K, V] with Write[K, V]
}

trait ActorSelectionKeyValueStore[K, V] extends ActorSelectionStore 
  with KeyValueStore[K, V] 
  with ActorSelectionKeyValueStore.Read[K, V]
  with ActorSelectionKeyValueStore.Write[K, V]
{

  val protocol: StoreProtocol with KeyValueStoreProtocol.Full[K, V]
  
}

