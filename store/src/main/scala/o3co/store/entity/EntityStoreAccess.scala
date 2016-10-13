package o3co.store
package entity

import scala.concurrent.Future

trait ReadAccess[K, E <: Entity[K]] extends kvs.ReadAccess[K, E] {
  this: Store =>

  def idsAsync: Future[Set[K]]
}

trait WriteAccess[K, E <: Entity[K]] extends EntityStore.Write[K, E] {
  this: Store =>
  //store.vs.WriteAccess[E] {
//
//  def deleteByIdAsync(ids: K *): Future[Unit]
}

trait ReadAccessLike[K, E <: Entity[K]] extends kvs.ReadAccessLike[K, E] {
  this: ReadAccess[K, E] =>
  
  def idsAsync: Future[Set[K]] = keysAsync
}

trait WriteAccessLike[K, E <: Entity[K]] extends vs.WriteAccessLike[E] {
  this: WriteAccess[K, E] =>
//
//  def deleteAsync(entities: E *) = deleteByIdAsync(entities.map(_.id): _*)
}
