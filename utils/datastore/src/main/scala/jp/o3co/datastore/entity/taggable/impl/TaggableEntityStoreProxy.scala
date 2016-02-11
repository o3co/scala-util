package jp.o3co.datastore.entity
package taggable
package impl

import jp.o3co.tag.store.TagStoreLike
import jp.o3co.tag.store.impl.TagStoreProxyLike
import jp.o3co.datastore.entity.EntityStoreLike
import jp.o3co.datastore.entity.impl.EntityStoreProxyLike
import jp.o3co.tag.TagNameSet

/**
 *
 */
trait TaggableEntityStoreProxy[K, E <: BaseEntity[K]] extends EntityStoreProxyLike[K, E] with TagStoreProxyLike[K] with TaggableEntityStoreProxyLike[K, E] {

  val underlying: EntityStoreLike[K, E] with TagStoreLike[K] with TaggableEntityStoreLike[K, E]
}

/**
 *
 */
trait TaggableEntityStoreProxyLike[K, E <: BaseEntity[K]] extends TaggableEntityStoreLike[K, E] {

  /**
   *
   */
  def underlying: TaggableEntityStoreLike[K, E]

  implicit def executionContext: scala.concurrent.ExecutionContext

  /**
   *
   */
  def getEntityWithTagsAsync(key: EntityKey) = underlying.getEntityWithTagsAsync(key)

  /**
   *
   */
  def putEntityWithTagsAsync(entity: Entity, tags: TagNameSet) = underlying.putEntityWithTagsAsync(entity, tags)

  /**
   *
   */
  def deleteEntityWithTagsAsync(key: EntityKey) = underlying.deleteEntityWithTagsAsync(key)
}

