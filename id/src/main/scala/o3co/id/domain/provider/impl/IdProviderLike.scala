package o3co.id.domain.provider

import o3co.net.Domain
import scala.concurrent.Future
import o3co.id.{provider => base}
import o3co.generate.Generate

/**
 */
trait IdProviderLike[ID] {
  this: IdProvider[ID] => 

  def rootDomain: Option[Domain]

  def generator: Generate[ID]

  /**
   * Make FQDN for given domain name
   */
  protected def fqdn(domain: Domain): Domain = domain match {
    case Domain(_, true) => 
      rootDomain match {
        case Some(root) if(!domain.isSubdomainOf(root)) => 
          throw new IllegalArgumentException(s"Domain $domain is not a subdomain of $root.")
        case _ => 
          domain
      }
    case Domain(_, false) => 
      rootDomain match {
        case Some(root) => root.subdomain(domain)
        case None       => domain
      }
  }

  // API to validate id existence
  def existsAsync(id: ID) = doExistsAsync(id, rootDomain.getOrElse(throw new Exception("Either rootDomain or parameter domain has to be specified."))) 
  def existsAsync(id: ID, domain: Domain) = doExistsAsync(id, fqdn(domain))
  protected def doExistsAsync(id: ID, domain: Domain): Future[Boolean]
  
  // API to generate id
  def generateAsync() = doGenerateAsync(rootDomain.getOrElse(throw new Exception("Either rootDomain or parameter domain has to be specified."))) 
  def generateAsync(domain: Domain) = doGenerateAsync(fqdn(domain))
  protected def doGenerateAsync(domain: Domain): Future[ID]

  // API to release id
  def releaseAsync(id: ID) = doReleaseAsync(id, rootDomain.getOrElse(throw new Exception("Either rootDomain or parameter domain has to be specified.")))
  def releaseAsync(id: ID, domain: Domain) = doReleaseAsync(id, fqdn(domain))
  protected def doReleaseAsync(id: ID, domain: Domain): Future[Unit]
  
  // API to reserve id
  def reserveAsync(id: ID) = doReleaseAsync(id, rootDomain.getOrElse(throw new Exception("Either rootDomain or parameter domain has to be specified.")))
  def reserveAsync(id: ID, domain: Domain) = doReserveAsync(id, fqdn(domain))
  protected def doReserveAsync(id: ID, domain: Domain): Future[Unit]
}

