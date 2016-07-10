package o3co.store.entity

import o3co.store
import scala.concurrent.Future

trait ReadAccess[K, E <: Entity[K]] extends store.kvs.ReadAccess[K, E] {

  def idsAsync: Future[Set[K]]
}

trait WriteAccess[K, E <: Entity[K]] extends store.vs.WriteAccess[E] {

  def deleteByIdAsync(ids: K *): Future[Unit]
}

trait ReadAccessLike[K, E <: Entity[K]] extends store.kvs.ReadAccessLike[K, E] {
  this: ReadAccess[K, E] =>
  
  def idsAsync: Future[Set[K]] = keysAsync
}

trait WriteAccessLike[K, E <: Entity[K]] extends store.vs.WriteAccessLike[E] {
  this: WriteAccess[K, E] =>

  def deleteAsync(entities: E *) = deleteByIdAsync(entities.map(_.id): _*)
}
