package jp.o3co.id.domain.store

import jp.o3co.net.DomainName
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

/**
 *
 */
trait DomainIDStoreLike[ID] {

  /**
   *
   */
  def existsAsync(domain: DomainName, id: ID): Future[Boolean]

  /**
   *
   */
  def putAsync(domain: DomainName, id: ID): Future[Unit]

  /**
   *
   */
  def deleteAsync(domain: Option[DomainName], id: ID): Future[Unit]
}

/**
 *
 */
trait DomainIDStore[ID] extends DomainIDStoreLike[ID]
