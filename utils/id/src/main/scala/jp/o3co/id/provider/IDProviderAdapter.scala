package jp.o3co.id.provider

import scala.concurrent.Future

/**
 *
 */
trait IDProviderAdapterLike[ID] extends IDProviderLike[ID] {

  /**
   */
  def existsAsync(id: ID) = doExistsAsync(id)

  /**
   */
  protected def doExistsAsync(id: ID): Future[Boolean]

  /**
   */
  def generateAsync() = doGenerateAsync()

  /**
   */
  protected def doGenerateAsync(): Future[ID]

  /**
   */
  def releaseAsync(id: ID) = doReleaseAsync(id)

  /**
   */
  protected def doReleaseAsync(id: ID): Future[Unit]
}

