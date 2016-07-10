package jp.o3co.id.domain.store

import akka.util.Timeout
import akka.actor.ActorRef
import akka.pattern.ask
import scala.concurrent.ExecutionContext
import jp.o3co.net.DomainName

/**
 *
 */
trait DomainIDStoreActorRefAdapterLike[ID] {

  val protocol: DomainIDStoreProtocolLike[ID]
  import protocol._

  implicit def timeout: Timeout

  implicit def executionContext: ExecutionContext

  def endpoint: ActorRef

  /**
   *
   */
  def existsAsync(domain: DomainName, id: ID) = {
    (endpoint ? DomainIDExists(domain, id))
      .map {
        case DomainIDExistsSuccess(exists) => exists
        case DomainIDExistsFailure(cause) => throw cause 
      }
  }

  /**
   *
   */
  def putAsync(domain: DomainName, id: ID) = {
    (endpoint ? PutDomainID(domain, id))
      .map {
        case PutDomainIDSuccess() => (): Unit
        case PutDomainIDFailure(cause) => throw cause 
      }
  }

  /**
   *
   */
  def deleteAsync(domain: Option[DomainName], id: ID) = {
    (endpoint ? DeleteDomainID(domain, id))
      .map {
        case DeleteDomainIDSuccess() => (): Unit 
        case DeleteDomainIDFailure(cause) => throw cause 
      }
  }
}

trait DomainIDStoreActorRefAdapter[ID] extends DomainIDStore[ID] with DomainIDStoreActorRefAdapterLike[ID] {
  val protocol: DomainIDStoreProtocol[ID]
}

