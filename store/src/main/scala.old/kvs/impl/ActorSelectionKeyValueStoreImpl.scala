package o3co.store.kvs 

import akka.actor.ActorSelection
import akka.pattern.ask
import akka.util.Timeout
import o3co.store.StoreProtocol
import o3co.store.ActorSelectionStoreLike
import scala.concurrent.ExecutionContext

trait ActorSelectionProxy[K, V] {
  def endpoint: ActorSelection

  implicit def timeout: Timeout

  implicit def executionContext: ExecutionContext

  val protocol: KeyValueStoreProtocol[K, V]
}

trait ActorSelectionReadAccessLike[K, V] extends ActorSelectionProxy[K, V] with ReadAccessLike[K, V] {
  this: ReadAccess[K, V] => 

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

trait ActorSelectionWriteAccessLike[K, V] extends ActorSelectionProxy[K, V] with WriteAccessLike[K, V] {
  this: WriteAccess[K, V] =>

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

//  /**
//   *
//   */
//  def replaceAsync(key: K, value: V) = {
//    (endpoint ? ReplaceKeyValue(key, value))
//      .map {
//        case ReplaceKeyValueSuccess => //
//        case ReplaceKeyValueFailure(cause)  => throw cause 
//      }
//  }

  def deleteAsync(keys: K*) = {
    (endpoint ? DeleteByKeys(keys))
      .map {
        case DeleteByKeysSuccess => //
        case DeleteByKeysFailure(cause)  => throw cause 
      }
  }

//  def deleteByKeyAsync(keys: K *) = {
//    (endpoint ? DeleteByKeys(keys))
//      .map {
//        case DeleteByKeysSuccess => //
//        case DeleteByKeysFailure(cause) => throw cause
//      }
//  }
}

trait ReadOnlyActorSelectionKeyValueStoreLike[K, V] extends ReadOnlyKeyValueStoreLike[K, V] 
  with ActorSelectionStoreLike
  with ActorSelectionReadAccessLike[K, V]
{
  this: ReadOnlyKeyValueStore[K, V] =>
}

trait ActorSelectionKeyValueStoreLike[K, V] extends KeyValueStore[K, V] 
  with ActorSelectionStoreLike
  with ActorSelectionReadAccessLike[K, V] 
  with ActorSelectionWriteAccessLike[K, V]
{
  this: KeyValueStore[K, V] =>
}

trait ReadOnlyActorSelectionKeyValueStore[K, V] extends ReadOnlyKeyValueStore[K, V] with ReadOnlyActorSelectionKeyValueStoreLike[K, V]

trait ActorSelectionKeyValueStore[K, V] extends KeyValueStore[K, V] with ActorSelectionKeyValueStoreLike[K, V] 
