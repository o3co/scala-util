package o3co.store.vs 

import scala.concurrent.Future

/**
 * 
 */
trait ReadAccess[V] {
  /**
   *
   */
  def existsAsync(value: V): Future[Boolean]

  def valuesAsync(): Future[Seq[V]]
}

trait ReadAccessLike[V] {
  this: ReadAccess[V] => 
}

trait WriteAccess[V] {
  /**
   * Upsert values
   */
  def putAsync(values: V *): Future[Unit]

  /**
   * Add new values
   */
  def addAsync(values: V *): Future[Unit]

  /**
   *
   */
  def deleteAsync(values: V *): Future[Unit]
}

trait WriteAccessLike[V] {
  this: WriteAccess[V] => 
}

