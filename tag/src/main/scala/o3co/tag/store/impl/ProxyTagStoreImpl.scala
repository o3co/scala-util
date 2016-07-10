package o3co.tag
package store
package impl

/**
 */
trait ProxyTagStoreImpl[O, T <: Tag[T]] extends TagStore[O, T] with ProxyTagStoreImplLike[O, T]

/**
 */
trait ProxyTagStoreImplLike[O, T <: Tag[T]] extends TagStoreLike[O, T] {

  def underlying: TagStoreLike[O, T]

  /**
   *
   */
  def getTagsAsync(owner: O) =
    underlying.getTagsAsync(owner)

  /**
   *
   */
  def getOwnersAsync(tag: T) =
    underlying.getOwnersAsync(tag)

  /**
   *
   */
  def tagExistsAsync(owner: O, tag: T) =
    underlying.tagExistsAsync(owner, tag)

  /**
   *
   */
  def putTagAsync(owner: O, tag: T) =
    underlying.putTagAsync(owner, tag)

  /**
   *
   */
  def putTagSetAsync(tags: Set[(O, T)]) =
    underlying.putTagSetAsync(tags)

  /**
   *
   */
  def deleteTagAsync(owner: O, tag: T) =
    underlying.deleteTagAsync(owner, tag)

  /**
   *
   */
  def deleteTagSetAsync(tags: Set[(O, T)]) =
    underlying.deleteTagSetAsync(tags)

  /**
   *
   */
  def deleteAllTagsAsync(owner: O) =
    underlying.deleteAllTagsAsync(owner)

  /**
   *
   */
  def deleteAllTagsAsync(tag: T) =
    underlying.deleteAllTagsAsync(tag)

  /**
   *
   */
  def replaceAllTagsAsync(owner: O, tags: Set[T]) =
    underlying.replaceAllTagsAsync(owner, tags)
}

object ProxyTagStoreImpl {
  def apply[O, T <: Tag[T]](tagStore: TagStore[O, T]) =
    new ProxyTagStoreImpl[O, T] {
      override val underlying = tagStore
    }
}
