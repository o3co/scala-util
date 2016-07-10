package jp.o3co.datastore.entity
package taggable
package impl

import jp.o3co.datastore.entity.EntityStoreLike
import jp.o3co.datastore.entity.impl.EntityStoreProxyLike
import jp.o3co.tag.TagNameSet

/**
 *
 */
trait TaggableEntityStoreProxy[K, E <: BaseEntity[K]] extends EntityStoreProxyLike[K, E] with TaggableEntityStoreProxyLike[K, E] {

  def underlying: EntityStoreLike[K, E] with TaggableEntityStoreLike[K, E]
}

/**
 *
 */
trait TaggableEntityStoreProxyLike[Key, Entity <: BaseEntity[Key]] extends TaggableEntityStoreLike[Key, Entity] {

  /**
   *
   */
  def underlying: TaggableEntityStoreLike[Key, Entity]

  implicit def executionContext: scala.concurrent.ExecutionContext

  /**
   *
   */
  def getEntityWithTagsAsync(key: Key) = underlying.getEntityWithTagsAsync(key)

  /**
   *
   */
  def putEntityWithTagsAsync(entity: Entity, tags: TagNameSet) = underlying.putEntityWithTagsAsync(entity, tags)

  /**
   *
   */
  def deleteEntityWithTagsAsync(key: Key) = underlying.deleteEntityWithTagsAsync(key)
}

