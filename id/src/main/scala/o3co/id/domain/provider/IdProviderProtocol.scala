package o3co.id.domain.provider

import o3co.net.Domain
import o3co.id.{provider => base}

/**
 */
trait IdProviderProtocol[ID] extends base.IdProviderProtocol[ID] {
  
  /**
   *
   */
  case class ExistsOnDomain(id: ID, domain: Domain)

  /**
   *
   */
  case class GenerateOnDomain(domain: Domain)

  /**
   */
  case class ReserveOnDomain(id: ID, domain: Domain)

  /**
   *
   */
  case class ReleaseOnDomain(id: ID, domain: Domain)
}

