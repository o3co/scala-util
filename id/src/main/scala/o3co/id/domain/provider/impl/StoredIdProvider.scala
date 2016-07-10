package o3co.id.domain.provider

import o3co.exceptions._
import o3co.net.Domain
import o3co.store.kvs.KeyValueStore
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

trait StoredIdProviderLike[ID] extends IdProviderLike[ID] {
  this: IdProvider[ID] =>
  
  def idStore: KeyValueStore[ID, Domain]

  implicit def executionContext: ExecutionContext

  /**
   *
   */
  protected def doExistsAsync(id: ID, domain: Domain): Future[Boolean] = {
    idStore.getAsync(id).map {
      case Some(value) => value == domain
      case None => false
    }
  }

  /**
   * Create new ID for the given domain
   */
  protected def doGenerateAsync(domain: Domain): Future[ID] = {
    val id = generator.generate.get
    idStore.addAsync(id, domain)
      .map(_ => id)
  }

  protected def doReserveAsync(id: ID, domain: Domain): Future[Unit] = {
    idStore.addAsync(id, domain)
  }

  /**
   *
   */
  protected def doReleaseAsync(id: ID, domain: Domain): Future[Unit] = {
    idStore.getAsync(id).flatMap { 
      case Some(d) if d.isSubdomainOf(domain) => idStore.deleteAsync(id)
      case _ => throw new NotFoundException(s"ID $id on Domain $domain is not exists.")
    }
  }
}

trait StoredIdProvider[ID] extends IdProvider[ID] with StoredIdProviderLike[ID]
