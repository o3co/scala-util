package o3co.tag
package store

/**
 *
 */
trait TagStoreProtocol[O, T <: Tag[T]] {

  case class GetTags(owner: O)
  case class GetTagsSuccess(tags: Set[T])
  case class GetTagsFailure(cause: Throwable)

  case class GetOwners(tag: T)
  case class GetOwnersSuccess(owners: Set[O])
  case class GetOwnersFailure(cause: Throwable)

  case class TagExists(owner: O, tag: T)
  case class TagExistsSuccess(exists: Boolean)
  case class TagExistsFailure(cause: Throwable)

  case class PutTag(owner: O, tag: T)
  case class PutTagSuccess()
  case class PutTagFailure(cause: Throwable)

  case class PutTagSet(tags: Set[(O, T)])
  case class PutTagSetSuccess()
  case class PutTagSetFailure(cause: Throwable)

  case class DeleteTag(owner: O, tag: T)
  case class DeleteTagSuccess()
  case class DeleteTagFailure(cause: Throwable)

  case class DeleteTagSet(tags: Set[(O, T)])
  case class DeleteTagSetSuccess()
  case class DeleteTagSetFailure(cause: Throwable)

  case class DeleteAllTagsByOwner(owner: O)
  case class DeleteAllTagsByName(tag: T)
  case class DeleteAllTagsSuccess()
  case class DeleteAllTagsFailure(cause: Throwable)

  case class ReplaceTags(owner: O, newTags: Set[T], oldTags: Set[T])
  case class ReplaceTagsSuccess()
  case class ReplaceTagsFailure(cause: Throwable)

  case class ReplaceAllTags(owner: O, tags: Set[T])
  case class ReplaceAllTagsSuccess()
  case class ReplaceAllTagsFailure(cause: Throwable)
}

