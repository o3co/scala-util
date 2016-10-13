package o3co.tag
package store

/**
 */
trait ProxyTagStore[O, T <: Tag[T]] extends TagStore[O, T] {

  def underlying: TagStore[O, T]

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
  def putTagAsync(owner: O, tags: Set[T]) = 
    underlying.putTagAsync(owner, tags)

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
  def deleteTagAsync(owner: O, tags: Set[T]) =
    underlying.deleteTagAsync(owner, tags)

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
   * Replace oldTags to newTags
   */
  def replaceTagsAsync(owner: O, newTags: Set[T], oldTags: Set[T]) =
    underlying.replaceTagsAsync(owner, newTags, oldTags)

  /**
   * replace all existed tags to new tags
   */
  def replaceAllTagsAsync(owner: O, tags: Set[T]) =
    underlying.replaceAllTagsAsync(owner, tags)
}

