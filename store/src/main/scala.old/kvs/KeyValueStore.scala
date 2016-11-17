package o3co.store
package kvs

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import o3co.store.Store

trait ReadOnlyKeyValueStore[K, V] extends Store with ReadAccess[K, V]

trait KeyValueStore[K, V] extends ReadOnlyKeyValueStore[K, V] with WriteAccess[K, V]

trait ReadOnlyKeyValueStoreLike[K, V] extends StoreLike with ReadAccessLike[K, V] {
  this: ReadOnlyKeyValueStore[K, V] =>
}

trait KeyValueStoreLike[K, V] extends KeyValueStore[K, V] with ReadAccessLike[K, V] with WriteAccessLike[K, V] {
  this: KeyValueStore[K, V] =>
}

