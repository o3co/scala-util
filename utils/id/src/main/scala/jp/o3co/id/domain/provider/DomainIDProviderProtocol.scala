package jp.o3co.id.domain.provider

import jp.o3co.net.DomainName
import jp.o3co.id.provider.IDProviderProtocol

/**
 */
trait DomainIDProviderProtocolLike[ID] extends IDProviderProtocol[ID] {
  
  /**
   *
   */
  case class ExistsWithDomain(id: ID, domain: DomainName)

  /**
   *
   */
  case class GenerateWithDomain(domain: DomainName)

  /**
   *
   */
  case class ReleaseWithDomain(id: ID, domain: DomainName)
}

trait DomainIDProviderProtocol[ID] extends DomainIDProviderProtocolLike[ID]
