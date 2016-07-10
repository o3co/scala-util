package jp.o3co.datastore
package entity
package taggable 

import jp.o3co.tag.TagName

/**
 */
trait TaggableEntityStoreProtocol[K, E <: BaseEntity[K]] extends EntityStoreProtocol[K, E] with TaggableEntityStoreProtocolLike[K, E]

/**
 *
 */
trait TaggableEntityStoreProtocolLike[Key, Entity <: BaseEntity[Key]] {

  case class GetEntityWithTags(key: Key)
  case class GetEntityWithTagsSuccess(entity: Option[Entity], tags: Set[TagName] = Set())
  case class GetEntityWithTagsFailure(cause: Throwable)

  case class PutEntityWithTags(entity: Entity, tags: Set[TagName] = Set())
  case class PutEntityWithTagsSuccess()
  case class PutEntityWithTagsFailure(cause: Throwable)

  case class DeleteEntityWithTags(key: Key)
  case class DeleteEntityWithTagsSuccess()
  case class DeleteEntityWithTagsFailure(cause: Throwable)
}

