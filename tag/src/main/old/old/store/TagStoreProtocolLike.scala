package jp.o3co.tag
package store

/**
 *
 */
trait TagStoreProtocol[O] extends TagStoreProtocolLike[O]

/**
 *
 */
trait TagStoreProtocolLike[Owner] {

  case class GetTagsFor(owner: Owner)
  case class GetTagsForSuccess(names: TagNameSet)
  case class GetTagsForFailure(cause: Throwable)

  case class GetOwnersFor(name: TagName)
  case class GetOwnersForSuccess(owners: Set[Owner])
  case class GetOwnersForFailure(cause: Throwable)

  case class TagExists(owner: Owner, name: TagName)
  case class TagExistsSuccess(exists: Boolean)
  case class TagExistsFailure(cause: Throwable)

  case class PutTag(owner: Owner, name: TagName)
  case class PutTagSuccess()
  case class PutTagFailure(cause: Throwable)

  case class DeleteTag(owner: Owner, name: TagName)
  case class DeleteTagSuccess()
  case class DeleteTagFailure(cause: Throwable)

  case class PutTagSet(tags: Set[(Owner, TagName)])
  case class PutTagSetSuccess()
  case class PutTagSetFailure(cause: Throwable)

  case class DeleteTagSet(tags: Set[(Owner, TagName)])
  case class DeleteTagSetSuccess()
  case class DeleteTagSetFailure(cause: Throwable)

  case class DeleteAllTagsByOwner(owner: Owner)
  case class DeleteAllTagsByName(name: TagName)
  case class DeleteAllTagsSuccess()
  case class DeleteAllTagsFailure(cause: Throwable)

  case class ReplaceAllTags(owner: Owner, names: TagNameSet)
  case class ReplaceAllTagsSuccess()
  case class ReplaceAllTagsFailure(cause: Throwable)
}
