package o3co.store
package entity

import o3co.store.{Store, StoreLike}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

/**
 * Base interface of EntityStore 
 */
trait EntityStore[K, E <: Entity[K]] extends Store with ReadOnlyEntityStore[K, E] with WriteAccess[K, E]

/**
 * Base trait of EntityStoreLike which implement the template of EntityStore
 */
trait EntityStoreLike[K, E <: Entity[K]] extends StoreLike with ReadAccessLike[K, E] with WriteAccessLike[K, E] {
  this: EntityStore[K, E] => 
}

// ReadOnly Store APIs
/**
 * Base interface of ReadOnly EntityStore
 */
trait ReadOnlyEntityStore[K, E <: Entity[K]] extends Store with ReadAccess[K, E]

/**
 * Base trait of EntityStoreLike which implement the template of EntityStore
 */
trait ReadOnlyEntityStoreLike[K, E <: Entity[K]] extends StoreLike with ReadAccessLike[K, E] {
  this: ReadOnlyEntityStore[K, E] => 
}

object EntityStore {

  trait Searchable[K, E <: Entity[K], C] {
    this: EntityStore[K, E] => 

    import o3co.search._

    def findAsync(condition: Option[C] = None, order: Option[OrderByFields] = None, size: Size = All, offset: Offset = 0): Future[(Seq[K], Long)]
  }

  trait Read[K, E <: Entity[K]] {
    this: Store => 

    /**
     * Get ids 
     */
    def idsAsync: Future[Set[K]]
  }

  trait Write[K, E <: Entity[K]] extends vs.ValueStore.Write[E] {
    this: Store => 

    /**
     * Delete entities by ids
     */
    def deleteByIdAsync(ids: K *): Future[Unit]

    /**
     * Delete entities
     */
    def deleteAsync(entities: E *): Future[Unit] = 
      deleteByIdAsync(entities.map(_.id): _*)
  }
}
