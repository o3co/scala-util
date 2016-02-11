package jp.o3co.tag
package store
package impl

trait TagStoreProxy[O] extends TagStore[O] with TagStoreProxyLike[O]

trait TagStoreProxyLike[O] extends TagStoreLike[O] {

  def underlying: TagStoreLike[O]

  /**
   * Check single tag is existed with owner
   */
  def containsTag(owner: Owner, tag: TagName) = underlying.containsTag(owner, tag)
 
  /**
   * Get all tags with owner
   */
  def getTags(owner: Owner) = underlying.getTags(owner)
  
  /**
   * Add tags to owner
   */
  def addTags(owner: Owner, tags: TagNameSet) = underlying.addTags(owner, tags)

  /**
   * Remove tags with owner
   */
  def removeTags(owner: Owner, tags: TagNameSet) = underlying.removeTags(owner, tags)

  /**
   * Remove all tags with owner
   */
  def removeAllTags(owner: Owner) = underlying.removeAllTags(owner)

  /**
   * 
   */
  def replaceTags(owner: Owner, tags: TagNameSet) = underlying.replaceTags(owner, tags)
}

object TagStoreProxy {
  def apply[O](tagStore: TagStore[O]) = new TagStoreProxy[O] {
    override val underlying = tagStore
  }
}
