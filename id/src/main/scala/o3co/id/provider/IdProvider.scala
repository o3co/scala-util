package o3co.id.provider

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import scala.util.Failure

/**
 *
 */
trait IdProvider[ID] {

  /**
   *
   */
  def existsAsync(id: ID): Future[Boolean] 

  /**
   *
   */
  def generateAsync(): Future[ID] 

  /**
   */
  def reserveAsync(id: ID): Future[Unit]

  /**
   *
   */
  def releaseAsync(id: ID): Future[Unit]
}

