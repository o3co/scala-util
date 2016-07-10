package jp.o3co.id.domain.store

import jp.o3co.net.DomainName

/**
 *
 */
trait DomainIDStoreProtocolLike[ID] {
  case class DomainIDExists(domain: DomainName, id: ID)
  case class DomainIDExistsSuccess(exists: Boolean)
  case class DomainIDExistsFailure(cause: Throwable)

  case class PutDomainID(domain: DomainName, id: ID)
  case class PutDomainIDSuccess()
  case class PutDomainIDFailure(cause: Throwable)

  case class DeleteDomainID(domain: Option[DomainName], id: ID)
  case class DeleteDomainIDSuccess()
  case class DeleteDomainIDFailure(cause: Throwable)
}

trait DomainIDStoreProtocol[ID] extends DomainIDStoreProtocolLike[ID] 
