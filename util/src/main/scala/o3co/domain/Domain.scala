package o3co.domain

/**
 * Domain
 */
trait Domain {
  /**
   * Name of the domain
   */
  def domainName: DomainName
}

/**
 * Object contains under the domain
 */
trait DomainObject {
  /**
   * Domain of where this object exists
   */
  def domain: DomainName

  /**
   * Path of this object under the domain
   */
  def domainPath: DomainName
}

