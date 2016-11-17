package o3co.store.kvs

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import o3co.store.Store

trait ReadAccess[K, V] {
  def existsAsync(key: K): Future[Boolean]

  def keysAsync: Future[Set[K]]
  
  def getAsync(key: K): Future[Option[V]]

  def getAsync(keys: Set[K] = Set()): Future[Map[K, V]]

  /**
   * Get in order
   */
  def getAsync(keys: K *): Future[Seq[V]]
}

trait WriteAccess[K, V] {
  /**
   */
  def setAsync(key: K, value: V): Future[Unit]

  def setAsync(kvs: Map[K, V]): Future[Unit]

  /**
   *
   */
  def addAsync(key: K, value: V): Future[Unit]

  def addAsync(kvs: Map[K, V]): Future[Unit]

  def deleteAsync(keys: K *): Future[Unit]
}

trait ReadAccessLike[K, V] {
  this: ReadAccess[K, V] =>

  implicit def executionContext: ExecutionContext

  def getAsync(key: K): Future[Option[V]] = getAsync(Set(key)).map(_.get(key))
}

trait WriteAccessLike[K, V] {
  this: WriteAccess[K, V] => 

  def setAsync(key: K, value: V): Future[Unit] = setAsync(Map(key -> value))

  def addAsync(key: K, value: V): Future[Unit] = addAsync(Map(key -> value))
}
