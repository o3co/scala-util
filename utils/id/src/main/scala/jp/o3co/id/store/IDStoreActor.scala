package jp.o3co.id.store

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.ExecutionContext

/**
 *
 */
trait IDStoreActorLike[ID] {
  this: Actor with IDStoreLike[ID] =>

  val protocol: IDStoreProtocolLike[ID]
  import protocol._

  implicit def executionContext: ExecutionContext

  def receiveIDStoreEvents: Receive = {
    case IDExists(id) => 
      existsAsync(id)
        .map(exists => IDExistsSuccess(exists))
        .pipeTo(sender())
    case PutID(id) => 
      putAsync(id)
        .map(_ => PutIDSuccess())
        .pipeTo(sender())
    case DeleteID(id) => 
      deleteAsync(id)
        .map(_ => DeleteIDSuccess())
        .pipeTo(sender())
  }
}

/**
 *
 */
trait MySQLIDStoreActorLike[ID] extends impl.MySQLIDStoreImpl[ID] {
  this: Actor =>  
}
