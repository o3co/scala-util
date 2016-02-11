package jp.o3co.datastore
package kvs
package impl

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

trait BaseKeyValueStoreImpl[K, V] extends mutable.Map[K, V]

trait KeyValueStoreImplLike[K, V, +This <: KeyValueStoreImplLike[K, V, This] with BaseKeyValueStoreImpl[K, V]] extends mutable.MapLike[K, V, This] with KeyValueStore[K, V] {

  implicit def executionContext: ExecutionContext

  var kvs: Map[K, V] = Map()

  def get(key: Key): Option[Value] = kvs.get(key)

  def iterator = kvs.iterator

  def +=(kv: (Key, Value)): this.type = {
    kvs = kvs + kv
    this
  }

  def -=(key: Key): this.type = {
    kvs = kvs - key
    this
  }

  def containsAsync(key: Key) = {
    val exists = contains(key)
    Future(exists)
  }
  
  def getAsync(key: Key) = {
    val value = kvs.get(key)
    Future(value)
  }

  def putAsync(key: Key, value: Value) = {
    val prev = put(key, value)
    Future(prev)
  }

  def deleteAsync(key: Key) = {
    val deleted = remove(key)
    Future(deleted)
  }
}

class KeyValueStoreImpl[K, V](implicit val executionContext: ExecutionContext) extends BaseKeyValueStoreImpl[K, V] with KeyValueStoreImplLike[K, V, KeyValueStoreImpl[K, V]] {

  override def empty = new KeyValueStoreImpl[K, V]
}

object KeyValueStoreImpl {
  def apply[K, V](implicit ec: ExecutionContext) = new KeyValueStoreImpl[K, V]
}

