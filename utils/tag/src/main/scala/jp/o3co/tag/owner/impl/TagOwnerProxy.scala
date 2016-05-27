package jp.o3co.tag
package owner
package impl

trait TagOwnerProxy[O] extends TagOwner[O] with TagOwnerProxyLike[O]

trait TagOwnerProxyLike[O] extends TagOwnerLike[O] {

  def underlying: TagOwnerLike[O]

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

object TagOwnerProxy {
  def apply[O](tagStore: TagOwner[O]) = new TagOwnerProxy[O] {
    override val underlying = tagStore
  }
}
