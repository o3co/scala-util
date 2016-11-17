package o3co.store
package kvs 

import akka.actor.Actor
import akka.pattern.pipe
import o3co.actor.ServiceActor
import o3co.store
import scala.concurrent.ExecutionContext

object KeyValueStoreActor {

  trait Read[K, V] extends StoreActor.Access {
    this: Actor with Store with KeyValueStore.Read[K, V] =>

    val protocol: KeyValueStoreProtocol.Read[K, V]
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

  trait Write[K, V] extends StoreActor.Access {
    this: Actor with Store with KeyValueStore.Write[K, V] =>

    val protocol: KeyValueStoreProtocol.Write[K, V]
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

  type Full[K, V] = Read[K, V] with Write[K, V] 
}

/**
 *
 */
trait KeyValueStoreActor[K, V] extends StoreActor with KeyValueStoreActor.Read[K, V] with KeyValueStoreActor.Write[K, V] {
  this: Store with KeyValueStore.Read[K, V] with KeyValueStore.Write[K, V] =>

  val protocol: StoreProtocol with KeyValueStoreProtocol.Full[K, V]

  override def receiveStoreCommands = {
    super[StoreActor].receiveStoreCommands orElse 
    super[Read].receiveStoreCommands orElse 
    super[Write].receiveStoreCommands
  }
}

