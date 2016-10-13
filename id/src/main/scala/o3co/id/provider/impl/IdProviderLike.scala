package o3co.id.provider

import scala.concurrent.Future

/**
 */
trait IdProviderLike[ID] {
  this: IdProvider[ID] => 

  /**
   *
   */
  protected def doExistsAsync(id: ID): Future[Boolean] 

  def existsAsync(id: ID) = doExistsAsync(id)

  /**
   *
   */
  protected def doGenerateAsync(): Future[ID]

  def generateAsync() = doGenerateAsync()

  protected def doReserveAsync(id: ID): Future[Unit]

  def reserveAsync(id: ID) = doReserveAsync(id)

  /**
   *
   */
  protected def doReleaseAsync(id: ID): Future[Unit]

  def releaseAsync(id: ID) = doReleaseAsync(id)
}

