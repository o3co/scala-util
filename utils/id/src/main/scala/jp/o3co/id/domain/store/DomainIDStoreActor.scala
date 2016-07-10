package jp.o3co.id.domain.store

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.ExecutionContext

/**
 *
 */
trait DomainIDStoreActorLike[ID] {
  this: Actor with DomainIDStoreLike[ID] => 

  val protocol: DomainIDStoreProtocolLike[ID]
  import protocol._

  implicit def executionContext: ExecutionContext

  def receiveDomainIDStoreEvents: Receive = {
    case DomainIDExists(domain, id) => 
      existsAsync(domain, id)
        .map(exists => DomainIDExistsSuccess(exists))
        .pipeTo(sender())
    case PutDomainID(domain, id) => 
      putAsync(domain, id)
        .map(_ => PutDomainIDSuccess())
        .pipeTo(sender())
    case DeleteDomainID(domain, id) => 
      deleteAsync(domain, id)
        .map(_ => DeleteDomainIDSuccess())
        .pipeTo(sender())
  }
}

