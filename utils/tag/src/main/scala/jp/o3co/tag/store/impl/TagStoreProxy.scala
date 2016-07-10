package jp.o3co.tag
package store
package impl

trait TagStoreProxy[O] extends TagStore[O] with TagStoreProxyLike[O]

trait TagStoreProxyLike[Owner] extends TagStoreLike[Owner] {

  def underlying: TagStoreLike[Owner]

  /**
   *
   */
  def getTagsForAsync(owner: Owner) = underlying.getTagsForAsync(owner)

  /**
   *
   */
  def getOwnersForAsync(name: TagName) = underlying.getOwnersForAsync(name)

  /**
   *
   */
  def tagExistsAsync(owner: Owner, name: TagName) = underlying.tagExistsAsync(owner, name)

  /**
   *
   */
  def putTagAsync(owner: Owner, name: TagName) = underlying.putTagAsync(owner, name)

  /**
   *
   */
  def putTagSetAsync(tags: Set[(Owner, TagName)]) = underlying.putTagSetAsync(tags)

  /**
   *
   */
  def deleteTagAsync(owner: Owner, name: TagName) = underlying.deleteTagAsync(owner, name)

  /**
   *
   */
  def deleteTagSetAsync(tags: Set[(Owner, TagName)]) = underlying.deleteTagSetAsync(tags)

  /**
   *
   */
  def deleteAllTagsAsync(owner: Owner) = underlying.deleteAllTagsAsync(owner)

  /**
   *
   */
  def deleteAllTagsAsync(name: TagName) = underlying.deleteAllTagsAsync(name)

  /**
   *
   */
  def replaceAllTagsAsync(owner: Owner, names: TagNameSet) = underlying.replaceAllTagsAsync(owner, names)
}

object TagStoreProxy {
  def apply[O](tagStore: TagStore[O]) = new TagStoreProxy[O] {
    override val underlying = tagStore
  }
}
