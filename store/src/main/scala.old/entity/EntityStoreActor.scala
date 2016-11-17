package o3co.store.entity

import akka.actor.Actor
import o3co.actor.ServiceActor
import o3co.store

/**
 */
trait ReceiveReadAccess[K, E <: Entity[K]] extends store.kvs.ReceiveReadAccess[K, E] {
  this: Actor with ReadAccess[K, E] =>

  val protocol: EntityStoreProtocol[K, E]
}

/**
 */
trait ReceiveWriteAccess[K, E <: Entity[K]] extends store.vs.ReceiveWriteAccess[E] {
  this: Actor with WriteAccess[K, E] =>

  val protocol: EntityStoreProtocol[K, E]
}

/**
 */
trait EntityStoreReceiver[K, E <: Entity[K]] extends store.ReceiveCountableStore with ReceiveReadAccess[K, E] with ReceiveWriteAccess[K, E] {
  this: Actor with EntityStore[K, E] => 

  override def receiveStoreCommands = 
    super[ReceiveCountableStore].receiveStoreCommands orElse 
    super[ReceiveReadAccess].receiveStoreCommands orElse 
    super[ReceiveWriteAccess].receiveStoreCommands
}

trait EntityStoreActor[K, E <: Entity[K]] extends ServiceActor with EntityStoreReceiver[K, E] {
  this: EntityStore[K, E] =>

  def receive = receiveStoreCommands orElse receiveExtension
}
