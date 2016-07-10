package jp.o3co.id.domain.provider
package impl

import jp.o3co.id.domain.store.DomainIDStore
import jp.o3co.id.provider.impl.StoredIDProviderImpl
import jp.o3co.id.provider._
import jp.o3co.net.DomainName
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

trait StoredDomainIDProviderImplLike[ID] extends DomainIDProviderImpl[ID] {
  
  def domainIdStore: DomainIDStore[ID]

  implicit def executionContext: ExecutionContext

  /**
   *
   */
  protected def doExistsAsync(id: ID, domain: DomainName): Future[Boolean] = {
    domainIdStore.existsAsync(domain, id)
  }

  /**
   * Create new ID for the given domain
   */
  protected def doGenerateAsync(domain: DomainName): Future[ID] = {
    doGenerateAsync().flatMap { id =>
      domainIdStore.putAsync(fqdn(domain), id)
        .map(_ => id)
    }
  }

  /**
   *
   */
  protected def doReleaseAsync(id: ID, domain: Option[DomainName]): Future[Unit] = {
    for {
      _ <- doReleaseAsync(id)
      _ <- domainIdStore.deleteAsync(domain, id)    
    } yield ()
  }
}

trait StoredDomainIDProviderImpl[ID] extends StoredIDProviderImpl[ID] with StoredDomainIDProviderImplLike[ID] {

}
