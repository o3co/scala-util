package jp.o3co.id.provider

import scala.concurrent.Future

/**
 *
 */
trait IDProviderLike[ID] {
  /**
   *
   */
  def existsAsync(id: ID): Future[Boolean] 

  /**
   *
   */
  def generateAsync(): Future[ID]

  /**
   *
   */
  def releaseAsync(id: ID): Future[Unit]
}

/**
 *
 */
trait IDProvider[ID] extends IDProviderLike[ID]
