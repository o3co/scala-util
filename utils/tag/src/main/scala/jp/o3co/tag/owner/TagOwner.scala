package jp.o3co.tag
package owner

import scala.concurrent.Future

trait TagOwner[O] extends TagOwnerLike[O]

trait TagOwnerLike[O] extends TagOwnerComponents[O] {

  /**
   * Check single tag is existed with owner
   */
  def containsTag(owner: Owner, tag: TagName): Future[Boolean]
 
  /**
   * Get all tags with owner
   */
  def getTags(owner: Owner): Future[TagNameSet]
  
  /**
   * Add tags to owner
   *
   */
  def addTags(owner: Owner, tags: TagNameSet): Future[Unit]

  /**
   * Remove tags with owner
   *
   * @return Updated TagNameSet
   */
  def removeTags(owner: Owner, tags: TagNameSet): Future[Unit]

  /**
   * Remove all Removed TagNameSet
   *
   * @return Removed TagNameSet
   */
  def removeAllTags(owner: Owner): Future[Unit]

  /**
   * Replace all tags with new tags
   * 
   * @return Removed TagNameSet 
   */
  def replaceTags(owner: Owner, tags: TagNameSet): Future[Unit]
}
