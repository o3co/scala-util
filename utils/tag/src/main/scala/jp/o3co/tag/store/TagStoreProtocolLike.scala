package jp.o3co.tag
package store

trait TagStoreProtocol[O] extends TagStoreProtocolLike[O]

/**
 *
 */
trait TagStoreProtocolLike[O] extends TagStoreComponents[O] {

  case class ContainsTag(owner: Owner, tag: TagName)
  case class ContainsTagComplete(exists: Boolean)
  case class ContainsTagFailure(cause: Throwable)

  case class GetTags(owner: Owner)
  case class GetTagsComplete(tags: TagNameSet)
  case class GetTagsFailure(cause: Throwable)

  case class AddTags(owner: Owner, tags: TagNameSet)
  case class AddTagsComplete(updated: TagNameSet)
  case class AddTagsFailure(cause: Throwable)

  case class RemoveTags(owner: Owner, tags: TagNameSet)
  case class RemoveTagsComplete(updated: TagNameSet)
  case class RemoveTagsFailure(cause: Throwable)

  case class RemoveAllTags(owner: Owner)
  case class RemoveAllTagsComplete(removed: TagNameSet)
  case class RemoveAllTagsFailure(cause: Throwable)

  case class ReplaceTags(owner: Owner, tags: TagNameSet)
  case class ReplaceTagsComplete(removed: TagNameSet)
  case class ReplaceTagsFailure(cause: Throwable)
}
