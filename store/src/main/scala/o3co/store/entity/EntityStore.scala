package o3co.store
package entity

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

/**
 */
object EntityStore {

  trait Searchable[K, E <: Entity[K], C] {
    this: EntityStore.Read[K, E] => 

    import o3co.search._

    def findAsync(condition: Option[C] = None, order: Option[OrderByFields] = None, size: Size = All, offset: Offset = 0): Future[(Seq[K], Long)]
  }

  trait Base extends Store 

  trait Read[K, E <: Entity[K]] extends Base with kvs.KeyValueStore.Read[K, E] {
    /**
     * Get ids 
     */
    def idsAsync: Future[Set[K]]
  }

  trait Write[K, E <: Entity[K]] extends Base with vs.ValueStore.Write[E] {
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

  type Full[K, E <: Entity[K]] = Read[K, E] with Write[K, E]
}

/**
 * Base interface of EntityStore 
 */
trait EntityStore[K, E <: Entity[K]] extends EntityStore.Read[K, E] with EntityStore.Write[K, E]

// ReadOnly Store APIs
/**
 * Base interface of ReadOnly EntityStore
 */
trait ReadOnlyEntityStore[K, E <: Entity[K]] extends EntityStore.Read[K, E]

