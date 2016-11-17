package o3co.store.kvs 

import o3co.store
import scala.concurrent.ExecutionContext


trait ProxyReadAccessLike[K, V] extends ReadAccessLike[K, V] {
  this: ReadAccess[K, V] =>

  def endpoint: ReadAccess[K, V]

  def existsAsync(key: K) = endpoint.existsAsync(key)

  def keysAsync = endpoint.keysAsync

  def getAsync(keys: K *) = endpoint.getAsync(keys: _*)
  
  def getAsync(keys: Set[K] = Set()) = endpoint.getAsync(keys)
}

trait ProxyWriteAccessLike[K, V] extends WriteAccessLike[K, V] {
  this: WriteAccess[K, V] =>

  // 
  def endpoint: WriteAccess[K, V] 

  /**
   */
  def setAsync(kvs: Map[K, V]) = endpoint.setAsync(kvs)

  /**
   */
  def addAsync(kvs: Map[K, V]) = endpoint.addAsync(kvs)

//  def replaceAsync(key: K, value: V) = endpoint.replaceAsync(key: K, value: V)

  def deleteAsync(key: K *) = endpoint.deleteAsync(key: _*)

//  def deleteByKeyAsync(keys: K *) = endpoint.deleteByKeyAsync(keys: K *)

}

trait ReadOnlyProxyKeyValueStore[K, V] extends store.ProxyStore with ReadOnlyKeyValueStore[K, V] with ProxyReadAccessLike[K, V] {
  def endpoint: store.Store with ReadAccess[K, V]
}

trait ProxyKeyValueStore[K, V] extends store.ProxyStore with KeyValueStore[K, V] with ProxyReadAccessLike[K, V] with ProxyWriteAccessLike[K, V] {
  
  def endpoint: store.Store with ReadAccess[K, V] with WriteAccess[K, V]
}
