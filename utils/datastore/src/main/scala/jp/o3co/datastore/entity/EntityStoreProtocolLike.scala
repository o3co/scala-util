package jp.o3co.datastore
package entity


trait EntityStoreProtocol[K, E <: BaseEntity[K]] extends EntityStoreProtocolLike[K, E]

/**
 *
 */
trait EntityStoreProtocolLike[K, E <: BaseEntity[K]] extends EntityStoreComponents[K, E] {

  case object CountEntities 
  case class CountEntitiesSuccess(size: Long) 
  case class CountEntitiesFailure(cause: Throwable) 

  case class EntityExists(key: EntityKey)
  case class EntityExistsSuccess(exsits: Boolean)
  case class EntityExistsFailure(cause: Throwable)

  case class GetKeys()
  case class GetKeysSuccess(keys: Seq[EntityKey])
  case class GetKeysFailure(cause: Throwable)

  case class GetEntity(key: EntityKey)
  case class GetEntitySuccess(entity: Option[Entity])
  case class GetEntityFailure(cause: Throwable)

  case class GetEntities(keys: Seq[EntityKey])
  case class GetEntitiesSuccess(entities: Seq[Entity])
  case class GetEntitiesFailure(cause: Throwable)

  case class PutEntity(entity: Entity)
  case class PutEntitySuccess(prev: Option[Entity])
  case class PutEntityFailure(cause: Throwable)

  case class DeleteEntity(key: EntityKey)
  case class DeleteEntitySuccess(removed: Option[Entity])
  case class DeleteEntityFailure(cause: Throwable)
}
