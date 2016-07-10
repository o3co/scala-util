package jp.o3co.datastore
package entity
package taggable 
package impl

import jp.o3co.tag.TagNameSet
import jp.o3co.tag.store.TagStoreLike
import jp.o3co.datastore.entity.EntityStoreLike
import scala.concurrent.ExecutionContext

/**
 * Provide base implementation of TaggableEntityStoreLike with combination of EntityStore and TagStore
 */
trait TaggableEntityStoreImplLike[Key, Entity <: BaseEntity[Key]] extends TaggableEntityStoreLike[Key, Entity] {

  type EntityStore = EntityStoreLike[Key, Entity]

  type TagStore = TagStoreLike[Key]

  def entityStore: EntityStore

  def tagStore: TagStore

  implicit def executionContext: ExecutionContext
  
  /**
   *
   */
  def getEntityWithTagsAsync(key: Key) = {
    val fEntity = entityStore.getEntityAsync(key)
    val fTags   = tagStore.getTagsForAsync(key)

    for {
      entity <- fEntity
      tags   <- fTags
    } yield (entity.map(e => (e, tags)))
  }//: Future[Option[(Entity, TagNameSet)]]

  /**
   *
   */
  def putEntityWithTagsAsync(entity: Entity, tags: TagNameSet) = {
    val fPrevEntity   = entityStore.putEntityAsync(entity)
    val fPrevTags     = tagStore.replaceAllTagsAsync(entity.entityKey, tags)

    (for {
      prevEntity <- fPrevEntity
      prevTags   <- fPrevTags
    } yield (): Unit)
  }//: Future[Unit]

  /**
   *
   */
  def deleteEntityWithTagsAsync(key: Key) = {
    val fRemovedEntity   = entityStore.deleteEntityAsync(key)
    val fRemovedTags     = tagStore.deleteAllTagsAsync(key)

    for {
      removedEntity <- fRemovedEntity
      removedTags   <- fRemovedTags
    } yield (): Unit
  }//: Future[Unit]
}
