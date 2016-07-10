package jp.o3co.datastore
package entity
package taggable 

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import jp.o3co.tag.{Tag, TagNameSet}

trait TaggableEntityStore[K, E <: BaseEntity[K]] extends EntityStore[K, E] with TaggableEntityStoreLike[K, E] 

/**
 * Store pattern for TaggableEntity. 
 *  
 */
trait TaggableEntityStoreLike[Key, Entity <: BaseEntity[Key]] {

  /**
   *
   * @return Tuple of Entity and TagNameSet if Entity exists. 
   */
  def getEntityWithTagsAsync(key: Key): Future[Option[(Entity, TagNameSet)]]

  /**
   * @return Previous entity if exists.
   */
  def putEntityWithTagsAsync(entity: Entity, tags: TagNameSet): Future[Unit]

  /**
   * @return Removed Entity
   */
  def deleteEntityWithTagsAsync(key: Key): Future[Unit]
}

