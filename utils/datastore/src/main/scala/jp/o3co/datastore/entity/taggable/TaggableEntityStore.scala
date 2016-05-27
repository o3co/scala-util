package jp.o3co.datastore
package entity
package taggable 

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import jp.o3co.tag.owner.TagOwnerLike 
import jp.o3co.tag.{Tag, TagNameSet}

trait TaggableEntityStore[K, E <: BaseEntity[K]] extends EntityStoreLike[K, E] with TagOwnerLike[K] with TaggableEntityStoreLike[K, E] 

/**
 * Store pattern for TaggableEntity. 
 *  
 */
trait TaggableEntityStoreLike[K, E <: BaseEntity[K]] extends EntityStoreComponents[K, E] {
  //self: EntityStore[K, E] with TagOwner[K] => 

  /**
   *
   * @return Tuple of Entity and TagNameSet if Entity exists. 
   */
  def getEntityWithTagsAsync(key: EntityKey): Future[Option[(Entity, TagNameSet)]]

  /**
   * @return Previous entity if exists.
   */
  def putEntityWithTagsAsync(entity: Entity, tags: TagNameSet): Future[Option[Entity]]

  /**
   * @return Removed Entity
   */
  def deleteEntityWithTagsAsync(key: EntityKey): Future[Option[Entity]]
}

/**
 * Provide base implementation of TaggableEntityStoreLike with combination of EntityStore and TagOwner
 */
trait TaggableEntityStoreImplLike[K, E <: BaseEntity[K]] extends TaggableEntityStoreLike[K, E] {
  self: EntityStoreLike[K, E] with TagOwnerLike[K] =>

  implicit def executionContext: ExecutionContext
  
  /**
   *
   */
  def getEntityWithTagsAsync(key: EntityKey) = {
    val fEntity = getEntityAsync(key)
    val fTags   = getTags(key)

    for {
      entity <- fEntity
      tags   <- fTags
    } yield (entity.map(e => (e, tags)))
  }//: Future[Option[(Entity, TagNameSet)]]

  /**
   *
   */
  def putEntityWithTagsAsync(entity: Entity, tags: TagNameSet) = {
    val fPrevEntity   = putEntityAsync(entity)
    val fPrevTags     = replaceTags(entity.entityKey, tags)

    (for {
      prevEntity <- fPrevEntity
      prevTags   <- fPrevTags
    } yield prevEntity)
  }//: Future[Option[Entity]]

  /**
   *
   */
  def deleteEntityWithTagsAsync(key: EntityKey) = {
    val fRemovedEntity   = deleteEntityAsync(key)
    val fRemovedTags     = removeAllTags(key)

    for {
      removedEntity <- fRemovedEntity
      removedTags   <- fRemovedTags
    } yield (removedEntity)
  }//: Future[Option[Entity]]
}
