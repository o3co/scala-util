package jp.o3co.datastore
package entity
package taggable 

import jp.o3co.tag.TagName
import jp.o3co.tag.owner.TagOwnerProtocolLike

trait TaggableEntityStoreProtocol[K, E <: BaseEntity[K]] extends EntityStoreProtocolLike[K, E] with TagOwnerProtocolLike[K] with TaggableEntityStoreProtocolLike[K, E]

/**
 *
 */
trait TaggableEntityStoreProtocolLike[K, E <: BaseEntity[K]] extends EntityStoreComponents[K, E] {

  case class GetEntityWithTags(key: EntityKey)
  case class GetEntityWithTagsSuccess(entity: Option[Entity], tags: Set[TagName] = Set())
  case class GetEntityWithTagsFailure(cause: Throwable)

  case class PutEntityWithTags(entity: Entity, tags: Set[TagName] = Set())
  case class PutEntityWithTagsSuccess(prev: Option[Entity])
  case class PutEntityWithTagsFailure(cause: Throwable)

  case class DeleteEntityWithTags(key: EntityKey)
  case class DeleteEntityWithTagsSuccess(removed: Option[Entity])
  case class DeleteEntityWithTagsFailure(cause: Throwable)

  case class AddTag(key: EntityKey, tag: TagName)
  case class AddTagSuccess(tags: Set[TagName])
  case class AddTagFailure(cause: Throwable)

  case class AddRemoveTag(key: EntityKey, tag: TagName)
  case class AddRemoveTagSuccess(tags: Set[TagName])
  case class AddRemoveTagFailure(cause: Throwable)
}

