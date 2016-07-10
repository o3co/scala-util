package jp.o3co.id.store

import scala.concurrent.Future

/**
 *
 */
trait IDStoreLike[ID] {

  /**
   *
   */
  def existsAsync(id: ID): Future[Boolean]

  /**
   *
   */
  def putAsync(id: ID): Future[Unit]

  /**
   *
   */
  def deleteAsync(id: ID): Future[Unit]
}

trait IDStore[ID] extends IDStoreLike[ID]
