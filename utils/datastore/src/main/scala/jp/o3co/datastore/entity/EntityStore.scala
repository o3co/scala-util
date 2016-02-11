package jp.o3co.datastore
package entity

import scala.concurrent.Future

trait EntityStore[K, E <: BaseEntity[K]] extends EntityStoreLike[K, E]

trait EntityStoreLike[K, E <: BaseEntity[K]] extends EntityStoreComponents[K, E] {

  /**
   *
   * @return Number of entities exists
   */
  def countEntitiesAsync(): Future[Long] 

  /**
   * Check the key is exists or not on async
   *
   * @return True if the entity exists, false otherwise
   */
  def entityExistsAsync(key: EntityKey): Future[Boolean]
  
  /**
   * Get the entity for key on async
   */
  def getEntityAsync(key: EntityKey): Future[Option[Entity]]

  //def listAsync(size: Long, offset: Long): Future[Traversable[Entity]]

  //def listWithCountAsync(size: Long, offset: Long): Future[(Traversable[Entity], Long)]

  /**
   * Get entities by keys
   */
  def getEntitiesAsync(keys: Seq[EntityKey]): Future[Seq[Entity]]

  /** 
   * Return all keys
   */
  def getKeysAsync(): Future[Seq[EntityKey]] 

  /**
   * Put entity
   */
  def putEntityAsync(entity: Entity): Future[Option[Entity]]

  /**
   * Delete entity by key
   */
  def deleteEntityAsync(key: EntityKey): Future[Option[Entity]]
}

