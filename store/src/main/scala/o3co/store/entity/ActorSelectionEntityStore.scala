package o3co.store
package entity

import akka.actor.ActorSelection
import akka.pattern.ask
import akka.util.Timeout
import o3co.store
import scala.concurrent.ExecutionContext

object ActorSelectionEntityStore {

  trait Read[K, E <: Entity[K]] extends EntityStore.Read[K, E] with kvs.ActorSelectionKeyValueStore.Read[K, E] {
    this: ActorSelectionStore =>

    val protocol: StoreProtocol with EntityStoreProtocol.Read[K, E]

  }

  trait Write[K, E <: Entity[K]] extends EntityStore.Write[K, E] with vs.ActorSelectionValueStore.Write[E] {
    this: ActorSelectionStore =>

    val protocol: StoreProtocol with EntityStoreProtocol.Write[K, E]
    import protocol._

    override def deleteAsync(values: E *) = super.deleteAsync(values: _*)

    def deleteByIdAsync(ids: K *) = 
      (endpoint ? DeleteByIds(ids))
        .map {
          case DeleteByIdsSuccess => //
          case DeleteByIdsFailure(cause) => throw cause
        }
  }

  type Full[K, E <: Entity[K]] = Read[K, E] with Write[K, E]
}

/**
 *
 */
trait ActorSelectionEntityStore[K, E <: Entity[K]] extends ActorSelectionStore 
  with EntityStore[K, E]
  with ActorSelectionEntityStore.Read[K, E]
  with ActorSelectionEntityStore.Write[K, E]
{
  val protocol: StoreProtocol with EntityStoreProtocol.Full[K, E]
}

