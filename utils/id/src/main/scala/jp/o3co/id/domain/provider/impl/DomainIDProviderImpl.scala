package jp.o3co.id.domain.provider
package impl

import jp.o3co.id.provider.impl.IDProviderImpl
import jp.o3co.net.DomainName
import scala.concurrent.Future

/**
 *
 */
trait DomainIDProviderImpl[ID] extends IDProviderImpl[ID] with DomainIDProvider[ID] {

  /**
   * Root Domain of this IDProvider if specified.
   */
  val rootDomain: Option[DomainName] = None

  /**
   * Make FQDN for given domain name
   */
  def fqdn(domain: DomainName): DomainName = domain match {
    case DomainName(_, true) => 
      rootDomain match {
        case Some(root) if(!domain.isSubdomainOf(root)) => 
          throw new IllegalArgumentException(s"Domain $domain is not a subdomain of $root.")
        case _ => 
          domain
      }
    case DomainName(_, false) => 
      rootDomain match {
        case Some(root) => root.subdomain(domain)
        case None       => domain
      }
  }

  /**
   *
   */
  protected def doExistsAsync(id: ID, domain: DomainName): Future[Boolean]

  def existsAsync(id: ID, domain: DomainName) = doExistsAsync(id, domain)

  override def existsAsync(id: ID) = rootDomain match {
    case Some(domain) => doExistsAsync(id, domain)
    case None    => doExistsAsync(id)
  }

  /**
   * Create new ID for the given domain
   */
  protected def doGenerateAsync(domain: DomainName): Future[ID]

  def generateAsync(domain: DomainName) = doGenerateAsync(fqdn(domain)) 

  override def generateAsync() = rootDomain match {
    case Some(domain) => doGenerateAsync(domain)
    case None       => doGenerateAsync()
  }

  /**
   *
   */
  protected def doReleaseAsync(id: ID, domain: Option[DomainName]): Future[Unit]

  def releaseAsync(id: ID, domain: DomainName) = doReleaseAsync(id, Some(fqdn(domain)))

  override def releaseAsync(id: ID) = doReleaseAsync(id, rootDomain)
}
