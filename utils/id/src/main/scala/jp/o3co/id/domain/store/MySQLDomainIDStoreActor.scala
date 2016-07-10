package jp.o3co.id.domain.store

import akka.actor.Actor

/**
 *
 */
trait MySQLIDStoreActorLike[ID] extends impl.MySQLDomainIDStoreImpl[ID] {
  this: Actor =>  
}
