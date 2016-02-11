package jp.o3co.datastore
package entity


trait EntityStoreProtocol[K, E <: BaseEntity[K]] extends EntityStoreProtocolLike[K, E]

/**
 *
 */
trait EntityStoreProtocolLike[K, E <: BaseEntity[K]] extends EntityStoreComponents[K, E] {

  case object CountEntities 
  case class CountEntitiesComplete(size: Long) 
  case class CountEntitiesFailure(cause: Throwable) 

  case class EntityExists(key: EntityKey)
  case class EntityExistsComplete(exsits: Boolean)
  case class EntityExistsFailure(cause: Throwable)

  case class GetKeys()
  case class GetKeysComplete(keys: Seq[EntityKey])
  case class GetKeysFailure(cause: Throwable)

  case class GetEntity(key: EntityKey)
  case class GetEntityComplete(entity: Option[Entity])
  case class GetEntityFailure(cause: Throwable)

  case class GetEntities(keys: Seq[EntityKey])
  case class GetEntitiesComplete(entities: Seq[Entity])
  case class GetEntitiesFailure(cause: Throwable)

  case class PutEntity(entity: Entity)
  case class PutEntityComplete(prev: Option[Entity])
  case class PutEntityFailure(cause: Throwable)

  case class DeleteEntity(key: EntityKey)
  case class DeleteEntityComplete(removed: Option[Entity])
  case class DeleteEntityFailure(cause: Throwable)
}
