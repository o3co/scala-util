package jp.o3co.datastore.entity
package impl


trait EntityStoreProxy[K, E <: BaseEntity[K]] extends EntityStore[K, E] with EntityStoreProxyLike[K, E]

/**
 *
 */
trait EntityStoreProxyLike[K, E <: BaseEntity[K]] extends EntityStoreLike[K, E] {

  def underlying: EntityStoreLike[K, E]

  def countEntitiesAsync = underlying.countEntitiesAsync

  def entityExistsAsync(key: EntityKey) = underlying.entityExistsAsync(key)
  
  def getEntityAsync(key: EntityKey) = underlying.getEntityAsync(key)

  def getEntitiesAsync(keys: Seq[EntityKey]) = underlying.getEntitiesAsync(keys)

  def putEntityAsync(entity: Entity) = underlying.putEntityAsync(entity)

  def deleteEntityAsync(key: EntityKey) = underlying.deleteEntityAsync(key)
}
