package o3co.store.entity

import o3co.store
import o3co.conversion._
import scala.concurrent.ExecutionContext

trait MappedReadAccessLike[K, A <: Entity[K], B <: Entity[K]] extends ReadAccessLike[K, B] with store.kvs.MappedReadAccessLike[K, A, B] {
  this: ReadAccess[K, B] =>

  def endpoint: ReadAccess[K, A]

  def converter: Conversion[A, B]
}

trait MappedWriteAccessLike[K, A <: Entity[K], B <: Entity[K]] extends WriteAccessLike[K, B] with store.vs.MappedWriteAccessLike[A, B] {
  this: WriteAccess[K, B] =>

  def endpoint: WriteAccess[K, A]

  def converter: ReversibleConversion[A, B]

  def deleteByIdAsync(ids: K *) = 
    endpoint.deleteByIdAsync(ids: _*)

  override def deleteAsync(entities: B *) = 
    deleteByIdAsync(entities.map(_.id): _*)
}


trait ReadOnlyMappedEntityStore[K, A <: Entity[K], B <: Entity[K]] extends store.ProxyStore with ReadOnlyEntityStore[K, B] with MappedReadAccessLike[K, A, B] {

  def endpoint: store.Store with ReadAccess[K, A]

  def converter: Conversion[A, B]
}

/**
 * MappedEntityStore[K, A, B] is a Proxy EntityStore which implements EntityStore[K, B]
 * and has undelrying EntityStore[K, A] with conversion A => B, B => A
 *
 */
trait MappedEntityStore[K, A <: Entity[K], B <: Entity[K]] extends store.ProxyStore with EntityStore[K, B] with ReadOnlyMappedEntityStore[K, A, B] with MappedWriteAccessLike[K, A, B] {

  def endpoint: store.Store with ReadAccess[K, A] with WriteAccess[K, A]

  def converter: ReversibleConversion[A, B]
}
