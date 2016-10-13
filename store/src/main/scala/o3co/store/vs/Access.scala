package o3co.store
package vs 

import scala.concurrent.Future

/**
 * 
 */
trait ReadAccess[V] extends ValueStore.Read[V] {
  this: Store =>
//  /**
//   *
//   */
//  def existsAsync(value: V): Future[Boolean]
//
//  def valuesAsync(): Future[Seq[V]]
}

trait ReadAccessLike[V] {
  //this: ReadAccess[V] => 
  this: ValueStore.Read[V] =>
}

trait WriteAccess[V] extends ValueStore.Write[V] {
  this: Store =>
//  /**
//   * Upsert values
//   */
//  def putAsync(values: V *): Future[Unit]
//
//  /**
//   * Add new values
//   */
//  def addAsync(values: V *): Future[Unit]
//
//  /**
//   *
//   */
//  def deleteAsync(values: V *): Future[Unit]
}

trait WriteAccessLike[V] {
  //this: WriteAccess[V] => 
  this: ValueStore.Write[V] => 
}

