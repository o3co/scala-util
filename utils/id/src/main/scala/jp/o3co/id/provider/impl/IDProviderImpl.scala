package jp.o3co.id.provider
package impl

import scala.concurrent.Future

trait IDProviderImpl[ID] extends IDProvider[ID] {

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

  /**
   *
   */
  protected def doReleaseAsync(id: ID): Future[Unit]

  def releaseAsync(id: ID) = doReleaseAsync(id)
}
