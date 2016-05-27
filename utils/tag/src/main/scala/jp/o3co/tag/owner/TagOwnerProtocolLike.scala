package jp.o3co.tag
package owner

trait TagOwnerProtocol[O] extends TagOwnerProtocolLike[O]

/**
 *
 */
trait TagOwnerProtocolLike[O] extends TagOwnerComponents[O] {

  case class ContainsTag(owner: Owner, tag: TagName)
  case class ContainsTagComplete(exists: Boolean)
  case class ContainsTagFailure(cause: Throwable)

  case class GetTags(owner: Owner)
  case class GetTagsComplete(tags: TagNameSet)
  case class GetTagsFailure(cause: Throwable)

  case class AddTags(owner: Owner, tags: TagNameSet)
  case class AddTagsComplete()
  case class AddTagsFailure(cause: Throwable)

  case class RemoveTags(owner: Owner, tags: TagNameSet)
  case class RemoveTagsComplete()
  case class RemoveTagsFailure(cause: Throwable)

  case class RemoveAllTags(owner: Owner)
  case class RemoveAllTagsComplete()
  case class RemoveAllTagsFailure(cause: Throwable)

  case class ReplaceTags(owner: Owner, tags: TagNameSet)
  case class ReplaceTagsComplete()
  case class ReplaceTagsFailure(cause: Throwable)
}
