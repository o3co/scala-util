package o3co.dictionary

import scala.concurrent.Future

trait BaseDictionary[K, V] {

  def containsKey(key: K): Boolean

  def containsValue(value: V): Boolean
  
  def get(key: K): Option[V]

  def put(key: K, value: V): Unit

  def remove(key: K): Unit

  def keys: Set[K] 

  def values: Set[V]
}

trait BaseAsyncDictionary[K, V] {

  def containsAsync(key: K): Future[Boolean]

  def containsValueAsync(value: V): Future[Boolean]
  
  def getAsync(key: K): Future[Option[V]]

  def putAsync(key: K, value: V): Future[Unit]

  def removeAsync(key: K): Future[Unit]

  def keysAsync: Future[Set[K]]

  def valuesAsync: Future[Set[V]]
}
