package o3co.store.entity

import o3co.store

trait ProxyReadAccessLike[K, E <: Entity[K]]  extends ReadAccessLike[K, E] with store.kvs.ProxyReadAccessLike[K, E] {
  this: ReadAccess[K, E] =>

  def endpoint: ReadAccess[K, E]
}

trait ProxyWriteAccessLike[K, E <: Entity[K]]  extends WriteAccessLike[K, E] with store.vs.ProxyWriteAccessLike[E] {
  this: WriteAccess[K, E] =>

  def endpoint: WriteAccess[K, E]

  def deleteByIdAsync(ids: K *) = 
    endpoint.deleteByIdAsync(ids: _*)

  override def deleteAsync(entities: E *) = super[WriteAccessLike].deleteAsync(entities: _*)
}

// Actual Likeementations 
/*
 *
 */
trait ReadOnlyProxyEntityStore[K, E <: Entity[K]] extends store.ProxyStore with ReadOnlyEntityStore[K, E] with ProxyReadAccessLike[K, E] {
  def endpoint: store.Store with ReadAccess[K, E]
}

/**
 * Actual implmementation of the ProxyEntityStore
 */
trait ProxyEntityStore[K, E <: Entity[K]] extends store.ProxyStore with EntityStore[K, E] with ReadOnlyProxyEntityStore[K, E] with ProxyWriteAccessLike[K, E] {
  def endpoint: store.Store with ReadAccess[K, E] with WriteAccess[K, E]
}
