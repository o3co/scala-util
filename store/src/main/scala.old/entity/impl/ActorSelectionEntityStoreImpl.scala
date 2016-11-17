package o3co.store.entity

import akka.actor.ActorSelection
import akka.pattern.ask
import akka.util.Timeout
import o3co.store.vs
import o3co.store.kvs
import o3co.store.{ActorSelectionStore, ActorSelectionStoreLike}

trait ActorSelectionReadAccessLike[K, E <: Entity[K]] extends ReadAccessLike[K, E] with kvs.ActorSelectionReadAccessLike[K, E]  {
  this: ReadAccess[K, E] =>

  val protocol: EntityStoreProtocol[K, E]
}

trait ActorSelectionWriteAccessLike[K, E <: Entity[K]] extends WriteAccessLike[K, E] with vs.ActorSelectionWriteAccessLike[E] {
  this: WriteAccess[K, E] =>

  val protocol: EntityStoreProtocol[K, E]
  import protocol._

  override def deleteAsync(values: E *) = super[WriteAccessLike].deleteAsync(values: _*)

  def deleteByIdAsync(ids: K *) = 
    (endpoint ? DeleteByKeys(ids))
      .map {
        case DeleteByKeysSuccess => //
        case DeleteByKeysFailure(cause) => throw cause
      }
}


// Actual
trait ReadOnlyActorSelectionEntityStoreLike[K, E <: Entity[K]] extends ReadOnlyEntityStoreLike[K, E] 
  with ActorSelectionStoreLike 
  with ActorSelectionReadAccessLike[K, E] 
{
  this: ReadOnlyEntityStore[K, E] => 
}

trait ActorSelectionEntityStoreLike[K, E <: Entity[K]] extends EntityStoreLike[K, E] 
  with ActorSelectionStoreLike 
  with ActorSelectionReadAccessLike[K, E] 
  with ActorSelectionWriteAccessLike[K, E] 
{
  this: EntityStore[K, E] =>  
}

//
trait ReadOnlyActorSelectionEntityStore[K, E <: Entity[K]] extends ReadOnlyEntityStore[K, E] with ReadOnlyActorSelectionEntityStoreLike[K, E] 

trait ActorSelectionEntityStore[K, E <: Entity[K]] extends EntityStore[K, E] with ActorSelectionEntityStoreLike[K, E] 
