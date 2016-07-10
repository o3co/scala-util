package o3co.store.entity

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
