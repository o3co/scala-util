package o3co.store.kvs 

import akka.actor.Actor
import akka.pattern.pipe
import o3co.actor.ServiceActor
import scala.concurrent.ExecutionContext
import o3co.store

trait ReceiveKeyValueStore[K, V] extends store.ReceiveStore {
  this: Actor =>
  implicit def executionContext: ExecutionContext

  val protocol: KeyValueStoreProtocol[K, V]
}

trait ReceiveReadAccess[K, V] extends ReceiveKeyValueStore[K, V] {
  this: Actor with ReadAccess[K, V] =>

  import protocol._

  def receiveStoreCommands = {
    case ExistsByKey(key) => 
      existsAsync(key)
        .map(ExistsByKeySuccess(_))
        .pipeTo(sender())
    case Get(key) => 
      getAsync(key)
        .map(GetSuccess(_))
        .pipeTo(sender())
    case GetByKeys(keys) => 
      getAsync(keys: _*)
        .map(GetByKeysSuccess(_))
        .pipeTo(sender())
    case GetKeyValues(keys) => 
      getAsync(keys)
        .map(GetKeyValuesSuccess(_))
        .pipeTo(sender())
  }
}

trait ReceiveWriteAccess[K, V] extends ReceiveKeyValueStore[K, V] {
  this: Actor with WriteAccess[K, V] =>

  import protocol._

  def receiveStoreCommands = {
    case SetKeyValues(kvs) => 
      setAsync(kvs)
        .map(_ => SetKeyValuesSuccess)
        .pipeTo(sender())
    case AddKeyValues(kvs) => 
      addAsync(kvs)
        .map(_ => AddKeyValuesSuccess)
        .pipeTo(sender())
    case DeleteByKeys(keys) =>
      deleteAsync(keys: _*)
        .map(_ => DeleteByKeysSuccess)
        .pipeTo(sender())
  }
}

trait KeyValueStoreReceiver[K, V] extends store.ReceiveCountableStore with ReceiveReadAccess[K, V] with ReceiveWriteAccess[K, V] {
  this: Actor with KeyValueStore[K, V] => 

  override def receiveStoreCommands = 
    super[ReceiveCountableStore].receiveStoreCommands orElse 
    super[ReceiveReadAccess].receiveStoreCommands orElse 
    super[ReceiveWriteAccess].receiveStoreCommands
}


trait KeyValueStoreActor[K, V] extends ServiceActor with KeyValueStoreReceiver[K, V] {
  this: KeyValueStore[K, V] =>

  def receive = receiveStoreCommands orElse receiveExtension
}
