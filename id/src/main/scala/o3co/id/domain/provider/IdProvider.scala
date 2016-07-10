package o3co.id.domain.provider

import o3co.id.{provider => base}
import o3co.net.Domain
import scala.concurrent.Future


trait IdProvider[ID] extends base.IdProvider[ID] {

  /**
   *
   */
  def existsAsync(id: ID, domain: Domain): Future[Boolean]

  /**
   * Create new ID on the domain
   */
  def generateAsync(domain: Domain): Future[ID]

  /**
   * Reserve ID on the domain
   */
  def reserveAsync(id: ID, domain: Domain): Future[Unit]

  /**
   * Reelase ID on the Domain
   */
  def releaseAsync(id: ID, domain: Domain): Future[Unit]
}

