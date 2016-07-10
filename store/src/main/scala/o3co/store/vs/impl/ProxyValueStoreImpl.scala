package o3co.store.vs 

import o3co.store.ProxyStoreLike

trait ProxyReadAccessLike[V] extends ReadAccessLike[V] {
  this: ReadAccess[V] =>

  def endpoint: ReadAccess[V] 

  def existsAsync(value: V) = endpoint.existsAsync(value)

  def valuesAsync() = endpoint.valuesAsync()
}

trait ProxyWriteAccessLike[V] extends WriteAccessLike[V] {
  this: WriteAccess[V] =>

  def endpoint: WriteAccess[V] 

  def putAsync(values: V *) = endpoint.putAsync(values: _*)

  def addAsync(values: V *) = endpoint.addAsync(values: _*)

  def deleteAsync(values: V *) = endpoint.deleteAsync(values: _*)
}

// ValueStore implementation
trait ReadOnlyProxyValueStoreLike[V] extends ReadOnlyValueStoreLike[V] with ProxyReadAccessLike[V] {
  this: ReadOnlyValueStore[V] =>

  def endpoint: ReadOnlyValueStore[V] 
}

trait ProxyValueStoreLike[V] extends ValueStoreLike[V] with ProxyWriteAccessLike[V] {
  this: ValueStore[V] =>

  def endpoint: ValueStore[V] 
}

trait ReadOnlyProxyValueStore[V] extends ReadOnlyValueStore[V] with ReadOnlyProxyValueStoreLike[V]

trait ProxyValueStore[V] extends ValueStore[V] with ProxyValueStoreLike[V]
