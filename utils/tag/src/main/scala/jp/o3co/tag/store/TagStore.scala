package jp.o3co.tag
package store

import scala.concurrent.Future

trait TagStore[O] extends TagStoreLike[O]

trait TagStoreLike[O] extends TagStoreComponents[O] {

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
   * @return Updated TagNameSet
   */
  def addTags(owner: Owner, tags: TagNameSet): Future[TagNameSet]

  /**
   * Remove tags with owner
   *
   * @return Updated TagNameSet
   */
  def removeTags(owner: Owner, tags: TagNameSet): Future[TagNameSet]

  /**
   * Remove all Removed TagNameSet
   *
   * @return Removed TagNameSet
   */
  def removeAllTags(owner: Owner): Future[TagNameSet]

  /**
   * Replace all tags with new tags
   * 
   * @return Removed TagNameSet 
   */
  def replaceTags(owner: Owner, tags: TagNameSet): Future[TagNameSet]
}
