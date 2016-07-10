package jp.o3co.id.domain.provider

import jp.o3co.id.provider._
import jp.o3co.net.DomainName
import scala.concurrent.Future

/**
 * 
 * {{{
 *  val comIdP = DomainIDProvider[ID]("com")
 *  
 *  val id: Future[ID] = comIdP.generateAsync(DomainName("sample"))
 *
 *  // relate id with new domain "com.test"
 *  comIdP.putAsync(id, DomainName("test"))
 *
 *  val trueOnCom = comIdP.existsAsync(id)
 *  val trueOnComSample = comIdP.existsAsync(id, DomainName("sample"))
 *  val falseOnComFoo   = comIdP.existsAsync(id, DomainName("foo"))
 *
 *  // Release on subdomain
 *  comIdP.releaseAsync(id, DomainName("sample"))
 *  // Release from all
 *  comIdP.releaseAsync(id)
 *
 *  // Easier to work, create SubdomainIDProvider adapter
 *  val comSampleIdP = SubdomainIDProviderAdapter(comIdP, DomainName("sample"))
 *
 *  val newId: Future[ID] = comSampleIdP.generateAsync()
 * }}}
 */
trait DomainIDProviderLike[ID] {
  this: IDProviderLike[ID] => 
  
  /**
   *
   */
  def existsAsync(id: ID, domain: DomainName): Future[Boolean]

  /**
   * Create new ID for the given domain
   */
  def generateAsync(domain: DomainName): Future[ID]

  /**
   *
   */
  def releaseAsync(id: ID, domain: DomainName): Future[Unit]
}

trait DomainIDProvider[ID] extends IDProvider[ID] with DomainIDProviderLike[ID]
